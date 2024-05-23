package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.common.CompanyCondition;
import th.com.bloomcode.paymentservice.model.config.Parameter;
import th.com.bloomcode.paymentservice.model.config.VendorConfig;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;
import th.com.bloomcode.paymentservice.payment.dao.CustomTriggersRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentAliasRepository;
import th.com.bloomcode.paymentservice.service.CustomTriggersSchedulerService;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentAliasServiceImpl implements PaymentAliasService {
  private final PaymentAliasRepository paymentAliasRepository;
  private final PaymentProcessService paymentProcessService;
  private final PaymentInformationService paymentInformationService;
  private final PaymentProposalLogService paymentProposalLogService;
  private final PaymentRealRunLogService paymentRealRunLogService;
  private final GLHeadService glHeadService;
  private final CustomTriggersRepository customTriggersRepository;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public PaymentAliasServiceImpl(PaymentAliasRepository paymentAliasRepository, PaymentProcessService paymentProcessService, PaymentProcessService paymentProcessService1, PaymentInformationService paymentInformationService, PaymentProposalLogService paymentProposalLogService, PaymentRealRunLogService paymentRealRunLogService, GLHeadService glHeadService, CustomTriggersRepository customTriggersRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    this.paymentAliasRepository = paymentAliasRepository;
    this.paymentProcessService = paymentProcessService1;
    this.paymentInformationService = paymentInformationService;
    this.paymentProposalLogService = paymentProposalLogService;
    this.paymentRealRunLogService = paymentRealRunLogService;
    this.glHeadService = glHeadService;
    this.customTriggersRepository = customTriggersRepository;

    this.jdbcTemplate = jdbcTemplate;
  }

  public PaymentAlias save(PaymentAlias paymentAlias) {
    log.info("==== Save PaymentAlias : {}", paymentAlias);
    if (null == paymentAlias.getId() || 0 == paymentAlias.getId()) {
      paymentAlias.setId(SqlUtil.getNextSeries(jdbcTemplate, PaymentAlias.TABLE_NAME + PaymentAlias.SEQ, null));
      paymentAlias.setCreated(new Timestamp(System.currentTimeMillis()));
      paymentAlias.setCreatedBy("SYSTEM");
      paymentAlias.setParameterStatus("S");
    } else {
      paymentAlias.setUpdated(new Timestamp(System.currentTimeMillis()));
      paymentAlias.setUpdatedBy("SYSTEM");
    }
    return paymentAliasRepository.save(paymentAlias);
  }

  @Override
  public ResponseEntity<Result<PaymentAlias>> create(PaymentAlias paymentAlias) {
    Result<PaymentAlias> result = new Result<>();
    result.setTimestamp(new Date());
    try {

      PaymentAlias checkDuplicate = this.paymentAliasRepository
          .findOneByPaymentDateAndPaymentName(paymentAlias.getPaymentDate(), paymentAlias.getPaymentName());

      if (null == checkDuplicate) {
        PaymentAlias newPaymentAlias = save(paymentAlias);
        result.setStatus(HttpStatus.CREATED.value());
        result.setData(newPaymentAlias);
        return new ResponseEntity<>(result, HttpStatus.OK);
      } else {
        result.setStatus(HttpStatus.FORBIDDEN.value());
        result.setData(null);
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<PaymentAlias>> update(PaymentAlias request) {
    Result<PaymentAlias> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      PaymentAlias paymentAlias = paymentAliasRepository.findById(request.getId()).orElse(null);

      if (null != paymentAlias) {

        if (null != request.getProposalStatus() && request.getProposalStatus().equalsIgnoreCase("DELETE")) {
          deleteProposal(paymentAlias);
        } else {
          if (null != request.getJsonText() && !request.getJsonText().equalsIgnoreCase("")) {
            paymentAlias.setJsonText(request.getJsonText());
          }
          if (null != request.getProposalScheduleDate()) {
            request.getProposalScheduleDate().setSeconds(0);
          }
          if (null != request.getRunScheduleDate()) {
            request.getRunScheduleDate().setSeconds(0);
          }
          paymentAlias.setProposalTriggersId(request.getProposalTriggersId());
          paymentAlias.setPaymentTriggersId(request.getPaymentTriggersId());
          paymentAlias.setProposalJobStatus(request.getProposalJobStatus());
          paymentAlias.setRunJobStatus(request.getRunJobStatus());

          paymentAlias.setProposalScheduleDate(request.getProposalScheduleDate());
          paymentAlias.setRunScheduleDate(request.getRunScheduleDate());
        }
        paymentAliasRepository.save(paymentAlias);
      } else {
        paymentAliasRepository.save(request);
      }
      result.setStatus(HttpStatus.OK.value());
      result.setData(paymentAlias);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<PaymentAlias>> updateParameter(PaymentAlias request) {
    Result<PaymentAlias> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      PaymentAlias paymentAlias = paymentAliasRepository.findById(request.getId()).orElse(null);
      deleteProposal(paymentAlias);
      result.setStatus(HttpStatus.OK.value());
      result.setData(paymentAlias);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<PaymentAlias>> searchByPaymentDateAndPaymentName(Timestamp paymentDate, String paymentName) {

    Result<PaymentAlias> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      PaymentAlias checkDuplicate = this.paymentAliasRepository
          .findOneByPaymentDateAndPaymentName(paymentDate, paymentName);

      if (null != checkDuplicate) {
        if (null != checkDuplicate.getRunStatus() && checkDuplicate.getRunStatus().equalsIgnoreCase("S")) {
//                    Long paymentProcessCount = paymentProcessService.findOneByPaymentAliasIdNotParent(checkDuplicate.getId());
          List<PaymentProcess> paymentProcessList = paymentProcessService.findAllByPaymentIdAndProposalNotChild(checkDuplicate.getId(), false);
          PaymentProcess paymentProcess = paymentProcessService.findOneIdemLastUpdatePaymentByPaymentAliasId(checkDuplicate.getId());
          Long idemCreatePaymentReply = paymentProcessService.countIdemCreatePaymentReplyByPaymentAliasId(checkDuplicate.getId());
          if (null != paymentProcess) {
            checkDuplicate.setIdemEnd(paymentProcess.getIdemUpdate());
          }

//                    checkDuplicate.setRunSuccessDocument(Integer.parseInt(paymentProcessCount.toString()));
          List<PaymentProcess> success = paymentProcessList.stream().filter(object -> "S".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
          List<PaymentProcess> repair = paymentProcessList.stream().filter(object -> "N".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
          List<PaymentProcess> error = paymentProcessList.stream().filter(object -> "E".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
          checkDuplicate.setRunSuccessDocument(success.size());
          checkDuplicate.setRunRepairDocument(repair.size());
          checkDuplicate.setRunErrorDocument(error.size());
          checkDuplicate.setIdemCreatePaymentReply(Integer.parseInt(idemCreatePaymentReply.toString()));
          paymentAliasRepository.save(checkDuplicate);
        } else if (null != checkDuplicate.getRunStatus() && checkDuplicate.getRunStatus().equalsIgnoreCase("R")) {
          Long paymentProcessId = paymentProcessService.findOneByPaymentAliasIdForReverseDocument(checkDuplicate.getId());
          Long idemReversePaymentReply = paymentProcessService.countIdemReversePaymentReplyByPaymentAliasId(checkDuplicate.getId());
          if (checkDuplicate.getRunSuccessDocument() == Integer.parseInt(idemReversePaymentReply.toString())) {
            deletePayment(checkDuplicate);
            deleteProposal(checkDuplicate);
          } else {
            checkDuplicate.setRunReverseDocument(Integer.parseInt(paymentProcessId.toString()));
            checkDuplicate.setIdemReversePaymentReply(Integer.parseInt(idemReversePaymentReply.toString()));
            paymentAliasRepository.save(checkDuplicate);
          }
        }
        result.setStatus(HttpStatus.OK.value());
        result.setData(checkDuplicate);
        return new ResponseEntity<>(result, HttpStatus.OK);
      } else {
        result.setStatus(HttpStatus.NOT_FOUND.value());
        result.setData(null);
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @Override
  public ResponseEntity<Result<List<PaymentAliasResponse>>> findByCondition(String value) {
    Result<List<PaymentAliasResponse>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<PaymentAliasResponse> paymentAliasList = this.paymentAliasRepository.findByCondition(value);

      result.setStatus(HttpStatus.OK.value());
      result.setData(paymentAliasList);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<PaymentAlias>> deleteById(Long id) {
    Result<PaymentAlias> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      if (id > 0) {
        PaymentAlias paymentAlias = paymentAliasRepository.findById(id).orElse(null);

        log.info("deleteById {}", paymentAlias);
        paymentProcessService.deleteAllByPaymentAliasId(paymentAlias.getId(), true);
        glHeadService.resetGLHead(paymentAlias.getId());

        this.paymentAliasRepository.deleteById(id);

        result.setStatus(HttpStatus.OK.value());

        result.setData(paymentAlias);
      }
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void saveAll(List<PaymentAlias> PaymentAliass) {
    paymentAliasRepository.saveAll(PaymentAliass);
  }

  @Override
  public List<PaymentAlias> findAll() {
    return (List<PaymentAlias>) paymentAliasRepository.findAll();
  }

  @Override
  public PaymentAlias findOneById(Long id) {
    return paymentAliasRepository.findOneById(id);
  }

//    @Override
//    public void deletePaymentDocumentAll(Long paymentAliasId) {
//        try {
//            PaymentAlias paymentAlias = paymentAliasRepository.findById(paymentAliasId).orElse(null);
//
//
//            if (null != paymentAlias) {
//                paymentAlias.setRunStatus(null);
//                paymentAlias.setRunStart(null);
//                paymentAlias.setRunEnd(null);
//                paymentAlias.setRunTotalDocument(0);
//                paymentAlias.setRunSuccessDocument(0);
//                paymentProposalLogService.deletePaymentProposalLog(paymentAliasId, false);
//                paymentProcessService.deleteAllByPaymentAliasId(paymentAliasId, false);
//                paymentRealRunLogService.deletePaymentRealRunLog(paymentAliasId);
//                paymentAliasRepository.save(paymentAlias);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


  @Override
  public ResponseEntity<Result<List<String>>> validateParameter(Parameter parameter) {
    Result<List<String>> result = new Result<>();
    result.setTimestamp(new Date());
    List<String> listValidates = new ArrayList<>();
    try {
      String paymentMethod = parameter.getPaymentMethod();
      char[] paymentMethods = paymentMethod.toCharArray();
      List<CompanyCondition> compCodes = parameter.getCompanyCondition();
      List<VendorConfig> vendors = parameter.getVendor();
      for (char c : paymentMethods) {
        if (!Context.sessionPaymentMethod.containsKey(String.valueOf(c))) {
          listValidates.add("ไม่พบข้อมูลวิธีการชำระเงิน '" + String.valueOf(c) + "' ในระบบ");
        }
      }

      for (CompanyCondition companyCondition : compCodes) {
        if (null != companyCondition.getCompanyFrom() && !companyCondition.getCompanyFrom().equalsIgnoreCase("")) {
          if (!Context.sessionCompCode.containsKey(companyCondition.getCompanyFrom().trim())) {
            listValidates.add("ไม่พบข้อมูลบริษัท '" + companyCondition.getCompanyFrom() + "' ในระบบ");
          }
        }

        if (null != companyCondition.getCompanyTo() && !companyCondition.getCompanyTo().equalsIgnoreCase("")) {
          if (!Context.sessionCompCode.containsKey(companyCondition.getCompanyTo().trim())) {
            listValidates.add("ไม่พบข้อมูลบริษัท '" + companyCondition.getCompanyTo() + "' ในระบบ");
          }
        }
      }

//            for (VendorConfig vendor : vendors) {
//                if (null != vendor.getVendorTaxIdFrom() && !vendor.getVendorTaxIdFrom().equalsIgnoreCase("")) {
//                    if (!Context.sessionVendor.containsKey(vendor.getVendorTaxIdFrom())) {
//                        listValidates.add("ไม่พบข้อมูลผู้ขาย '" + vendor.getVendorTaxIdFrom() + "' ในระบบ");
//                    }
//                }
//
//                if (null != vendor.getVendorTaxIdTo() && !vendor.getVendorTaxIdTo().equalsIgnoreCase("")) {
//                    if (!Context.sessionVendor.containsKey(vendor.getVendorTaxIdTo())) {
//                        listValidates.add("ไม่พบข้อมูลผู้ขาย '" + vendor.getVendorTaxIdTo() + "' ในระบบ");
//                    }
//                }
//            }

      if (listValidates.isEmpty()) {
        result.setStatus(HttpStatus.OK.value());
        result.setData(listValidates);
        return new ResponseEntity<>(result, HttpStatus.OK);
      } else {
        result.setMessage("Validate Error");
        result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        result.setData(listValidates);
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      result.setMessage(e.getMessage());
      return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<PaymentAlias>> updateSchedule(PaymentAlias request, String type) {
    Result<PaymentAlias> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      PaymentAlias paymentAlias = paymentAliasRepository.findOneById(request.getId());

      if (null != paymentAlias) {
        if (type.equalsIgnoreCase("0")) {
          paymentAlias.setProposalJobStatus("W");
          request.getProposalScheduleDate().setSeconds(0);
          paymentAlias.setProposalScheduleDate(request.getProposalScheduleDate());
        } else {
          paymentAlias.setRunJobStatus("W");
          request.getRunScheduleDate().setSeconds(0);
          paymentAlias.setRunScheduleDate(request.getRunScheduleDate());
        }
        paymentAliasRepository.save(paymentAlias);
        result.setStatus(HttpStatus.OK.value());
        result.setData(paymentAlias);
      }
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<List<PaymentAliasResponse>>> findCreateJobByCondition(String value) {
    Result<List<PaymentAliasResponse>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<PaymentAliasResponse> paymentAliasList = this.paymentAliasRepository.findCreateJobByCondition(value);

      result.setStatus(HttpStatus.OK.value());
      result.setData(paymentAliasList);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<PaymentAliasResponse>> findCreateJobByCondition(Date paymentDate, String paymentName) {
    Result<PaymentAliasResponse> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<PaymentAliasResponse> paymentAliasList = this.paymentAliasRepository.findCreateJobByCondition(paymentDate, paymentName);
      if (null != paymentAliasList && !paymentAliasList.isEmpty()) {
        result.setStatus(HttpStatus.OK.value());
        result.setData(paymentAliasList.get(0));
        return new ResponseEntity<>(result, HttpStatus.OK);
      } else {
        result.setStatus(HttpStatus.NOT_FOUND.value());
        result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<List<PaymentAliasResponse>>> findSearchJobByCondition(String value) {
    Result<List<PaymentAliasResponse>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<PaymentAliasResponse> paymentAliasList = this.paymentAliasRepository.findSearchJobByCondition(value);

      result.setStatus(HttpStatus.OK.value());
      result.setData(paymentAliasList);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public PaymentAlias findOneByPaymentDateAndPaymentName(Timestamp paymentDate, String paymentName) {
    return paymentAliasRepository.findOneByPaymentDateAndPaymentName(paymentDate, paymentName);
  }

  private void deleteProposal(PaymentAlias paymentAlias) {
    paymentProposalLogService.deletePaymentProposalLog(paymentAlias.getId(), true);
    paymentProcessService.deleteAllByPaymentAliasId(paymentAlias.getId(), true);
    paymentRealRunLogService.deletePaymentRealRunLog(paymentAlias.getId());
    glHeadService.resetGLHead(paymentAlias.getId());
    customTriggersRepository.deleteById(paymentAlias.getProposalTriggersId());
    paymentAlias.setProposalScheduleDate(null);
    paymentAlias.setProposalStatus(null);
    paymentAlias.setProposalJobStatus(null);
    paymentAlias.setProposalScheduleDate(null);
    paymentAlias.setProposalStart(null);
    paymentAlias.setProposalEnd(null);
    paymentAlias.setProposalSuccessDocument(0);
    paymentAlias.setProposalTotalDocument(0);
    paymentAlias.setProposalTriggersId(null);
    paymentAlias.setProposalJobStatus(null);
    paymentAlias.setRunJobStatus(null);
    paymentAliasRepository.save(paymentAlias);
  }

  private void deletePayment(PaymentAlias paymentAlias) {
    paymentProposalLogService.deletePaymentProposalLog(paymentAlias.getId(), false);
    paymentProcessService.deleteAllByPaymentAliasId(paymentAlias.getId(), false);
    paymentRealRunLogService.deletePaymentRealRunLog(paymentAlias.getId());
    paymentAlias.setRunStatus(null);
    paymentAlias.setRunStart(null);
    paymentAlias.setRunEnd(null);
    paymentAlias.setRunTotalDocument(0);
    paymentAlias.setRunSuccessDocument(0);
    paymentAlias.setIdemReversePaymentReply(0);
    paymentAlias.setPaymentTriggersId(null);
    paymentAlias.setRunStart(null);
    paymentAlias.setRunEnd(null);
    paymentAlias.setIdemEnd(null);
    paymentAliasRepository.save(paymentAlias);
  }
}

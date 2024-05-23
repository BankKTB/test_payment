package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.PeriodControl;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.ReversePaymentReport;
import th.com.bloomcode.paymentservice.model.request.MassChangeDocumentRequest;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;
import th.com.bloomcode.paymentservice.model.request.ReversePaymentDocumentAllRequest;
import th.com.bloomcode.paymentservice.repository.payment.ReverseDocumentRepository;
import th.com.bloomcode.paymentservice.service.idem.PeriodControlService;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.TimestampUtil;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.APUpPbkToAllRequest;
import th.com.bloomcode.paymentservice.webservice.model.request.FIPostRequest;

import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class PaymentReverseServiceImpl implements PaymentReverseService {


  private final JmsTemplate jmsTemplate;

  private final GLHeadService glHeadService;

  private final PaymentReportService paymentReportService;

  private final PeriodControlService periodControlService;

  private final PaymentAliasService paymentAliasService;

  private final ReverseDocumentService reverseDocumentService;

  private final PaymentProcessService paymentProcessService;

  private final MassChangeDocumentService massChangeDocumentService;
  private final JdbcTemplate paymentJdbcTemplate;


  public PaymentReverseServiceImpl(JmsTemplate jmsTemplate, GLHeadService glHeadService, PaymentReportService paymentReportService, PeriodControlService periodControlService, PaymentAliasService paymentAliasService, ReverseDocumentService reverseDocumentService, ReverseDocumentRepository reverseDocumentRepository, PaymentProcessService paymentProcessService, MassChangeDocumentService massChangeDocumentService,
                                   @Qualifier("paymentJdbcTemplate") JdbcTemplate paymentJdbcTemplate) {
    this.jmsTemplate = jmsTemplate;
    this.glHeadService = glHeadService;
    this.paymentReportService = paymentReportService;
    this.periodControlService = periodControlService;
    this.paymentAliasService = paymentAliasService;
    this.reverseDocumentService = reverseDocumentService;

    this.paymentProcessService = paymentProcessService;
    this.massChangeDocumentService = massChangeDocumentService;
    this.paymentJdbcTemplate = paymentJdbcTemplate;
  }

  @Override
  public ResponseEntity<Result<List<MassChangeDocumentRequest>>> massChangeDocument(List<MassChangeDocumentRequest> request) {
    Result<List<MassChangeDocumentRequest>> result = new Result<>();
    result.setTimestamp(new Date());
    UUID uuid = UUID.randomUUID();
    try {
      List<MassChangeDocument> massChangeDocuments = new ArrayList<>();
      List<MassChangeDocument> massChangeDocumentsUpdate = new ArrayList<>();
      List<FIMessage> fiMessages = new ArrayList<>();
      for (MassChangeDocumentRequest item : request) {

        GLHead glHead = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(item.getCompCode(), item.getAccountDocNo(), item.getFiscalYear());

        String checkTypeTransaction = null;
        if (glHead == null) {
          checkTypeTransaction = "NOT_IN_SYSTEM";
        } else if (glHead.getPaymentId() == null || glHead.getPaymentId() == 0) {
          checkTypeTransaction = "NOT_IN_PROPOSAL";
        } else {
          checkTypeTransaction = "IN_PROPOSAL";
        }

        MassChangeDocument checkDuplicate = massChangeDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(item.getCompCode(), item.getAccountDocNo(), item.getFiscalYear());

        if (null == checkDuplicate) {
          MassChangeDocument massChangeDocument = new MassChangeDocument();
          massChangeDocument.setGroupDoc(uuid.toString());
          massChangeDocument.setCompanyCode(item.getCompCode());
          massChangeDocument.setDocumentNo(item.getAccountDocNo());
          massChangeDocument.setFiscalYear(item.getFiscalYear());
          massChangeDocument.setPaymentBlock(item.getPaymentBlock());
          massChangeDocument.setUserPost(item.getWebInfo().getUserWebOnline());
          if (checkTypeTransaction.equalsIgnoreCase("NOT_IN_SYSTEM") || checkTypeTransaction.equalsIgnoreCase("NOT_IN_PROPOSAL")) {
            massChangeDocument.setStatus("P");
          } else {
            PaymentAlias paymentAlias = paymentAliasService.findOneById(glHead.getPaymentId());
            massChangeDocument.setStatus("ไม่สามารถเปลี่ยนแปลงรายการเนื่องจากอยู่ใน " + paymentAlias.getPaymentName() + " " + TimestampUtil.dateThai(TimestampUtil.timestampToString(paymentAlias.getPaymentDate())));
          }

          massChangeDocument.setCreatedBy(item.getWebInfo().getUserWebOnline());
          massChangeDocument.setCreated(new Timestamp(System.currentTimeMillis()));
          massChangeDocument.setUpdatedBy(item.getWebInfo().getUserWebOnline());
          massChangeDocument.setUpdated(new Timestamp(System.currentTimeMillis()));
          massChangeDocuments.add(massChangeDocument);
//                    massChangeDocumentService.save(massChangeDocument);
        } else {
          checkDuplicate.setGroupDoc(uuid.toString());
          if (checkTypeTransaction.equalsIgnoreCase("NOT_IN_SYSTEM") || checkTypeTransaction.equalsIgnoreCase("NOT_IN_PROPOSAL")) {
            checkDuplicate.setStatus("P");
          } else {
            PaymentAlias paymentAlias = paymentAliasService.findOneById(glHead.getPaymentId());
            checkDuplicate.setStatus("ไม่สามารถเปลี่ยนแปลงรายการเนื่องจากอยู่ใน " + paymentAlias.getPaymentName() + " " + TimestampUtil.dateThai(TimestampUtil.timestampToString(paymentAlias.getPaymentDate())));
          }
          checkDuplicate.setPaymentBlock(item.getPaymentBlock());
          checkDuplicate.setUpdatedBy(item.getWebInfo().getUserWebOnline());
          checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
          massChangeDocumentsUpdate.add(checkDuplicate);
//                    massChangeDocumentService.save(checkDuplicate);
        }

        if (checkTypeTransaction.equalsIgnoreCase("NOT_IN_SYSTEM") || checkTypeTransaction.equalsIgnoreCase("NOT_IN_PROPOSAL")) {

          APUpPbkToAllRequest aPUpPbkToAllRequest = new APUpPbkToAllRequest();
          aPUpPbkToAllRequest.setFlag(1);
          aPUpPbkToAllRequest.setAccountDocNo(item.getAccountDocNo());
          aPUpPbkToAllRequest.setCompCode(item.getCompCode());
          aPUpPbkToAllRequest.setFiscalYear(item.getFiscalYear());
          aPUpPbkToAllRequest.setPaymentBlock(item.getPaymentBlock());
          aPUpPbkToAllRequest.setCreated(new Timestamp(System.currentTimeMillis()));
          aPUpPbkToAllRequest.setWebInfo(item.getWebInfo());

          if (item.getCompCode().startsWith("99999")) {
            FIMessage fiMessage = new FIMessage();
            fiMessage.setId(item.getCompCode() + "." + item.getFiscalYear() + "." + item.getAccountDocNo());
            fiMessage.setType(FIMessage.Type.REQUEST.getCode());
            fiMessage.setDataType(FIMessage.DataType.PBK_ALL.getCode());
            fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
            fiMessage.setTo("99999");
            log.info("APUpPbkToAllRequest {}", item);
            fiMessage.setData(XMLUtil.xmlMarshall(APUpPbkToAllRequest.class, aPUpPbkToAllRequest));
            fiMessages.add(fiMessage);
//                        log.info("fiMessage {}", fiMessage);
//                        this.sendChangeDocument(fiMessage, item.getCompCode());
          } else {
            FIMessage fiMessage = new FIMessage();
            fiMessage.setId(item.getCompCode() + "." + item.getFiscalYear() + "." + item.getAccountDocNo());
            fiMessage.setType(FIMessage.Type.REQUEST.getCode());
            fiMessage.setDataType(FIMessage.DataType.PBK_ALL.getCode());
            fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
            fiMessage.setTo(item.getCompCode().substring(0, 2));
            log.info("APUpPbkToAllRequest {}", item);
            fiMessage.setData(XMLUtil.xmlMarshall(APUpPbkToAllRequest.class, aPUpPbkToAllRequest));
            fiMessages.add(fiMessage);
//                        log.info("fiMessage {}", fiMessage);
//                        this.sendChangeDocument(fiMessage, item.getCompCode());
          }
        }
      }

      massChangeDocumentService.saveBatch(massChangeDocuments);
      massChangeDocumentService.updateBatch(massChangeDocumentsUpdate);

      for (FIMessage fiMessage : fiMessages) {
        this.sendChangeDocument(fiMessage, fiMessage.getTo());
      }

      result.setData(request);
      result.setMessage(uuid.toString());
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @Override
  public ResponseEntity<Result<List<ReverseDocumentRequest>>> massReverseDocument(List<ReverseDocumentRequest> request) {
    Result<List<ReverseDocumentRequest>> result = new Result<>();
    result.setTimestamp(new Date());
    UUID uuid = UUID.randomUUID();
    try {

      for (ReverseDocumentRequest item : request) {
        ReverseDocument checkDuplicate = reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(item.getCompCode(), item.getAccountDocNo(), item.getFiscalYear());
        log.info(" item {}", item);
        if (null == checkDuplicate) {
          ReverseDocument reverseDocument = new ReverseDocument();
          reverseDocument.setCompanyCode(item.getCompCode());
          reverseDocument.setDocumentNo(item.getAccountDocNo());
          reverseDocument.setFiscalYear(item.getFiscalYear());
          reverseDocument.setCompanyCodeAgency(item.getCompCodeAgency());
          reverseDocument.setDocumentNoAgency(item.getAccountDocNoAgency());
          reverseDocument.setFiscalYearAgency(item.getFiscalYearAgency());
          reverseDocument.setUserPost(item.getWebInfo().getUserWebOnline());
          reverseDocument.setStatus("P");
          reverseDocument.setCreatedBy(item.getWebInfo().getUserWebOnline());
          reverseDocument.setGroupDoc(uuid.toString());
          reverseDocumentService.save(reverseDocument);
        } else {
          checkDuplicate.setStatus("P");
          checkDuplicate.setUpdatedBy(item.getWebInfo().getUserWebOnline());
          checkDuplicate.setGroupDoc(uuid.toString());
          reverseDocumentService.save(checkDuplicate);
        }

        FIPostRequest fiPostRequest = new FIPostRequest();
        BeanUtils.copyProperties(item, fiPostRequest);
        if (item.getCompCode().startsWith("99999")) {
          FIMessage fiMessage = new FIMessage();
          fiMessage.setId(item.getCompCode() + "." + item.getFiscalYear() + "." + item.getAccountDocNo());
          fiMessage.setType(FIMessage.Type.REQUEST.getCode());
          fiMessage.setDataType(FIMessage.DataType.REVERSE.getCode());
          fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
          fiMessage.setTo("99999");
          log.info("FIPostRequest {}", item);
          fiPostRequest.setFlag(1);
          fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
          log.info("fiMessage {}", fiMessage);
          this.send(fiMessage);
        } else {
          FIMessage fiMessage = new FIMessage();
          fiMessage.setId(item.getCompCode() + "." + item.getFiscalYear() + "." + item.getAccountDocNo());
          fiMessage.setType(FIMessage.Type.REQUEST.getCode());
          fiMessage.setDataType(FIMessage.DataType.REVERSE_INVOICE.getCode());
          fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
          fiMessage.setTo(item.getCompCode().substring(0, 2));
          log.info("FIPostRequest {}", item);
          fiPostRequest.setFlag(1);
          fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
          log.info("fiMessage {}", fiMessage);
          this.sendReverse(fiMessage, item.getCompCode());
        }
      }
      result.setMessage(uuid.toString());
      result.setData(request);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @Override
  public ResponseEntity<Result<List<ReverseDocument>>> pullMassReverseDocument(List<ReverseDocumentRequest> request) {
    Result<List<ReverseDocument>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<ReverseDocument> listDocument = reverseDocumentService.findByListDocument(request);

      Long total = (long) listDocument.size();
      Long success = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("S")).count();
      Long fail = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("E")).count();
      Long process = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("P")).count();

      for (ReverseDocument reverseDocument : listDocument) {
        reverseDocument.setTotal(total);
        reverseDocument.setSuccess(success);
        reverseDocument.setFail(fail);
        reverseDocument.setProcess(process);
      }

      result.setData(listDocument);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<List<ReverseDocument>>> pullMassReverseDocument(String uuid) {
    Result<List<ReverseDocument>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<ReverseDocument> listDocument = reverseDocumentService.findByListDocument(uuid);

      Long total = (long) listDocument.size();
      Long success = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("S")).count();
      Long fail = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("E")).count();
      Long process = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("P")).count();

      for (ReverseDocument reverseDocument : listDocument) {
        reverseDocument.setTotal(total);
        reverseDocument.setSuccess(success);
        reverseDocument.setFail(fail);
        reverseDocument.setProcess(process);
      }

      result.setData(listDocument);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<List<MassChangeDocument>>> pullMassChangeDocument(List<MassChangeDocumentRequest> request) {
    Result<List<MassChangeDocument>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<MassChangeDocument> listDocument = massChangeDocumentService.findByListDocument(request);
      Long total = (long) listDocument.size();
      Long success = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("S")).count();
      Long fail = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("E")).count();
      Long process = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("P")).count();

      for (MassChangeDocument massChangeDocument : listDocument) {
        massChangeDocument.setTotal(total);
        massChangeDocument.setSuccess(success);
        massChangeDocument.setFail(fail);
        massChangeDocument.setProcess(process);
      }


      result.setData(listDocument);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<List<MassChangeDocument>>> pullMassChangeDocument(String uuid) {
    Result<List<MassChangeDocument>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
//            List<MassChangeDocument> listDocument = massChangeDocumentService.findByListDocument(request);
      List<MassChangeDocument> listDocument = massChangeDocumentService.findByListDocument(uuid);
      Long total = (long) listDocument.size();
      Long success = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("S")).count();
      Long fail = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("E")).count();
      Long process = listDocument.stream().filter(u -> u.getStatus().equalsIgnoreCase("P")).count();

      for (MassChangeDocument massChangeDocument : listDocument) {
        massChangeDocument.setTotal(total);
        massChangeDocument.setSuccess(success);
        massChangeDocument.setFail(fail);
        massChangeDocument.setProcess(process);
      }


      result.setData(listDocument);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<ReverseDocumentRequest>> reversePaymentDocument(ReverseDocumentRequest request) {
    Result<ReverseDocumentRequest> result = new Result<>();
    result.setTimestamp(new Date());
    log.info("FIPostRequest {}", request);
    try {

      List<PeriodControl> periodControlsByCompCode = periodControlService.findAllByCondition(request.getPeriod(), request.getFiscalYear(), request.getCompCode());

      List<PeriodControl> periodControlsCentral = periodControlService.findAllByCondition(request.getPeriod(), request.getFiscalYear(), "0");


      boolean checkPeriod = false;
      if (periodControlsByCompCode.size() == 2) {
        checkPeriod = true;
      } else if (periodControlsByCompCode.size() == 0) {
        if (periodControlsCentral.size() == 2) {
          checkPeriod = true;
        } else {
          checkPeriod = false;
        }
      } else {
        checkPeriod = false;
      }

      if (checkPeriod) {
        ReverseDocument checkDuplicate = reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(request.getCompCode(), request.getAccountDocNo(), request.getFiscalYear());
        if (null == checkDuplicate) {
          ReverseDocument reverseDocument = new ReverseDocument();
          reverseDocument.setCompanyCode(request.getCompCode());
          reverseDocument.setDocumentNo(request.getAccountDocNo());
          reverseDocument.setFiscalYear(request.getFiscalYear());
          reverseDocument.setUserPost(request.getWebInfo().getUserWebOnline());
          reverseDocument.setStatus("P");
          reverseDocument.setCreatedBy(request.getWebInfo().getUserWebOnline());
          reverseDocumentService.save(reverseDocument);
        } else {
          checkDuplicate.setStatus("P");
          checkDuplicate.setUpdatedBy(request.getWebInfo().getUserWebOnline());
          reverseDocumentService.save(checkDuplicate);
        }

        FIPostRequest fiPostRequest = new FIPostRequest();
        BeanUtils.copyProperties(request, fiPostRequest);

        FIMessage fiMessage = new FIMessage();
        fiMessage.setId(request.getCompCode() + "." + request.getFiscalYear() + "." + request.getAccountDocNo());
        fiMessage.setType(FIMessage.Type.REQUEST.getCode());
        fiMessage.setDataType(FIMessage.DataType.REVERSE.getCode());
        fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
        fiMessage.setTo("99999");
        log.info("FIPostRequest {}", request);
        fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));

        log.info("fiMessage {}", fiMessage);
        this.send(fiMessage);
        result.setData(request);

        result.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
      } else {
        result.setMessage("กรุณาเปิดงวด ");
        result.setData(request);
        result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<ReverseDocumentRequest>> reverseInvoiceDocument(ReverseDocumentRequest request) {
    Result<ReverseDocumentRequest> result = new Result<>();
    result.setTimestamp(new Date());
    try {

      List<PeriodControl> periodControlsByCompCode = periodControlService.findAllByCondition(request.getPeriod(), request.getFiscalYear(), request.getCompCode());

      List<PeriodControl> periodControlsCentral = periodControlService.findAllByCondition(request.getPeriod(), request.getFiscalYear(), "0");

      boolean checkPeriod = false;
      if (periodControlsByCompCode.size() == 2) {
        checkPeriod = true;
      } else if (periodControlsByCompCode.size() == 0) {
        if (periodControlsCentral.size() == 2) {
          checkPeriod = true;
        } else {
          checkPeriod = false;
        }
      } else {
        checkPeriod = false;
      }

      if (checkPeriod) {
        ReverseDocument checkDuplicate = reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(request.getCompCode(), request.getAccountDocNo(), request.getFiscalYear());
        if (null == checkDuplicate) {
          ReverseDocument reverseDocument = new ReverseDocument();
          reverseDocument.setCompanyCode(request.getCompCode());
          reverseDocument.setDocumentNo(request.getAccountDocNo());
          reverseDocument.setFiscalYear(request.getFiscalYear());
          reverseDocument.setUserPost(request.getWebInfo().getUserWebOnline());
          reverseDocument.setStatus("P");
          reverseDocument.setCreatedBy(request.getWebInfo().getUserWebOnline());
          reverseDocumentService.save(reverseDocument);
        } else {
          checkDuplicate.setStatus("P");
          checkDuplicate.setUpdatedBy(request.getWebInfo().getUserWebOnline());
          reverseDocumentService.save(checkDuplicate);
        }

        GLHead glHead = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(request.getCompCode(), request.getAccountDocNo(), request.getFiscalYear());
        log.info(" glHead {}", glHead);

        if (null != glHead && 0 == glHead.getPaymentId() && null == glHead.getPaymentDocumentNo()) {
          log.info(" getPaymentId {}", glHead.getPaymentId());
          log.info(" getPaymentDocumentNo {}", glHead.getPaymentDocumentNo());
          FIPostRequest fiPostRequest = new FIPostRequest();
          BeanUtils.copyProperties(request, fiPostRequest);

          FIMessage fiMessage = new FIMessage();
          fiMessage.setId(request.getCompCode() + "." + request.getFiscalYear() + "." + request.getAccountDocNo());
          fiMessage.setType(FIMessage.Type.REQUEST.getCode());
          fiMessage.setDataType(FIMessage.DataType.REVERSE_INVOICE.getCode());
          fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
          fiMessage.setTo(request.getCompCode().substring(0, 2));
          log.info("FIPostRequest {}", request);
          fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));

          log.info("fiMessage {}", fiMessage);
          this.sendReverse(fiMessage, request.getCompCode());
          result.setData(request);
          result.setStatus(HttpStatus.OK.value());
          return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
          result.setMessage("ไม่สามารถกลับรายการได้ เพราะ เลขที่เอกสารขอเบิก" + request.getAccountDocNo() + " " + request.
              getCompCode() + " " + request.getFiscalYear() + " มีการสร้างข้อเสนอค้างไว้");
          result.setData(request);
          result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
          return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }
      } else {
        result.setMessage("กรุณาเปิดงวด ");
        result.setData(request);
        result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
      }

    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<List<PaymentReport>>> reversePaymentAll(HttpServletRequest
                                                                           httpServletRequest, ReversePaymentDocumentAllRequest reversePaymentDocumentAllRequest) {
    Result<List<PaymentReport>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<PaymentReport> paymentReports = paymentReportService.findDocumentPayment(reversePaymentDocumentAllRequest.getPaymentAliasId());
      paymentProcessService.updateReversePaymentDocument(reversePaymentDocumentAllRequest.getPaymentAliasId());

//            Map<String, List<PaymentReport>> groupByPaymentCompanyCode = paymentReports.stream()
//                    .collect(Collectors.groupingBy(PaymentReport::getPaymentCompanyCode,
//                            Collectors.mapping((PaymentReport p) -> p, toList())));
//            List<PeriodControl> periodControls = new ArrayList<>();
//
//
//            for (Map.Entry<String, List<PaymentReport>> entry : groupByPaymentCompanyCode.entrySet()) {
//                if (!entry.getKey().equalsIgnoreCase("99999")) {
//                    periodControls = periodControlService.findAllByCondition(reversePaymentDocumentAllRequest.getReverseDate(), entry.getKey());
//                    if (periodControls.size() != 0 && periodControls.size() != 2) {
//                        break;
//                    }
//                }
//            }
//            log.info("  periodControls {} ", periodControls.size());

//            List<PeriodControl> periodControls = periodControlService.findAllByCondition(reversePaymentDocumentAllRequest.getReverseDate(), "0");
//
//
//            log.info(" periodControls {} ", periodControls);
//            log.info(" periodControls {} ", periodControls.size());


      if (null != paymentReports && !paymentReports.isEmpty()) {
        log.info(" periodControls >0 {} ", " ");
        List<ReverseDocument> reverseDocuments = new ArrayList<>();
        for (PaymentReport paymentReport : paymentReports) {
          if (null != paymentReport.getPaymentDocumentNo()) {
            log.info(" paymentReport.getPaymentDocumentNo()  {} ", paymentReport.getPaymentDocumentNo());
            ReverseDocument checkDuplicate = reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear());
            log.info(" paymentReport checkDuplicate  {} ", checkDuplicate);
            if (null == checkDuplicate) {
              ReverseDocument reverseDocument = new ReverseDocument();
              reverseDocument.setCompanyCode(paymentReport.getOriginalCompanyCode());
              reverseDocument.setDocumentNo(paymentReport.getOriginalDocumentNo());
              reverseDocument.setFiscalYear(paymentReport.getOriginalFiscalYear());
              reverseDocument.setUserPost(reversePaymentDocumentAllRequest.getWebInfo().getUserWebOnline());
              reverseDocument.setStatus("P");
              reverseDocument.setCreatedBy(reversePaymentDocumentAllRequest.getWebInfo().getUserWebOnline());
              reverseDocuments.add(reverseDocument);
//                                reverseDocumentService.save(reverseDocument);
            } else {
              checkDuplicate.setStatus("P");
              checkDuplicate.setUpdatedBy(reversePaymentDocumentAllRequest.getWebInfo().getUserWebOnline());
              reverseDocuments.add(checkDuplicate);
//                                reverseDocumentService.save(checkDuplicate);
            }
          } else {
            this.glHeadService.updateGLHead(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), null, null);
          }
        }

        long reverseDocumentSize = reverseDocuments.size();
        long reverseDocumentId = SqlUtil.getNextSeries(
            paymentJdbcTemplate, ReverseDocument.TABLE_NAME + ReverseDocument.SEQ, reverseDocumentSize);
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, reverseDocumentId + reverseDocumentSize, ReverseDocument.TABLE_NAME + ReverseDocument.SEQ);

        for (ReverseDocument reverseDocument : reverseDocuments) {
          reverseDocument.setId(reverseDocumentId++);
        }
        reverseDocumentService.saveBatch(reverseDocuments);

        for (PaymentReport paymentReport : paymentReports) {
          FIPostRequest fiPostRequest = new FIPostRequest(paymentReport);
          fiPostRequest.setWebInfo(reversePaymentDocumentAllRequest.getWebInfo());
          fiPostRequest.setReasonNo(reversePaymentDocumentAllRequest.getReasonReverse());
          fiPostRequest.setRevDateAcct(reversePaymentDocumentAllRequest.getReverseDate());
          fiPostRequest.setFlag(1);

          FIMessage fiMessage = new FIMessage();
          fiMessage.setId(paymentReport.getPaymentCompanyCode() + "." + paymentReport.getPaymentFiscalYear() + "." + paymentReport.getPaymentDocumentNo());
          fiMessage.setType(FIMessage.Type.REQUEST.getCode());
          fiMessage.setDataType(FIMessage.DataType.REVERSE.getCode());
          fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
          fiMessage.setTo("99999");
          log.info("FIPostRequest REV ALL {}", fiPostRequest);
          fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
          this.send(fiMessage);
          log.info("fiMessage REV ALL {}", fiMessage);
        }
      }
      PaymentAlias paymentAlias = paymentAliasService.findOneById(reversePaymentDocumentAllRequest.getPaymentAliasId());
      paymentAlias.setRunStatus("R");
      paymentAlias.setRunJobStatus(null);
      paymentAlias.setIdemCreatePaymentReply(0);
      paymentAliasService.save(paymentAlias);
      result.setData(paymentReports);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<ReversePaymentReport>> pullReversePaymentDocument(HttpServletRequest
                                                                                    httpServletRequest, Long paymentAliasId, WSWebInfo webInfo) {
    Result<ReversePaymentReport> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<PaymentReport> paymentReports = paymentReportService.findDocumentPayment(paymentAliasId);

//            log.info(" paymentReports {} ", paymentReports);

      List<PaymentReport> paymentSuccess = paymentReports.stream().filter(item -> null != item.getPaymentDocumentNo() && !item.getPaymentDocumentNo().equalsIgnoreCase("")).collect(Collectors.mapping((PaymentReport p) -> p, toList()));
      Long total = (long) paymentSuccess.size();
      Long success = paymentSuccess.stream().filter(u -> u.getReversePaymentStatus().equalsIgnoreCase("S")).count();
      Long fail = paymentSuccess.stream().filter(u -> u.getReversePaymentStatus().equalsIgnoreCase("E")).count();
      Long process = paymentSuccess.stream().filter(u -> u.getReversePaymentStatus().equalsIgnoreCase("W")).count();
      ReversePaymentReport reversePaymentReport = new ReversePaymentReport();
      reversePaymentReport.setData(paymentSuccess);
      reversePaymentReport.setTotal(total);
      reversePaymentReport.setSuccess(success);
      reversePaymentReport.setFail(fail);
      reversePaymentReport.setProcess(process);
//            log.info(" paymentSuccess {} ", paymentSuccess);
      result.setData(reversePaymentReport);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @Override
  public void sendReverse(FIMessage message, String compCode) throws Exception {
    log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
    log.info("client : {}", compCode.substring(0, 2) + ".AP.Payment");
    final AtomicReference<Message> msg = new AtomicReference<>();
    jmsTemplate.convertAndSend(compCode.substring(0, 2) + ".AP.Payment",
        XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
          msg.set(message1);
          return message1;
        });
    log.info("msg id : {}", msg.get().getJMSMessageID());
  }

  public void send(FIMessage message) throws Exception {
    log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
    final AtomicReference<Message> msg = new AtomicReference<>();
    jmsTemplate.convertAndSend("99999.AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
      msg.set(message1);
      return message1;
    });
    log.info("msg id : {}", msg.get().getJMSMessageID());
  }

  public void sendChangeDocument(FIMessage message, String compCode) throws Exception {
    log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
    String agency = null != compCode && compCode.length() > 2 && !("99999").equalsIgnoreCase(compCode) ? compCode.substring(0, 2) : compCode;
    log.info("client : {}", compCode.substring(0, 2) + ".AP.UpPbk");

    final AtomicReference<Message> msg = new AtomicReference<>();
    jmsTemplate.convertAndSend(agency + ".AP.UpPbk",
        XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
          msg.set(message1);
          return message1;
        });
    log.info("msg id : {}", msg.get().getJMSMessageID());
  }
}

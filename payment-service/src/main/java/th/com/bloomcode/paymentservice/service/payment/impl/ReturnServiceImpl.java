package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.CompCodeRepository;
import th.com.bloomcode.paymentservice.model.*;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.response.ReturnLogResultResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnProposalLogResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnReverseInvoiceResponse;
import th.com.bloomcode.paymentservice.service.FileTransferService;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.APStatusPaidRequest;
import th.com.bloomcode.paymentservice.webservice.model.request.FIPostRequest;

import javax.jms.Message;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class ReturnServiceImpl implements ReturnService {
  private final CompCodeRepository compCodeRepository;
  private final ProposalLogService proposalLogService;
  private final JmsTemplate jmsTemplate;
  private final GLHeadServiceImpl glHeadService;
  private final GLLineService glLineService;
  private final FileTransferService fileTransferService;
  private static final String CAN_UPDATE = "CAN_UPDATE";
  private static final String CANNOT_UPDATE = "CANNOT_UPDATE";
  private final ReverseDocumentService reverseDocumentService;
  private final ProposalReturnLogService proposalLogReturnService;
  private final ReturnReverseDocumentService returnReverseDocumentService;
  private final PrepareRunDocumentService prepareRunDocumentService;
  private final PaymentAliasService paymentAliasService;
  private final JdbcTemplate paymentJdbcTemplate;

  public ReturnServiceImpl(
      CompCodeRepository compCodeRepository,
      ProposalLogService proposalLogService,
      JmsTemplate jmsTemplate,
      GLHeadServiceImpl glHeadService, GLLineService glLineService,
      FileTransferService fileTransferService,
      ReverseDocumentService reverseDocumentService,
      ProposalReturnLogService proposalLogReturnService, ReturnReverseDocumentService returnReverseDocumentService, PrepareRunDocumentService prepareRunDocumentService, PaymentAliasService paymentAliasService, @Qualifier("paymentJdbcTemplate") JdbcTemplate paymentJdbcTemplate) {
    this.compCodeRepository = compCodeRepository;
    this.proposalLogService = proposalLogService;
    this.jmsTemplate = jmsTemplate;
    this.glHeadService = glHeadService;
    this.glLineService = glLineService;
    this.fileTransferService = fileTransferService;
    this.reverseDocumentService = reverseDocumentService;
    this.proposalLogReturnService = proposalLogReturnService;
    this.returnReverseDocumentService = returnReverseDocumentService;
    this.prepareRunDocumentService = prepareRunDocumentService;
    this.paymentAliasService = paymentAliasService;
    this.paymentJdbcTemplate = paymentJdbcTemplate;
  }

  @Override
  public ResponseEntity<Result<List<ReturnProposalLogResponse>>> getPropLogReturn(
      ReturnSearchProposalLog request) {
    Result<List<ReturnProposalLogResponse>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<ReturnProposalLogResponse> returnProposalDocumentResponse =
          proposalLogService.findAllProposalLogReturn(request);
      result.setData(returnProposalDocumentResponse);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<ReturnLogResultResponse>> getFileReturn(ReturnGetFile request) {
    log.info("getFileReturn BEGIN");
    Result<ReturnLogResultResponse> result = new Result<>();
    try {
      ReturnLogResultResponse listProposal = this.fileTransferService.downloadFile();
      result.setData(listProposal);
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
  public Result<List<ReturnLogDetail>> changeFileStatusReturn(List<ChangeFileStatusReturnRequest> request) {
    JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
    WSWebInfo wsWebInfo = new WSWebInfo();
    wsWebInfo.setIpAddress("127.0.0.1");
    wsWebInfo.setUserWebOnline(jwt.getSN());
    wsWebInfo.setFiArea("1000");
    wsWebInfo.setCompCode("99999");
    wsWebInfo.setPaymentCenter("9999999999");
    Timestamp created = new Timestamp(System.currentTimeMillis());
    Result<List<ReturnLogDetail>> result = new Result<>();
    List<ReturnLogDetail> resultList = new ArrayList<>();
    List<ProposalLog> proposalLogsDelete = new ArrayList<>();
    List<ProposalReturnLog> proposalReturnLogsInsert = new ArrayList<>();
    List<ProposalReturnLog> proposalReturnLogsUpdate = new ArrayList<>();
    List<ProposalLog> proposalLogsUpdate = new ArrayList<>();
    try {
      for (ChangeFileStatusReturnRequest changeFileStatusReturnRequest : request) {
        if (null != changeFileStatusReturnRequest.getFileStatus()) {
          log.info(
              " changeFileStatusReturnRequest id : {} ", changeFileStatusReturnRequest.getId());
          ProposalLog proposalLog =
              proposalLogService.findOneById(changeFileStatusReturnRequest.getId());
          log.info(" proposalLog data : {} ", proposalLog);
          if (null != proposalLog) {
            log.info(" proposalLog : {} ", proposalLog.getId());
            ProposalLog proposalLogLevel1 =
                proposalLogService.findOneByRefRunningAndFileTypeAndTransferLevel(
                    proposalLog.getRefRunning(), "SWIFT", "1");
            if (null != proposalLogLevel1) {
              log.info(" proposalLogLevel1 : {} ", proposalLogLevel1.getId());
              String logType;
              if ("C".equalsIgnoreCase(changeFileStatusReturnRequest.getFileStatus())) {
                if (this.checkUpdateStatusComplete(proposalLog)) {
                  proposalLog.setFileStatus("C");
                  proposalLogLevel1.setFileStatus("C");
                  proposalLog.setReturnBy(jwt.getSub());
                  proposalLog.setReturnDate(created);
                  proposalLogLevel1.setReturnBy(jwt.getSub());
                  proposalLogLevel1.setReturnDate(created);
                  logType = CAN_UPDATE;
                } else {
                  logType = CANNOT_UPDATE;
                }
                proposalLogsDelete.add(proposalLog);
//               proposalLogReturnService.deleteProposalReturnByStep2Complete(proposalLog);
              } else {
                if (this.checkUpdateStatusReturn(proposalLog)) {
                  proposalLog.setFileStatus("R");
                  proposalLogLevel1.setFileStatus("R");
                  proposalLog.setReturnBy(jwt.getSub());
                  proposalLog.setReturnDate(created);
                  proposalLogLevel1.setReturnBy(jwt.getSub());
                  proposalLogLevel1.setReturnDate(created);

                  if ("9".equalsIgnoreCase(proposalLog.getTransferLevel()) && !"0000000000".equalsIgnoreCase(proposalLog.getVendor())) {
                    ProposalReturnLog checkDuplicate =
                        proposalLogReturnService.findOneByInvoiceDocumentAndPaymentDocument(
                            proposalLog);

                    if (checkDuplicate == null) {
                      ProposalReturnLog proposalReturnLog = new ProposalReturnLog();
//                      proposalReturnLog.setId(proposalReturnId++);
                      proposalReturnLog.setInvoiceCompCode(proposalLog.getInvCompCode());
                      proposalReturnLog.setInvoiceDocNo(proposalLog.getInvDocNo());
                      //
                      // proposalReturnLog.setInvoiceLine(proposalLog.getInvoiceLine());
                      proposalReturnLog.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                      proposalReturnLog.setPaymentCompCode(proposalLog.getPaymentCompCode());
                      proposalReturnLog.setPaymentDocNo(proposalLog.getPaymentDocument());
                      proposalReturnLog.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                      proposalReturnLog.setPaymentDate(proposalLog.getPaymentDate());
                      proposalReturnLog.setPaymentName(proposalLog.getPaymentName());
                      proposalReturnLog.setVendor(proposalLog.getVendor());
                      //
                      // proposalReturnLog.setPaymentMethod(proposalLog.getPaymentMethod);
                      proposalReturnLog.setBankKey(proposalLog.getBankKey());
//                    proposalReturnLog.setReason(proposalLog.getRevOriginalReason());
                      proposalReturnLog.setFileName("Manual Flag");
                      proposalReturnLog.setTransferDate(proposalLog.getTransferDate());

                      proposalReturnLog.setOriginalCompCode(proposalLog.getOriginalCompCode());
                      proposalReturnLog.setOriginalDocumentNo(proposalLog.getOriginalDocNo());
                      proposalReturnLog.setOriginalFiscalYear(proposalLog.getOriginalFiscalYear());
                      proposalReturnLog.setCreated(new Timestamp(System.currentTimeMillis()));
                      proposalReturnLog.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                      proposalReturnLogsInsert.add(proposalReturnLog);

                      if (proposalLog.getCreditMemoAmount().compareTo(BigDecimal.ZERO) > 0) {
                        PaymentAlias paymentAlias = paymentAliasService.findOneByPaymentDateAndPaymentName(proposalLog.getPaymentDate(), proposalLog.getPaymentName());
                        List<PrepareRunDocument> childs = prepareRunDocumentService.findChild(paymentAlias.getId(), proposalLog.getOriginalCompCode(), proposalLog.getOriginalDocNo(), proposalLog.getOriginalFiscalYear());
                        for (PrepareRunDocument child : childs) {
                          ProposalReturnLog proposalReturnLogChild = new ProposalReturnLog();
//                          proposalReturnLogChild.setId(proposalReturnId++);
                          proposalReturnLogChild.setInvoiceCompCode(child.getInvoiceCompanyCode());
                          proposalReturnLogChild.setInvoiceDocNo(child.getInvoiceDocumentNo());
                          proposalReturnLogChild.setInvoiceFiscalYear(child.getInvoiceFiscalYear());
                          proposalReturnLogChild.setPaymentCompCode(proposalLog.getPaymentCompCode());
                          proposalReturnLogChild.setPaymentDocNo(proposalLog.getPaymentDocument());
                          proposalReturnLogChild.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                          proposalReturnLogChild.setPaymentDate(proposalLog.getPaymentDate());
                          proposalReturnLogChild.setPaymentName(proposalLog.getPaymentName());
                          proposalReturnLogChild.setVendor(proposalLog.getVendor());
                          proposalReturnLogChild.setBankKey(proposalLog.getBankKey());
                          proposalReturnLogChild.setFileName("Manual Flag");
                          proposalReturnLogChild.setTransferDate(proposalLog.getTransferDate());

                          proposalReturnLogChild.setOriginalCompCode(child.getOriginalCompanyCode());
                          proposalReturnLogChild.setOriginalDocumentNo(child.getOriginalDocumentNo());
                          proposalReturnLogChild.setOriginalFiscalYear(child.getOriginalFiscalYear());
                          proposalReturnLogChild.setCreated(new Timestamp(System.currentTimeMillis()));
                          proposalReturnLogChild.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                          proposalReturnLogsInsert.add(proposalReturnLogChild);
                        }
                      }
                    } else {
                      checkDuplicate.setInvoiceCompCode(proposalLog.getInvCompCode());
                      checkDuplicate.setInvoiceDocNo(proposalLog.getInvDocNo());
                      checkDuplicate.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                      checkDuplicate.setPaymentCompCode(proposalLog.getPaymentCompCode());
                      checkDuplicate.setPaymentDocNo(proposalLog.getPaymentDocument());
                      checkDuplicate.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                      checkDuplicate.setPaymentDate(proposalLog.getPaymentDate());
                      checkDuplicate.setPaymentName(proposalLog.getPaymentName());
                      checkDuplicate.setVendor(proposalLog.getVendor());
                      checkDuplicate.setBankKey(proposalLog.getBankKey());
                      checkDuplicate.setFileName("Manual Flag");
                      checkDuplicate.setTransferDate(proposalLog.getTransferDate());
                      checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
                      checkDuplicate.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                      proposalReturnLogsUpdate.add(checkDuplicate);

                      if (proposalLog.getCreditMemoAmount().compareTo(BigDecimal.ZERO) > 0) {
                        PaymentAlias paymentAlias = paymentAliasService.findOneByPaymentDateAndPaymentName(proposalLog.getPaymentDate(), proposalLog.getPaymentName());
                        List<PrepareRunDocument> childs = prepareRunDocumentService.findChild(paymentAlias.getId(), proposalLog.getOriginalCompCode(), proposalLog.getOriginalDocNo(), proposalLog.getOriginalFiscalYear());
                        for (PrepareRunDocument child : childs) {
                          ProposalReturnLog checkDuplicateChild =
                              proposalLogReturnService.findOneByInvoiceDocumentAndPaymentDocument(
                                  child.getInvoiceCompanyCode(), child.getInvoiceDocumentNo(), child.getInvoiceFiscalYear(), child.getPaymentCompanyCode(), child.getPaymentDocumentNo(), child.getPaymentFiscalYear());
                          if (null == checkDuplicateChild) {
                            ProposalReturnLog proposalReturnLogChild = new ProposalReturnLog();
//                            proposalReturnLogChild.setId(proposalReturnId++);
                            proposalReturnLogChild.setInvoiceCompCode(child.getInvoiceCompanyCode());
                            proposalReturnLogChild.setInvoiceDocNo(child.getInvoiceDocumentNo());
                            proposalReturnLogChild.setInvoiceFiscalYear(child.getInvoiceFiscalYear());
                            proposalReturnLogChild.setPaymentCompCode(proposalLog.getPaymentCompCode());
                            proposalReturnLogChild.setPaymentDocNo(proposalLog.getPaymentDocument());
                            proposalReturnLogChild.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                            proposalReturnLogChild.setPaymentDate(proposalLog.getPaymentDate());
                            proposalReturnLogChild.setPaymentName(proposalLog.getPaymentName());
                            proposalReturnLogChild.setVendor(proposalLog.getVendor());
                            proposalReturnLogChild.setBankKey(proposalLog.getBankKey());
                            proposalReturnLogChild.setFileName("Manual Flag");
                            proposalReturnLogChild.setTransferDate(proposalLog.getTransferDate());

                            proposalReturnLogChild.setOriginalCompCode(child.getOriginalCompanyCode());
                            proposalReturnLogChild.setOriginalDocumentNo(child.getOriginalDocumentNo());
                            proposalReturnLogChild.setOriginalFiscalYear(child.getOriginalFiscalYear());
                            proposalReturnLogChild.setCreated(new Timestamp(System.currentTimeMillis()));
                            proposalReturnLogChild.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                            proposalReturnLogsInsert.add(proposalReturnLogChild);
                          } else {
                            checkDuplicateChild.setInvoiceCompCode(child.getInvoiceCompanyCode());
                            checkDuplicateChild.setInvoiceDocNo(child.getInvoiceDocumentNo());
                            checkDuplicateChild.setInvoiceFiscalYear(child.getInvoiceFiscalYear());
                            checkDuplicateChild.setPaymentCompCode(proposalLog.getPaymentCompCode());
                            checkDuplicateChild.setPaymentDocNo(proposalLog.getPaymentDocument());
                            checkDuplicateChild.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                            checkDuplicateChild.setPaymentDate(proposalLog.getPaymentDate());
                            checkDuplicateChild.setPaymentName(proposalLog.getPaymentName());
                            checkDuplicateChild.setVendor(proposalLog.getVendor());
                            checkDuplicateChild.setBankKey(proposalLog.getBankKey());
                            checkDuplicateChild.setFileName("Manual Flag");
                            checkDuplicateChild.setTransferDate(proposalLog.getTransferDate());
                            checkDuplicateChild.setUpdated(new Timestamp(System.currentTimeMillis()));
                            checkDuplicateChild.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                            proposalReturnLogsUpdate.add(checkDuplicateChild);
                          }
                        }
                      }
                    }
                  }

                  logType = CAN_UPDATE;
                } else {
                  logType = CANNOT_UPDATE;
                }
              }
              log.info("changeFileStatusReturnRequest {}", changeFileStatusReturnRequest);
              log.info("fileStatus return {}", changeFileStatusReturnRequest.getFileStatus());
              if (CAN_UPDATE.equalsIgnoreCase(logType)) {
                proposalLogsUpdate.add(proposalLog);
                sendMQUpdate(proposalLog, wsWebInfo);
//                proposalLogService.save(proposalLog);
              }
              ReturnLogDetail propLog = new ReturnLogDetail();
              propLog.setLogType(logType);
              propLog.setRefRunning(proposalLog.getRefRunning());
              propLog.setRefLine(proposalLog.getRefLine());
              propLog.setPaymentDate(proposalLog.getPaymentDate());
              propLog.setPaymentName(proposalLog.getPaymentName());
              propLog.setAccountNoFrom(proposalLog.getAccountNoFrom());
              propLog.setAccountNoTo(proposalLog.getAccountNoTo());
              propLog.setFileType(proposalLog.getFileType());
              propLog.setFileName(proposalLog.getFileName());
              propLog.setTransferDate(proposalLog.getTransferDate());
              propLog.setVendor(proposalLog.getVendor());
              propLog.setAmount(proposalLog.getAmount());
              propLog.setBankFee(proposalLog.getBankFee());
              propLog.setFileStatus(proposalLog.getFileStatus());
              resultList.add(propLog);

              if (proposalLog.getCreditMemoAmount().compareTo(BigDecimal.ZERO) > 0) {
//                resultList.addAll(resultListChild);
                PaymentAlias paymentAlias = paymentAliasService.findOneByPaymentDateAndPaymentName(proposalLog.getPaymentDate(), proposalLog.getPaymentName());
                List<PrepareRunDocument> childs = prepareRunDocumentService.findChild(paymentAlias.getId(), proposalLog.getOriginalCompCode(), proposalLog.getOriginalDocNo(), proposalLog.getOriginalFiscalYear());
                for (PrepareRunDocument child : childs) {
                  ReturnLogDetail propLogChild = new ReturnLogDetail();
                  propLogChild.setLogType(logType);
                  propLogChild.setRefRunning(proposalLog.getRefRunning());
                  propLogChild.setRefLine(proposalLog.getRefLine());
                  propLogChild.setPaymentDate(proposalLog.getPaymentDate());
                  propLogChild.setPaymentName(proposalLog.getPaymentName());
                  propLogChild.setAccountNoFrom(proposalLog.getAccountNoFrom());
                  propLogChild.setAccountNoTo(proposalLog.getAccountNoTo());
                  propLogChild.setFileType(proposalLog.getFileType());
                  propLogChild.setFileName(proposalLog.getFileName());
                  propLogChild.setTransferDate(proposalLog.getTransferDate());
                  propLogChild.setVendor(proposalLog.getVendor());
                  propLogChild.setAmount(child.getInvoiceAmountPaid());
                  propLogChild.setBankFee(proposalLog.getBankFee());
                  propLogChild.setFileStatus(proposalLog.getFileStatus());
                  resultList.add(propLogChild);
                }
              }
            }
          }
        }
      }
//      SqlUtil.updateNextSeries(paymentJdbcTemplate, proposalReturnId, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ);
      Long proposalReturnId = SqlUtil.getNextSeries(paymentJdbcTemplate, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ, (long) proposalReturnLogsInsert.size());
      proposalLogReturnService.deleteBatch(proposalLogsDelete);
      for (ProposalReturnLog proposalLog : proposalReturnLogsInsert) {
        proposalLog.setId(proposalReturnId++);
      }
      proposalLogReturnService.saveBatch(proposalReturnLogsInsert);
      proposalLogReturnService.updateBatch(proposalReturnLogsUpdate);
      proposalLogService.updateBatch(proposalLogsUpdate);
      result.setData(resultList);
      result.setStatus(HttpStatus.OK.value());
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      result.setData(resultList);
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return result;
    }
  }

  //    @Override
  //    public ResponseEntity<Result<List<ReturnReversePaymentResponse>>>
  // getPropLogReverseDocPayment(ReturnSearchProposalLog request) {
  //        Result<List<ReturnReversePaymentResponse>> result = new Result<>();
  //        result.setTimestamp(new Date());
  //        try {
  //            List<ReturnReversePaymentResponse> returnProposalDocumentResponse =
  // proposalLogService.findAllProposalLogReverseDocumentReturn(request);
  //            log.info("size {}", returnProposalDocumentResponse.size());
  //            result.setData(returnProposalDocumentResponse);
  //            result.setStatus(HttpStatus.OK.value());
  //            return new ResponseEntity<>(result, HttpStatus.OK);
  //        } catch (Exception e) {
  //            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
  //            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
  //            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  //        }
  //    }

  @Override
  public Result reversePayment(List<ReversePaymentReturnRequest> request) {
    Result result = new Result<>();
    List<ReturnLogDetail> resultList = new ArrayList<>();
    log.info("RETURN reversePayment");
    try {
      for (ReversePaymentReturnRequest reversePaymentReturnRequest : request) {
        ReverseDocument checkDuplicate =
            reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                reversePaymentReturnRequest.getCompCode(),
                reversePaymentReturnRequest.getAccountDocNo(),
                reversePaymentReturnRequest.getFiscalYear());
        if (null == checkDuplicate) {
          ReverseDocument reverseDocument = new ReverseDocument();
          reverseDocument.setCompanyCode(reversePaymentReturnRequest.getCompCode());
          reverseDocument.setDocumentNo(reversePaymentReturnRequest.getAccountDocNo());
          reverseDocument.setFiscalYear(reversePaymentReturnRequest.getFiscalYear());
          reverseDocument.setUserPost(reversePaymentReturnRequest.getWebInfo().getUserWebOnline());
          reverseDocument.setStatus("P");
          reverseDocument.setCreatedBy(reversePaymentReturnRequest.getWebInfo().getUserWebOnline());
          reverseDocumentService.save(reverseDocument);
        } else {
          checkDuplicate.setStatus("P");
          checkDuplicate.setUpdatedBy(reversePaymentReturnRequest.getWebInfo().getUserWebOnline());
          reverseDocumentService.save(checkDuplicate);
        }

        List<ProposalReturnLog> proposalReturnLogList = proposalLogReturnService.findOneReturnDocumentProposalReturnLog(reversePaymentReturnRequest.getCompCode(), reversePaymentReturnRequest.getAccountDocNo(), reversePaymentReturnRequest.getFiscalYear(), true);
        for (ProposalReturnLog proposalReturnLog : proposalReturnLogList) {
          ReturnReverseDocument returnReverseDocumentDuplicate =
              returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                  proposalReturnLog.getOriginalCompCode(),
                  proposalReturnLog.getOriginalDocumentNo(),
                  proposalReturnLog.getOriginalFiscalYear(),
                  false);
          if (null == returnReverseDocumentDuplicate) {
            ReturnReverseDocument returnReverseDocument =
                new ReturnReverseDocument(proposalReturnLog);
            returnReverseDocument.setPaymentIdemStatus("P");
            returnReverseDocument.setPaymentUserPost(
                reversePaymentReturnRequest.getWebInfo().getUserWebOnline());
            returnReverseDocument.setCreatedBy(
                reversePaymentReturnRequest.getWebInfo().getUserWebOnline());
            returnReverseDocumentService.save(returnReverseDocument);
          } else {
            returnReverseDocumentDuplicate.setPaymentIdemStatus("P");
            returnReverseDocumentDuplicate.setUpdatedBy(
                reversePaymentReturnRequest.getWebInfo().getUserWebOnline());
            returnReverseDocumentService.save(returnReverseDocumentDuplicate);
          }
        }

        log.info("getPaymentDocument {}", reversePaymentReturnRequest.getAccountDocNo());
        log.info("getRevDateAcct {}", reversePaymentReturnRequest.getRevDateAcct());
        FIPostRequest fiPostRequest = new FIPostRequest();
        BeanUtils.copyProperties(reversePaymentReturnRequest, fiPostRequest);
        FIMessage fiMessage = new FIMessage();
        fiMessage.setId(
            reversePaymentReturnRequest.getCompCode()
                + "."
                + reversePaymentReturnRequest.getFiscalYear()
                + "."
                + reversePaymentReturnRequest.getAccountDocNo());
        fiMessage.setType(FIMessage.Type.REQUEST.getCode());
        fiMessage.setDataType(FIMessage.DataType.REVERSE.getCode());
        fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
        fiMessage.setTo("99999");
        log.info("FIPostRequest RETURN reversePayment {}", request);
        fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
        log.info("fiMessage RETURN reversePayment{}", fiMessage);
        this.send(fiMessage);
        ProposalLog proposalLog =
            proposalLogService.findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(
                reversePaymentReturnRequest.getAccountDocNo(),
                reversePaymentReturnRequest.getCompCode(),
                reversePaymentReturnRequest.getFiscalYear());
        ReturnLogDetail propLogResult = setProposalLogToResponse(proposalLog, CAN_UPDATE);
        resultList.add(propLogResult);
      }
      result.setData(resultList);
      result.setStatus(HttpStatus.OK.value());
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      result.setData(resultList);
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return result;
    }
  }

  @Override
  public ResponseEntity<Result<List<ReturnReverseInvoiceResponse>>> getPropLogReverseDocInvoice(
      ReturnSearchProposalLog request) {
    Result<List<ReturnReverseInvoiceResponse>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<ReturnReverseInvoiceResponse> returnProposalDocumentResponse =
          proposalLogService.findAllProposalLogReverseInvoiceReturn(request);
      log.info("size {}", returnProposalDocumentResponse.size());
      result.setData(returnProposalDocumentResponse);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public Result reverseInvoice(List<ReverseInvoiceReturnRequest> request) {
    Result result = new Result<>();
    result.setTimestamp(new Date());
    List<ReturnLogDetail> resultList = new ArrayList<>();
    log.info("RETURN reverseInvoice");
    try {
      for (ReverseInvoiceReturnRequest reverseRequest : request) {
        log.info("reverseRequest : {} ", reverseRequest);
        String logType = "";
        ProposalReturnLog proposalReturnLog =
            proposalLogReturnService.findOneById(reverseRequest.getId());

        if (proposalReturnLog.getRevPaymentDocNo() != null) {
          proposalReturnLog.setReasonText(reverseRequest.getReason());
          proposalLogReturnService.save(proposalReturnLog);
          GLHead glHead =
              glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
                  reverseRequest.getCompCode(),
                  reverseRequest.getAccountDocNo(),
                  reverseRequest.getFiscalYear());
          GLLine glLine = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(reverseRequest.getCompCode(), reverseRequest.getAccountDocNo(), reverseRequest.getFiscalYear(), "K");
          log.info("glHead : {} ", glHead);
          if (null != glHead.getPaymentId()) {

            ReverseDocument checkDuplicate =
                reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                    reverseRequest.getCompCode(),
                    reverseRequest.getAccountDocNo(),
                    reverseRequest.getFiscalYear());
            if (null == checkDuplicate) {
              ReverseDocument reverseDocument = new ReverseDocument();
              reverseDocument.setCompanyCode(reverseRequest.getCompCode());
              reverseDocument.setDocumentNo(reverseRequest.getAccountDocNo());
              reverseDocument.setFiscalYear(reverseRequest.getFiscalYear());
              reverseDocument.setUserPost(reverseRequest.getWebInfo().getUserWebOnline());
              reverseDocument.setPoDocNo(null != glLine ? glLine.getPoDocumentNo() : null);
              reverseDocument.setStatus("P");
              reverseDocument.setCreatedBy(reverseRequest.getWebInfo().getUserWebOnline());
              reverseDocumentService.save(reverseDocument);
            } else {
              checkDuplicate.setStatus("P");
              checkDuplicate.setUpdatedBy(reverseRequest.getWebInfo().getUserWebOnline());
              reverseDocumentService.save(checkDuplicate);
            }

            ReturnReverseDocument returnReverseDocumentDuplicate =
                returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                    proposalReturnLog.getOriginalCompCode(),
                    proposalReturnLog.getOriginalDocumentNo(),
                    proposalReturnLog.getOriginalFiscalYear(), false);
            if (null == returnReverseDocumentDuplicate) {
              ReturnReverseDocument returnReverseDocument = new ReturnReverseDocument(proposalReturnLog);
              returnReverseDocument.setOriginalIdemStatus("P");
              returnReverseDocument.setOriginalUserPost(reverseRequest.getWebInfo().getUserWebOnline());
              returnReverseDocument.setPoDocNo(null != glLine ? glLine.getPoDocumentNo() : null);
              returnReverseDocument.setCreatedBy(reverseRequest.getWebInfo().getUserWebOnline());
              returnReverseDocumentService.save(returnReverseDocument);
            } else {
              returnReverseDocumentDuplicate.setOriginalIdemStatus("P");
              returnReverseDocumentDuplicate.setUpdatedBy(reverseRequest.getWebInfo().getUserWebOnline());
              returnReverseDocumentService.save(returnReverseDocumentDuplicate);
            }

            FIPostRequest fiPostRequest = new FIPostRequest();
            BeanUtils.copyProperties(reverseRequest, fiPostRequest);
            fiPostRequest.setReason(reverseRequest.getReason());
            FIMessage fiMessage = new FIMessage();
            fiMessage.setId(
                reverseRequest.getCompCode()
                    + "."
                    + reverseRequest.getFiscalYear()
                    + "."
                    + reverseRequest.getAccountDocNo());
            fiMessage.setType(FIMessage.Type.REQUEST.getCode());
            fiMessage.setDataType(FIMessage.DataType.REVERSE_INVOICE.getCode());
            fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
            fiMessage.setTo(reverseRequest.getCompCode().substring(0, 2));
            log.info("FIPostRequest {}", request);
            fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
            log.info("fiMessage {}", fiMessage);
            this.sendReverse(fiMessage, reverseRequest.getCompCode());
            logType = CAN_UPDATE;
          } else {
            logType = CANNOT_UPDATE;
          }
          ReturnLogDetail propLogResult = setProposalLogToResponse(proposalReturnLog, logType);
          resultList.add(propLogResult);
        } else {
          GLLine glLine = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(reverseRequest.getCompCode(), reverseRequest.getAccountDocNo(), reverseRequest.getFiscalYear(), "K");
          ReverseDocument checkDuplicate =
              reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                  proposalReturnLog.getPaymentCompCode(),
                  proposalReturnLog.getPaymentDocNo(),
                  proposalReturnLog.getPaymentFiscalYear());
          if (null == checkDuplicate) {
            ReverseDocument reverseDocument = new ReverseDocument();
            reverseDocument.setCompanyCode(proposalReturnLog.getPaymentCompCode());
            reverseDocument.setDocumentNo(proposalReturnLog.getPaymentDocNo());
            reverseDocument.setFiscalYear(proposalReturnLog.getPaymentFiscalYear());
            reverseDocument.setUserPost(reverseRequest.getWebInfo().getUserWebOnline());
            reverseDocument.setPoDocNo(null != glLine ? glLine.getPoDocumentNo() : null);
            reverseDocument.setStatus("P");
            reverseDocument.setCreatedBy(reverseRequest.getWebInfo().getUserWebOnline());
            reverseDocumentService.save(reverseDocument);
          } else {
            checkDuplicate.setStatus("P");
            checkDuplicate.setUpdatedBy(reverseRequest.getWebInfo().getUserWebOnline());
            reverseDocumentService.save(checkDuplicate);
          }
          ReturnReverseDocument returnReverseDocumentDuplicate =
              returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                  proposalReturnLog.getOriginalCompCode(),
                  proposalReturnLog.getOriginalDocumentNo(),
                  proposalReturnLog.getOriginalFiscalYear(), false);
          if (null == returnReverseDocumentDuplicate) {
            ReturnReverseDocument returnReverseDocument = new ReturnReverseDocument(proposalReturnLog);
            returnReverseDocument.setPaymentIdemStatus("P");
            returnReverseDocument.setOriginalIdemStatus("P");
            returnReverseDocument.setPaymentUserPost(reverseRequest.getWebInfo().getUserWebOnline());
            returnReverseDocument.setCreatedBy(reverseRequest.getWebInfo().getUserWebOnline());
            returnReverseDocument.setPoDocNo(null != glLine ? glLine.getPoDocumentNo() : null);
            returnReverseDocument.setAutoStep3(true);
            returnReverseDocumentService.save(returnReverseDocument);
          } else {
            returnReverseDocumentDuplicate.setPaymentIdemStatus("P");
            returnReverseDocumentDuplicate.setOriginalIdemStatus("P");
            returnReverseDocumentDuplicate.setUpdatedBy(reverseRequest.getWebInfo().getUserWebOnline());
            returnReverseDocumentDuplicate.setAutoStep3(true);
            returnReverseDocumentService.save(returnReverseDocumentDuplicate);
          }

          log.info("getPaymentDocument {}", proposalReturnLog.getPaymentDocNo());
          log.info("getRevDateAcct {}", reverseRequest.getRevDateAcct());
          FIPostRequest fiPostRequest = new FIPostRequest();
          fiPostRequest.setCompCode(proposalReturnLog.getPaymentCompCode());
          fiPostRequest.setAccountDocNo(proposalReturnLog.getPaymentDocNo());
          fiPostRequest.setFiscalYear(proposalReturnLog.getPaymentFiscalYear());
          fiPostRequest.setFlag(1);
          fiPostRequest.setReasonNo("06");
          fiPostRequest.setReason(reverseRequest.getReason());
          fiPostRequest.setRevDateAcct(reverseRequest.getRevDateAcct());

          log.info("web Infooo {}", reverseRequest.getWebInfo());
          fiPostRequest.setWebInfo(reverseRequest.getWebInfo());

          FIMessage fiMessage = new FIMessage();
          fiMessage.setId(
              proposalReturnLog.getPaymentCompCode()
                  + "."
                  + proposalReturnLog.getPaymentFiscalYear()
                  + "."
                  + proposalReturnLog.getPaymentDocNo());
          fiMessage.setType(FIMessage.Type.REQUEST.getCode());
          fiMessage.setDataType(FIMessage.DataType.REVERSE.getCode());
          fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
          fiMessage.setTo("99999");
          log.info("FIPostRequest RETURN reversePayment {}", request);
          fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
          log.info("fiMessage RETURN reversePayment{}", fiMessage);
          this.send(fiMessage);
          ReturnLogDetail propLogResult = setProposalLogToResponse(proposalReturnLog, CAN_UPDATE);
          resultList.add(propLogResult);
          proposalReturnLog.setAutoStep3(true);
          proposalLogReturnService.save(proposalReturnLog);
        }
      }
      result.setData(resultList);
      result.setStatus(HttpStatus.OK.value());
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      result.setData(resultList);
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return result;
    }
  }

  private Boolean checkUpdateStatusComplete(ProposalLog proposalLog) {
    if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
      if (null == proposalLog.getRevPaymentDocument()) {
        proposalLog.setFileStatus("C");
        return true;
      } else {
        // delete prop log
        log.info("delete prop log");
        return false;
      }
    } else {
      return true;
    }
  }

  private Boolean checkUpdateStatusReturn(ProposalLog proposalLog) {
    if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
      if (null == proposalLog.getRevPaymentDocument()) {
        return true;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  private void send(FIMessage message) throws Exception {
    log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
    final AtomicReference<Message> msg = new AtomicReference<>();
    jmsTemplate.convertAndSend("99999.AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
      msg.set(message1);
      return message1;
    });
    log.info("msg id : {}", msg.get().getJMSMessageID());
  }

  private void sendReverse(FIMessage message, String compCode) throws Exception {
    log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
    log.info("client : {}", compCode.substring(0, 2) + ".AP.Payment");
    final AtomicReference<Message> msg = new AtomicReference<>();
    jmsTemplate.convertAndSend(
        compCode.substring(0, 2) + ".AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
          msg.set(message1);
          return message1;
        });
    log.info("msg id : {}", msg.get().getJMSMessageID());
  }

  private ReturnLogDetail setProposalLogToResponse(ProposalLog proposalLog, String logType) {
    ReturnLogDetail propLogResult = new ReturnLogDetail();
    propLogResult.setId(proposalLog.getId());
    propLogResult.setLogType(logType);
    propLogResult.setPaymentDate(proposalLog.getPaymentDate());
    propLogResult.setPaymentName(proposalLog.getPaymentName());
    propLogResult.setDocumentNo(proposalLog.getPaymentDocument());
    propLogResult.setAccountDocNo(proposalLog.getPaymentDocument());
    propLogResult.setCompCode(proposalLog.getPaymentCompCode());
    propLogResult.setFiscalYear(proposalLog.getPaymentFiscalYear());

    return propLogResult;
  }

  private ReturnLogDetail setProposalLogToResponse(
      ProposalReturnLog proposalReturnLog, String logType) {
    ReturnLogDetail propLogResult = new ReturnLogDetail();
    propLogResult.setId(proposalReturnLog.getId());
    propLogResult.setLogType(logType);
    propLogResult.setPaymentDate(proposalReturnLog.getPaymentDate());
    propLogResult.setPaymentName(proposalReturnLog.getPaymentName());
    propLogResult.setDocumentNo(proposalReturnLog.getOriginalDocumentNo());
    propLogResult.setAccountDocNo(proposalReturnLog.getOriginalDocumentNo());
    propLogResult.setCompCode(proposalReturnLog.getOriginalCompCode());
    propLogResult.setFiscalYear(proposalReturnLog.getOriginalFiscalYear());

    return propLogResult;
  }

  public void autoStep4ByMessageQueue(ProposalReturnLog proposalReturnLog) {
    try {
      GLHead glHead =
          glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
              proposalReturnLog.getOriginalCompCode(),
              proposalReturnLog.getOriginalDocumentNo(),
              proposalReturnLog.getOriginalFiscalYear());
      log.info("glHead : {} ", glHead);
      if (null != glHead.getPaymentId()) {

        ReverseDocument checkDuplicate =
            reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                proposalReturnLog.getOriginalCompCode(),
                proposalReturnLog.getOriginalDocumentNo(),
                proposalReturnLog.getOriginalFiscalYear());
        if (null == checkDuplicate) {
          ReverseDocument reverseDocument = new ReverseDocument();
          reverseDocument.setCompanyCode(proposalReturnLog.getOriginalCompCode());
          reverseDocument.setDocumentNo(proposalReturnLog.getOriginalDocumentNo());
          reverseDocument.setFiscalYear(proposalReturnLog.getOriginalFiscalYear());
          reverseDocument.setUserPost(proposalReturnLog.getCreatedBy());
          reverseDocument.setStatus("P");
          reverseDocument.setCreatedBy(proposalReturnLog.getCreatedBy());
          reverseDocumentService.save(reverseDocument);
        } else {
          checkDuplicate.setStatus("P");
          checkDuplicate.setUpdatedBy(proposalReturnLog.getCreatedBy());
          reverseDocumentService.save(checkDuplicate);
        }
        ReturnReverseDocument returnReverseDocumentDuplicate =
            returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                proposalReturnLog.getOriginalCompCode(),
                proposalReturnLog.getOriginalDocumentNo(),
                proposalReturnLog.getOriginalFiscalYear(), false);
        if (null == returnReverseDocumentDuplicate) {
          ReturnReverseDocument returnReverseDocument = new ReturnReverseDocument(proposalReturnLog);
          returnReverseDocument.setOriginalIdemStatus("P");
          returnReverseDocument.setOriginalUserPost(proposalReturnLog.getCreatedBy());
          returnReverseDocument.setCreatedBy(proposalReturnLog.getCreatedBy());
          returnReverseDocumentService.save(returnReverseDocument);
        } else {
          returnReverseDocumentDuplicate.setOriginalIdemStatus("P");
          returnReverseDocumentDuplicate.setUpdatedBy(returnReverseDocumentDuplicate.getPaymentUserPost());
          returnReverseDocumentService.save(returnReverseDocumentDuplicate);
        }

        WSWebInfo wsWebInfo = new WSWebInfo();
        wsWebInfo.setUserWebOnline(proposalReturnLog.getCreatedBy());
        wsWebInfo.setFiArea("1000");
        wsWebInfo.setCompCode("99999");
        wsWebInfo.setPaymentCenter("9999999999");

        FIPostRequest fiPostRequest = new FIPostRequest();
        fiPostRequest.setCompCode(proposalReturnLog.getOriginalCompCode());
        fiPostRequest.setAccountDocNo(proposalReturnLog.getOriginalDocumentNo());
        fiPostRequest.setFiscalYear(proposalReturnLog.getOriginalFiscalYear());
        fiPostRequest.setFlag(1);
        fiPostRequest.setReasonNo("06");
        fiPostRequest.setRevDateAcct(proposalReturnLog.getTransferDate());
        fiPostRequest.setWebInfo(wsWebInfo);

        FIMessage fiMessage = new FIMessage();
        fiMessage.setId(
            proposalReturnLog.getOriginalCompCode()
                + "."
                + proposalReturnLog.getOriginalFiscalYear()
                + "."
                + proposalReturnLog.getOriginalDocumentNo());
        fiMessage.setType(FIMessage.Type.REQUEST.getCode());
        fiMessage.setDataType(FIMessage.DataType.REVERSE_INVOICE.getCode());
        fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
        fiMessage.setTo(proposalReturnLog.getOriginalCompCode().substring(0, 2));
        fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
        log.info("fiMessage {}", fiMessage);
        this.sendReverse(fiMessage, proposalReturnLog.getOriginalCompCode());
      } else {
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sendMQUpdate(ProposalLog proposalLog, WSWebInfo wsWebInfo) {
    try {
      APStatusPaidRequest apStatusPaidRequest = new APStatusPaidRequest();
      apStatusPaidRequest.setCompCode(proposalLog.getPayingCompCode());
      apStatusPaidRequest.setAccountDocNo(proposalLog.getPaymentDocument());
      apStatusPaidRequest.setFiscalYear(proposalLog.getPaymentFiscalYear());
      apStatusPaidRequest.setStatusPaid(proposalLog.getFileStatus());
      apStatusPaidRequest.setWebInfo(wsWebInfo);
      FIMessage fiMessage = new FIMessage();
      fiMessage.setId(proposalLog.getPayingCompCode() + "-" + proposalLog.getPaymentFiscalYear() + "-" + proposalLog.getPaymentDocument());
      fiMessage.setType(FIMessage.Type.REQUEST.getCode());
      fiMessage.setDataType(FIMessage.DataType.STATUS_PAID.getCode());
      fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
      fiMessage.setTo("99999");
      //          log.info("apPaymentRequest {}", apPaymentRequest);
      fiMessage.setData(XMLUtil.xmlMarshall(APStatusPaidRequest.class, apStatusPaidRequest));
      this.send(fiMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

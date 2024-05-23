//package th.com.bloomcode.paymentservice.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import th.com.bloomcode.paymentservice.idem.dao.CompCodeRepository;
//import th.com.bloomcode.paymentservice.idem.entity.CompCode;
//import th.com.bloomcode.paymentservice.model.*;
//import th.com.bloomcode.paymentservice.model.payment.GLHead;
//import th.com.bloomcode.paymentservice.model.response.ReturnLogResultResponse;
//import th.com.bloomcode.paymentservice.payment.dao.ProposalLogRepository;
//
//import th.com.bloomcode.paymentservice.payment.entity.ProposalLog;
//import th.com.bloomcode.paymentservice.payment.entity.mapping.ReturnProposalLogResponse;
//import th.com.bloomcode.paymentservice.payment.entity.mapping.ReturnReverseInvoiceResponse;
//import th.com.bloomcode.paymentservice.payment.entity.mapping.ReturnReversePaymentResponse;
//import th.com.bloomcode.paymentservice.service.payment.impl.GLHeadServiceImpl;
//import th.com.bloomcode.paymentservice.util.SqlUtil;
//import th.com.bloomcode.paymentservice.util.Util;
//import th.com.bloomcode.paymentservice.util.XMLUtil;
//import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
//import th.com.bloomcode.paymentservice.webservice.model.request.FIPostRequest;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Slf4j
//@Service
//@Transactional
//public class ReturnService {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private final EntityManager entityManager;
//    private final ProposalLogRepository proposalLogRepository;
//    private final CompCodeRepository compCodeRepository;
//    private final JmsTemplate jmsTemplate;
//    private final GLHeadServiceImpl glHeadService;
//    public static final String CAN_UPDATE = "CAN_UPDATE";
//    public static final String CANNOT_UPDATE = "CANNOT_UPDATE";
//
//    public ReturnService(@Qualifier("paymentEntityManagerFactory") EntityManager entityManager, ProposalLogRepository proposalLogRepository, CompCodeRepository compCodeRepository, JmsTemplate jmsTemplate, GLHeadServiceImpl glHeadService) {
//        this.entityManager = entityManager;
//        this.proposalLogRepository = proposalLogRepository;
//        this.compCodeRepository = compCodeRepository;
//        this.jmsTemplate = jmsTemplate;
//        this.glHeadService = glHeadService;
//    }
//
//    public ResponseEntity<Result<List<ReturnLogResultResponse>>> getFileReturn(ReturnGetFile request) {
//        logger.info("getFileReturn BEGIN");
//        Result<List<ReturnLogResultResponse>> result = new Result<>();
//        List<ReturnLogResultResponse> listProposal = new ArrayList<>();
//        try {
//            String url = request.getDirectory() + "\\" + request.getPath();
//            String pathBackUp = url + "\\backup\\" + UUID.randomUUID();
//
//            File folder = new File(url);
//            logger.info("folder {}", folder);
//            if (folder.exists()) {
//                for (File fileEntry : folder.listFiles()) {
//                    String fileName = fileEntry.getName();
//                    if (!"backup".equalsIgnoreCase(fileEntry.getName())) {
//                        if (fileName.indexOf(".") >= 0) {
//                            String[] split = fileName.split("\\.");
//                            fileName = split[0];
//                        }
//                        if (fileName.startsWith("I") && fileName.endsWith("R")) {
//                            File inHouseFile = new File(url + "\\" + fileEntry.getName());
//                            if (inHouseFile.exists()) {
//                                logger.info("FOUND FILE TYPE IN HOUSE ========== > {} ", fileName);
//                                BufferedReader br = new BufferedReader(new FileReader(inHouseFile));
//                                this.updateReturnFileInHouse(br, listProposal, fileEntry.getName());
//                                br.close();
//                            }
//                        }
//                        else if (fileName.startsWith("GA") || fileName.startsWith("BMC")) {
//                            File giroFile = new File(url + "\\" + fileEntry.getName());
//                            if (giroFile.exists()) {
//                                logger.info("FOUND FILE TYPE GIRO ========== > {} ", fileName);
//                                BufferedReader br = new BufferedReader(new FileReader(giroFile));
//                                this.updateReturnFileGiro(br, listProposal, fileEntry.getName());
//                                br.close();
//                            }
//                        }
//                        else if (fileEntry.getName().indexOf(".r") > -1) {
//                            File smartFile = new File(url + "\\" + fileEntry.getName());
//                            if (smartFile.exists()) {
//                                logger.info("FOUND FILE TYPE SMART ========== > {} ", fileName);
//                                BufferedReader br = new BufferedReader(new FileReader(smartFile));
//                                this.updateReturnFileSmart(br, listProposal, fileEntry.getName());
//                                br.close();
//                            }
//                        }
//                        File destinationFolder = new File(pathBackUp);
//                        if (!destinationFolder.exists()) {
//                            destinationFolder.mkdirs();
//                        }
//                        boolean success = fileEntry.renameTo(new File(pathBackUp + "\\" + fileEntry.getName()));
//                        logger.info("success : {} " , success);
//                    }
//                }
//            }
//            result.setData(listProposal);
//            result.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public ResponseEntity<Result<List<ReturnProposalLogResponse>>> getPropLogReturn(ReturnSearchProposalLog request) {
//        Result<List<ReturnProposalLogResponse>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            Map<String, Object> params = new HashMap<>();
//            StringBuilder sb = new StringBuilder();
//            sb.append(" SELECT ID ");
//            sb.append(" , FILE_STATUS ");
//            sb.append(" , REF_RUNNING ");
//            sb.append(" , REF_LINE ");
//            sb.append(" , TO_CHAR(PAYMENT_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PAYMENT_DATE ");
//            sb.append(" , PAYMENT_NAME ");
//            sb.append(" , ACCOUNT_NO_FROM ");
//            sb.append(" , ACCOUNT_NO_TO ");
//            sb.append(" , FILE_TYPE ");
//            sb.append(" , FILE_NAME ");
//            sb.append(" , TO_CHAR(TRANSFER_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS TRANSFER_DATE ");
//            sb.append(" , VENDOR ");
//            sb.append(" , BANK_KEY ");
//            sb.append(" , VENDOR_BANK_ACCOUNT ");
//            sb.append(" , TRANSFER_LEVEL ");
//            sb.append(" , PAY_ACCOUNT ");
//            sb.append(" , PAYMENT_COMP_CODE ");
//            sb.append(" , PAYMENT_DOCUMENT ");
//            sb.append(" , AMOUNT ");
//            sb.append(" from PROPOSAL_LOG ");
//            sb.append(" WHERE 1 = 1 ");
//            sb.append(" AND VENDOR NOT IN ('BOT' , '0000000000') ");
//            sb.append(" AND PAYMENT_DOCUMENT IS NOT NULL ");
//            if (!Util.isEmpty(request.getGenerateFileName())) {
//                sb.append(SqlUtil.dynamicCondition(request.getGenerateFileName(), "PAYMENT_NAME", params));
//            }
//            if (!Util.isEmpty(request.getPaymentDate())) {
//                sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
//            }
//            if (!Util.isEmpty(request.getTransferDate())) {
//                sb.append(SqlUtil.dynamicDateCondition(request.getTransferDate(), "TRANSFER_DATE", params));
//            }
//            if (!Util.isEmpty(request.getAccountNoFrom())) {
//                sb.append(SqlUtil.dynamicCondition(request.getAccountNoFrom(), "ACCOUNT_NO_FROM", params));
//            }
//            if (!Util.isEmpty(request.getAccountNoTo())) {
//                sb.append(SqlUtil.dynamicCondition(request.getAccountNoTo(), "ACCOUNT_NO_TO", params));
//            }
//            if (!Util.isEmpty(request.getVendor())) {
//                sb.append(SqlUtil.dynamicCondition(request.getVendor(), "VENDOR", params));
//            }
//            if (!Util.isEmpty(request.getVendorFrom())) {
//                sb.append(SqlUtil.dynamicCondition(request.getVendorFrom(), "VENDOR", params));
//            }
//            if (!Util.isEmpty(request.getVendorTo())) {
//                sb.append(SqlUtil.dynamicCondition(request.getVendorTo(), "VENDOR", params));
//            }
//            if (!Util.isEmpty(request.getBankKey())) {
//                sb.append(SqlUtil.dynamicCondition(request.getBankKey(), "BANK_KEY", params));
//            }
//            if (!Util.isEmpty(request.getVendorBankAccount())) {
//                sb.append(SqlUtil.dynamicCondition(request.getVendorBankAccount(), "VENDOR_BANK_ACCOUNT", params));
//            }
//            if (!Util.isEmpty(request.getTransferLevel())) {
//                sb.append(SqlUtil.dynamicCondition(request.getTransferLevel(), "TRANSFER_LEVEL", params));
//            }
//            if (!Util.isEmpty(request.getPayAccount())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPayAccount(), "PAY_ACCOUNT", params));
//            }
//            if (!Util.isEmpty(request.getPaymentCompCode())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentCompCode(), "PAYMENT_COMP_CODE", params));
//            }
//            if (!Util.isEmpty(request.getPaymentDocument())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentDocument(), "PAYMENT_DOCUMENT", params));
//            }
//            if (!Util.isEmpty(request.getRefLine())) {
//                sb.append(SqlUtil.dynamicCondition(request.getRefLine(), "REF_LINE", params));
//            }
//            if (!Util.isEmpty(request.getRefRunning())) {
//                sb.append(SqlUtil.dynamicCondition(request.getRefRunning(), "REF_RUNNING", params));
//            }
//            if (!Util.isEmpty(request.getFileName())) {
//                sb.append(SqlUtil.dynamicCondition(request.getFileName(), "FILE_NAME", params));
//            }
//
//            List<String> fileTypelist = new ArrayList<String>();
//            if (request.isFileTypeGiro()) {
//                fileTypelist.add("GIRO");
//            }
//
//            if (request.isFileTypeInHouse()) {
//                fileTypelist.add("INHOU");
//            }
//
//            if (request.isFileTypeSmart()) {
//                fileTypelist.add("SMART");
//            }
//
//            if (request.isFileTypeSwift()) {
//                fileTypelist.add("SWIFT");
//            }
//
//            if (fileTypelist.size() > 0) {
//                sb.append(" AND ( ");
//                int index = 0;
//                for (String item : fileTypelist) {
//                    if (0 != index) {
//                        sb.append(" OR ");
//                    }
//                    sb.append(" FILE_TYPE LIKE '" + item + "' ");
//                    index++;
//                }
//                sb.append(" ) ");
//            }
//
//            List<String> fileStatuslist = new ArrayList<String>();
//            if (request.isFileStatusComplete()) {
//                fileStatuslist.add("C");
//            }
//
//            if (request.isFileStatusReturn()) {
//                fileStatuslist.add("R");
//            }
//
//            if (request.isFileStatusNotSet()) {
//                fileStatuslist.add("NULL");
//            }
//            if (fileStatuslist.size() > 0) {
//                sb.append(" AND ( ");
//                int indexStatus = 0;
//                for (String item : fileStatuslist) {
//                    if (0 != indexStatus) {
//                        sb.append(" OR ");
//                    }
//                    if ("NULL".equalsIgnoreCase(item)) {
//                        sb.append(" FILE_STATUS IS NULL ");
//                    } else {
//                        sb.append(" FILE_STATUS LIKE '" + item + "' ");
//                    }
//                    indexStatus++;
//                }
//                sb.append(" ) ");
//            }
//
//            sb.append(" ORDER BY REF_LINE ");
//            logger.info("params : {} ", params);
//            logger.info("query : {} ", sb.toString());
//            Query q = entityManager.createNativeQuery(sb.toString(), ReturnProposalLogResponse.class);
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                q.setParameter(entry.getKey(), entry.getValue());
//            }
//            List<ReturnProposalLogResponse> returnProposalDocumentResponse = q.getResultList();
//
//
//            logger.info("size {}", returnProposalDocumentResponse.size());
//
//            result.setData(returnProposalDocumentResponse);
//            result.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public Result changeFileStatusReturn(List<ChangeFileStatusReturnRequest> request) {
//        Result result = new Result<>();
//        List<ReturnLogResultResponse> resultList = new ArrayList<>();
//        try {
//            for (ChangeFileStatusReturnRequest changeFileStatusReturnRequest : request) {
//                if (null != changeFileStatusReturnRequest.getFileStatus()) {
//                    ProposalLog proposalLog = proposalLogRepository.findOneById(changeFileStatusReturnRequest.getId());
//                    if (null != proposalLog) {
//                        ProposalLog proposalLogLevel1 = proposalLogRepository.findOneByRefRunningAndFileTypeAndTransferLevel(proposalLog.getRefRunning(), "SWIFT", "1");
//                        String logType;
//                        if ("C".equalsIgnoreCase(changeFileStatusReturnRequest.getFileStatus())) {
//                            if (this.checkUpdateStatusComplete(proposalLog)) {
//                                proposalLog.setFileStatus("C");
//                                proposalLogLevel1.setFileStatus("C");
//                                logType = CAN_UPDATE;
//                            } else {
//                                logType = CANNOT_UPDATE;
//                            }
//                        } else {
//                            if (this.checkUpdateStatusReturn(proposalLog)) {
//                                proposalLog.setFileStatus("R");
//                                proposalLogLevel1.setFileStatus("R");
//                                logType = CAN_UPDATE;
//                            } else {
//                                logType = CANNOT_UPDATE;
//                            }
//                        }
//                        logger.info("changeFileStatusReturnRequest {}", changeFileStatusReturnRequest);
//                        logger.info("fileStatus return {}", changeFileStatusReturnRequest.getFileStatus());
//                        if (CAN_UPDATE.equalsIgnoreCase(logType)) {
//                            proposalLog.setFileName("Manual flag");
//                            proposalLogRepository.save(proposalLog);
//                        }
//
//                        ReturnLogResultResponse propLog = new ReturnLogResultResponse();
//                        propLog.setLogType(logType);
//                        propLog.setRefRunning(proposalLog.getRefRunning());
//                        propLog.setRefLine(proposalLog.getRefLine());
//                        propLog.setPaymentDate(proposalLog.getPaymentDate());
//                        propLog.setPaymentName(proposalLog.getPaymentName());
//                        propLog.setAccountNoFrom(proposalLog.getAccountNoFrom());
//                        propLog.setAccountNoTo(proposalLog.getAccountNoTo());
//                        propLog.setFileType(proposalLog.getFileType());
//                        propLog.setFileName(proposalLog.getFileName());
//                        propLog.setTransferDate(proposalLog.getTransferDate());
//                        propLog.setVendor(proposalLog.getVendor());
//                        propLog.setAmount(proposalLog.getAmount());
//                        propLog.setBankFee(proposalLog.getBankFee());
//                        propLog.setFileStatus(proposalLog.getFileStatus());
//                        resultList.add(propLog);
//                    }
//                }
//            }
//            result.setData(resultList);
//            result.setStatus(HttpStatus.OK.value());
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setData(resultList);
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return result;
//        }
//    }
//
//    public ResponseEntity<Result<List<ReturnReversePaymentResponse>>> getPropLogReverseDocPayment(ReturnSearchProposalLog request) {
//        Result<List<ReturnReversePaymentResponse>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            logger.info("request : {} ", request);
//            Map<String, Object> params = new HashMap<>();
//            StringBuilder sb = new StringBuilder();
//            sb.append(" SELECT ID ");
//            sb.append(" , CASE WHEN REV_PAYMENT_DOCUMENT IS NOT NULL THEN 1 ELSE 0 END AS REV_PAYMENT_STATUS ");
//            sb.append(" , PAYMENT_COMP_CODE ");
//            sb.append(" , PAYMENT_DOCUMENT ");
//            sb.append(" , PAYMENT_FISCAL_YEAR ");
//            sb.append(" , TO_CHAR(PAYMENT_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PAYMENT_DATE ");
//            sb.append(" , PAYMENT_NAME ");
//            sb.append(" , VENDOR ");
//            sb.append(" , ORIGINAL_COMP_CODE ");
//            sb.append(" , ORIGINAL_DOC_NO ");
//            sb.append(" , REF_LINE ");
//            sb.append(" , ORIGINAL_FISCAL_YEAR ");
//            sb.append(" , TO_CHAR(TRANSFER_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS TRANSFER_DATE  ");
//            sb.append(" , FILE_NAME ");
//            sb.append(" from PROPOSAL_LOG ");
//            sb.append(" WHERE 1 = 1 ");
//            sb.append(" AND FILE_STATUS = 'R' ");
//            sb.append(" AND REV_PAYMENT_DOCUMENT IS NULL ");
//            if (!Util.isEmpty(request.getPaymentCompCode())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentCompCode(), "PAYMENT_COMP_CODE", params));
//            }
//            if (!Util.isEmpty(request.getPaymentDocument())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentDocument(), "PAYMENT_DOCUMENT", params));
//            }
//            if (!Util.isEmpty(request.getPaymentFiscalYear())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentFiscalYear(), "PAYMENT_FISCAL_YEAR", params));
//            }
//            if (!Util.isEmpty(request.getPaymentDate())) {
//                sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
//            }
//            if (!Util.isEmpty(request.getPaymentName())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
//            }
//            if (!Util.isEmpty(request.getVendor())) {
//                sb.append(SqlUtil.dynamicCondition(request.getVendor(), "VENDOR", params));
//            }
//            if (!Util.isEmpty(request.getOriginalCompCode())) {
//                sb.append(SqlUtil.dynamicCondition(request.getOriginalCompCode(), "ORIGINAL_COMP_CODE", params));
//            }
//            if (!Util.isEmpty(request.getOriginalDocNo())) {
//                sb.append(SqlUtil.dynamicCondition(request.getOriginalDocNo(), "ORIGINAL_DOC_NO", params));
//            }
//            if (!Util.isEmpty(request.getRefLine())) {
//                sb.append(SqlUtil.dynamicCondition(request.getRefLine(), "REF_LINE", params));
//            }
//            if (!Util.isEmpty(request.getOriginalFiscalYear())) {
//                sb.append(SqlUtil.dynamicCondition(request.getOriginalFiscalYear(), "ORIGINAL_FISCAL_YEAR", params));
//            }
//            if (!Util.isEmpty(request.getTransferDate())) {
//                sb.append(SqlUtil.dynamicDateCondition(request.getTransferDate(), "TRANSFER_DATE", params));
//            }
//            if (!Util.isEmpty(request.getFileName())) {
//                sb.append(SqlUtil.dynamicCondition(request.getFileName(), "FILE_NAME", params));
//            }
//            sb.append(" ORDER BY REF_LINE ");
//            logger.info("query : {} ", sb.toString());
//            Query q = entityManager.createNativeQuery(sb.toString(), ReturnReversePaymentResponse.class);
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                q.setParameter(entry.getKey(), entry.getValue());
//            }
//            List<ReturnReversePaymentResponse> returnProposalDocumentResponse = q.getResultList();
//
//            logger.info("size {}", returnProposalDocumentResponse.size());
//
//            result.setData(returnProposalDocumentResponse);
//            result.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public Result reversePayment(List<ReversePaymentReturnRequest> request) {
//        Result result = new Result<>();
//        List<ReturnLogResultResponse> resultList = new ArrayList<>();
//        logger.info("RETURN reversePayment");
//        try {
//            for (ReversePaymentReturnRequest reversePaymentReturnRequest : request) {
//                logger.info("getPaymentDocument {}", reversePaymentReturnRequest.getAccountDocNo());
//                logger.info("getRevDateAcct {}", reversePaymentReturnRequest.getRevDateAcct());
//                FIPostRequest fiPostRequest = new FIPostRequest();
//                BeanUtils.copyProperties(reversePaymentReturnRequest, fiPostRequest);
//                FIMessage fiMessage = new FIMessage();
//                fiMessage.setId(reversePaymentReturnRequest.getCompCode() + "." + reversePaymentReturnRequest.getFiscalYear() + "." + reversePaymentReturnRequest.getAccountDocNo());
//                fiMessage.setType(FIMessage.Type.REQUEST.getCode());
//                fiMessage.setDataType(FIMessage.DataType.REVERSE.getCode());
//                fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
//                fiMessage.setTo("99999");
//                logger.info("FIPostRequest {}", request);
//                fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
//                logger.info("fiMessage {}", fiMessage);
//                this.send(fiMessage);
//                ProposalLog proposalLog = proposalLogRepository.findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(reversePaymentReturnRequest.getAccountDocNo(), reversePaymentReturnRequest.getCompCode(), reversePaymentReturnRequest.getFiscalYear());
//                ReturnLogResultResponse propLogResult = setProposalLogToResponse(proposalLog, CAN_UPDATE);
//                resultList.add(propLogResult);
//            }
//            result.setData(resultList);
//            result.setStatus(HttpStatus.OK.value());
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setData(resultList);
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return result;
//        }
//    }
//
//    public ResponseEntity<Result<List<ReturnReverseInvoiceResponse>>> getPropLogReverseDocInvoice(ReturnSearchProposalLog request) {
//        Result<List<ReturnReverseInvoiceResponse>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            logger.info("request : {} ", request);
//            Map<String, Object> params = new HashMap<>();
//            StringBuilder sb = new StringBuilder();
//            sb.append(" SELECT ID ");
//            sb.append(" , CASE WHEN REV_INV_DOC_NO IS NOT NULL THEN 1 ELSE 0 END AS REV_INVOICE_STATUS ");
//            sb.append(" , NULL AS REV_INVOICE_REASON");
//            sb.append(" , ORIGINAL_COMP_CODE ");
//            sb.append(" , ORIGINAL_DOC_NO ");
//            sb.append(" , REF_LINE ");
//            sb.append(" , ORIGINAL_FISCAL_YEAR ");
//            sb.append(" , TO_CHAR(PAYMENT_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PAYMENT_DATE ");
//            sb.append(" , PAYMENT_NAME ");
//            sb.append(" , VENDOR ");
//            sb.append(" , PAYING_COMP_CODE ");
//            sb.append(" , PAYMENT_DOCUMENT ");
//            sb.append(" , PAYMENT_FISCAL_YEAR ");
//            sb.append(" , TO_CHAR(TRANSFER_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS TRANSFER_DATE  ");
//            sb.append(" , FILE_NAME ");
//            sb.append(" from PROPOSAL_LOG ");
//            sb.append(" WHERE 1 = 1 ");
////            sb.append(" AND FILE_STATUS = 'R' ");
//            sb.append(" AND REV_PAYMENT_DOCUMENT IS NOT NULL ");
//            sb.append(" AND REV_ORIGINAL_DOC_NO IS NULL ");
//            if (!Util.isEmpty(request.getOriginalCompCode())) {
//                sb.append(SqlUtil.dynamicCondition(request.getOriginalCompCode(), "ORIGINAL_COMP_CODE", params));
//            }
//            if (!Util.isEmpty(request.getOriginalDocNo())) {
//                sb.append(SqlUtil.dynamicCondition(request.getOriginalDocNo(), "ORIGINAL_DOC_NO", params));
//            }
//            if (!Util.isEmpty(request.getRefLine())) {
//                sb.append(SqlUtil.dynamicCondition(request.getRefLine(), "REF_LINE", params));
//            }
//            if (!Util.isEmpty(request.getOriginalFiscalYear())) {
//                sb.append(SqlUtil.dynamicCondition(request.getOriginalFiscalYear(), "ORIGINAL_FISCAL_YEAR", params));
//            }
//            if (!Util.isEmpty(request.getPaymentDate())) {
//                sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
//            }
//            if (!Util.isEmpty(request.getPaymentName())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
//            }
//            if (!Util.isEmpty(request.getVendor())) {
//                sb.append(SqlUtil.dynamicCondition(request.getVendor(), "VENDOR", params));
//            }
//            if (!Util.isEmpty(request.getPayingCompCode())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPayingCompCode(), "PAYING_COMP_CODE", params));
//            }
//            if (!Util.isEmpty(request.getPaymentDocument())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentDocument(), "PAYMENT_DOCUMENT", params));
//            }
//            if (!Util.isEmpty(request.getPaymentFiscalYear())) {
//                sb.append(SqlUtil.dynamicCondition(request.getPaymentFiscalYear(), "PAYMENT_FISCAL_YEAR", params));
//            }
//            if (!Util.isEmpty(request.getTransferDate())) {
//                sb.append(SqlUtil.dynamicDateCondition(request.getTransferDate(), "TRANSFER_DATE", params));
//            }
//            if (!Util.isEmpty(request.getFileName())) {
//                sb.append(SqlUtil.dynamicCondition(request.getFileName(), "FILE_NAME", params));
//            }
//            sb.append(" ORDER BY REF_LINE ");
//            logger.info("query : {} ", sb.toString());
//            Query q = entityManager.createNativeQuery(sb.toString(), ReturnReverseInvoiceResponse.class);
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                q.setParameter(entry.getKey(), entry.getValue());
//            }
//            List<ReturnReverseInvoiceResponse> returnProposalDocumentResponse = q.getResultList();
//
//            logger.info("size {}", returnProposalDocumentResponse.size());
//
//            result.setData(returnProposalDocumentResponse);
//            result.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public Result reverseInvoice(List<ReverseInvoiceReturnRequest> request) {
//        Result result = new Result<>();
//        result.setTimestamp(new Date());
//        List<ReturnLogResultResponse> resultList = new ArrayList<>();
//        logger.info("RETURN reverseInvoice");
//        try {
//            for (ReverseInvoiceReturnRequest reverseRequest : request) {
//                logger.info("reverseRequest : {} ", reverseRequest);
//                String logType = "";
//                ProposalLog proposalLog = proposalLogRepository.findOneById(reverseRequest.getId());
//                if (null != proposalLog) {
//                    proposalLog.setRevOriginalReason(reverseRequest.getReason());
//                    proposalLogRepository.save(proposalLog);
//                }
//                GLHead glHead = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear( reverseRequest.getCompCode(), reverseRequest.getAccountDocNo(),reverseRequest.getFiscalYear());
//                if (null == glHead.getPaymentId() && null == glHead.getPaymentDocumentNo()) {
//                    FIPostRequest fiPostRequest = new FIPostRequest();
//                    BeanUtils.copyProperties(reverseRequest, fiPostRequest);
//                    FIMessage fiMessage = new FIMessage();
//                    fiMessage.setId(reverseRequest.getCompCode() + "." + reverseRequest.getFiscalYear() + "." + reverseRequest.getAccountDocNo());
//                    fiMessage.setType(FIMessage.Type.REQUEST.getCode());
//                    fiMessage.setDataType(FIMessage.DataType.REVERSE_INVOICE.getCode());
//                    fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
//                    fiMessage.setTo(reverseRequest.getCompCode().substring(0, 2));
//                    logger.info("FIPostRequest {}", request);
//                    fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));
//                    logger.info("fiMessage {}", fiMessage);
//                    this.sendReverse(fiMessage, reverseRequest.getCompCode());
//                    logType = CAN_UPDATE;
//                } else {
//                    logType = CANNOT_UPDATE;
//                }
//                ReturnLogResultResponse propLogResult = setProposalLogToResponse(proposalLog, logType);
//                resultList.add(propLogResult);
//            }
//            result.setData(resultList);
//            result.setStatus(HttpStatus.OK.value());
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setData(resultList);
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return result;
//        }
//    }
//
//    private void updateComplete (String effectiveDate) {
//        if(!Util.isEmpty(effectiveDate)) {
//            logger.info(" effectiveDate {} ", effectiveDate);
//            Map<String, Object> params = new HashMap<>();
//            params.put("effectiveDate", effectiveDate);
//            StringBuilder sb = new StringBuilder();
//            sb.append("UPDATE PROPOSAL_LOG ");
//            sb.append(" SET FILE_STATUS = 'C' ");
//            sb.append(" WHERE TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = :effectiveDate ");
//            sb.append(" AND FILE_TYPE = 'SMART' ");
//
//            Query q = entityManager.createNativeQuery(sb.toString());
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                q.setParameter(entry.getKey(), entry.getValue());
//            }
//            q.executeUpdate();
//        }
//    }
//
//    private Boolean checkUpdateStatusComplete(ProposalLog proposalLog) {
//        if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
//            if (null == proposalLog.getRevPaymentDocument()) {
//                proposalLog.setFileStatus("C");
//                return true;
//            } else {
//                // delete prop log
//                logger.info("delete prop log");
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }
//
//    private Boolean checkUpdateStatusReturn(ProposalLog proposalLog) {
//        if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
//            if (null == proposalLog.getRevPaymentDocument()) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }
//
//    private void send(FIMessage message) throws Exception {
//        logger.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
//        jmsTemplate.convertAndSend("99999.AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message));
//    }
//
//    private void sendReverse(FIMessage message, String compCode) throws Exception {
//        logger.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
//        logger.info("client : {}", compCode.substring(0, 2) + ".AP.Payment");
//
//        jmsTemplate.convertAndSend(compCode.substring(0, 2) + ".AP.Payment",
//                XMLUtil.xmlMarshall(FIMessage.class, message));
//    }
//
//    private List<ReturnLogResultResponse> updateReturnFileInHouse(BufferedReader br, List<ReturnLogResultResponse> resultList, String fileName) throws Exception {
//        String strLine;
//        String transferDate = "";
//        while ((strLine = br.readLine()) != null) {
//            if (strLine.startsWith("H")) {
//                StringBuilder lineHead = new StringBuilder(strLine.trim());
//                StringBuilder transferDateBuilder = new StringBuilder();
//                lineHead.reverse();
//                transferDateBuilder.append(lineHead.substring(0, 94));
//                transferDateBuilder.reverse();
//                transferDate = transferDateBuilder.substring(0, 6);
//            }
//            if (strLine.startsWith("D")) {
//                String logType = CANNOT_UPDATE;
//                String bankCode = strLine.substring(7, 10);
//                String bankAccount = strLine.substring(10, 25);
//                String status = strLine.substring(41, 42);
//                String vendorCode = strLine.substring(48, 58);
//                String paymentCompCode = strLine.substring(58, 62);
//                if ("9999".equalsIgnoreCase(paymentCompCode)) {
//                    paymentCompCode = paymentCompCode + "9";
//                } else {
//                    CompCode compCodeObj = compCodeRepository.findOneByOldValueCode("1302");
//                    if (null != compCodeObj) {
//                        paymentCompCode = compCodeObj.getValueCode();
//                    }
//                }
//                String paymentDocument = strLine.substring(62, 72);
//                String paymentFiscalYear = strLine.substring(72, 76);
//                String paymentDate = strLine.substring(76, 82);
//                String paymentName = strLine.substring(82, 87);
//                String fiArea = strLine.substring(87, 91);
//                String paymentDateConvert = paymentDate.substring(0, 2) + "-" + paymentDate.substring(2, 4) + "-" + "20" + paymentDate.substring(4, 6);
//                String transferDateConvert = transferDate.substring(0, 2) + "-" + transferDate.substring(2, 4) + "-" + "20" + transferDate.substring(4, 6);
//                Map<String, Object> params = new HashMap<>();
//                params.put("bankKey", bankCode.trim());
//                params.put("bankAccount", bankAccount.trim());
//                params.put("vendorCode", vendorCode.trim());
//                params.put("paymentCompCode", paymentCompCode.trim());
//                params.put("paymentDocument", paymentDocument.trim());
//                params.put("paymentFiscalYear", paymentFiscalYear.trim());
//                params.put("paymentDate", paymentDateConvert);
//                params.put("transferDate", transferDateConvert);
//                params.put("paymentName", paymentName.trim());
//                params.put("fiArea", fiArea.trim());
//                params.put("fileType", "INHOU");
//                StringBuilder sb = new StringBuilder();
//                sb.append(" SELECT * FROM ");
//                sb.append(" PROPOSAL_LOG ");
//                sb.append(" WHERE SUBSTR(BANK_KEY,1,3) = :bankKey ");
//                sb.append(" AND VENDOR_BANK_ACCOUNT = :bankAccount ");
//                sb.append(" AND VENDOR = :vendorCode ");
//                sb.append(" AND PAYMENT_COMP_CODE = :paymentCompCode ");
//                sb.append(" AND PAYMENT_DOCUMENT = :paymentDocument ");
//                sb.append(" AND PAYMENT_FISCAL_YEAR = :paymentFiscalYear ");
//                sb.append(" AND TO_CHAR(PAYMENT_DATE,'DD-MM-YYYY') = :paymentDate ");
//                sb.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = :transferDate ");
//                sb.append(" AND PAYMENT_NAME = :paymentName ");
//                sb.append(" AND FI_AREA = :fiArea ");
//                sb.append(" AND FILE_TYPE = :fileType ");
//                logger.info("SQL : {} ", sb.toString());
//                Query q = entityManager.createNativeQuery(sb.toString(), ProposalLog.class);
//                for (Map.Entry<String, Object> entry : params.entrySet()) {
//                    q.setParameter(entry.getKey(), entry.getValue());
//                }
//                if (q.getResultList().size() > 0) {
//                    ProposalLog proposalLog = (ProposalLog) q.getSingleResult();
//                    if (null != proposalLog) {
//                        logger.info("FOUND !!!! proposalLog : {} ", proposalLog.getId());
//                        if ("0".equalsIgnoreCase(status)) {
//                            if (this.checkUpdateStatusComplete(proposalLog)) {
//                                proposalLog.setFileStatus("C");
//                                logType = CAN_UPDATE;
//                            }
//                        } else {
//                            if (this.checkUpdateStatusReturn(proposalLog)) {
//                                proposalLog.setFileStatus("R");
//                                logType = CAN_UPDATE;
//                            }
//                        }
//                        logger.info("SAVE proposalLog SUCCESS IN HOUSE : {} ", proposalLog.getId());
//                        proposalLog.setFileName(fileName);
//                        proposalLogRepository.save(proposalLog);
//                        ReturnLogResultResponse propLogResult = this.setProposalLogToResponse(proposalLog, logType);
//                        resultList.add(propLogResult);
//                    }
//                }
//            }
//        }
//        return resultList;
//    }
//
//    private List<ReturnLogResultResponse> updateReturnFileGiro(BufferedReader br, List<ReturnLogResultResponse> resultList, String fileName) throws Exception {
//        String strLine;
//        String transferDate = "";
//        while ((strLine = br.readLine()) != null) {
//            if (strLine.startsWith("H")) {
//                StringBuilder lineHead = new StringBuilder(strLine.trim());
//                StringBuilder transferDateBuilder = new StringBuilder();
//                lineHead.reverse();
//                transferDateBuilder.append(lineHead.substring(0, 94));
//                transferDateBuilder.reverse();
//                transferDate = transferDateBuilder.substring(0, 6);
//            }
//            if (strLine.startsWith("D")) {
//                String logType = CANNOT_UPDATE;
//                String bankCode = strLine.substring(7, 10);
//                String bankAccount = strLine.substring(10, 20);
//                String status = strLine.substring(36, 37);
//                String vendorCode = strLine.substring(43, 53);
//                String paymentCompCode = strLine.substring(53, 57);
//                if ("9999".equalsIgnoreCase(paymentCompCode)) {
//                    paymentCompCode = paymentCompCode + "9";
//                } else {
//                    CompCode compCodeObj = compCodeRepository.findOneByOldValueCode("1302");
//                    if (null != compCodeObj) {
//                        paymentCompCode = compCodeObj.getValueCode();
//                    }
//                }
//                String paymentDocument = strLine.substring(57, 67);
//                String paymentFiscalYear = strLine.substring(67, 71);
//                String paymentDate = strLine.substring(71, 77);
//                String paymentName = strLine.substring(77, 82);
//                String fiArea = strLine.substring(82, 86);
//                String paymentDateConvert = paymentDate.substring(0, 2) + "-" + paymentDate.substring(2, 4) + "-" + "20" + paymentDate.substring(4, 6);
//                String transferDateConvert = transferDate.substring(0, 2) + "-" + transferDate.substring(2, 4) + "-" + "20" + transferDate.substring(4, 6);
//                Map<String, Object> params = new HashMap<>();
//                params.put("bankKey", bankCode.trim());
//                params.put("bankAccount", bankAccount.trim());
//                params.put("vendorCode", vendorCode.trim());
//                params.put("paymentCompCode", paymentCompCode.trim());
//                params.put("paymentDocument", paymentDocument.trim());
//                params.put("paymentFiscalYear", paymentFiscalYear.trim());
//                params.put("paymentDate", paymentDateConvert);
//                params.put("transferDate", transferDateConvert);
//                params.put("paymentName", paymentName.trim());
//                params.put("fiArea", fiArea.trim());
//                params.put("fileType", "GIRO");
//                StringBuilder sb = new StringBuilder();
//                sb.append(" SELECT * FROM ");
//                sb.append(" PROPOSAL_LOG ");
//                sb.append(" WHERE SUBSTR(BANK_KEY,1,3) = :bankKey ");
//                sb.append(" AND VENDOR_BANK_ACCOUNT = :bankAccount ");
//                sb.append(" AND VENDOR = :vendorCode ");
//                sb.append(" AND PAYMENT_COMP_CODE = :paymentCompCode ");
//                sb.append(" AND PAYMENT_DOCUMENT = :paymentDocument ");
//                sb.append(" AND PAYMENT_FISCAL_YEAR = :paymentFiscalYear ");
//                sb.append(" AND TO_CHAR(PAYMENT_DATE,'DD-MM-YYYY') = :paymentDate ");
//                sb.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = :transferDate ");
//                sb.append(" AND PAYMENT_NAME = :paymentName ");
//                sb.append(" AND FI_AREA = :fiArea ");
//                sb.append(" AND FILE_TYPE = :fileType ");
//                Query q = entityManager.createNativeQuery(sb.toString(), ProposalLog.class);
//                for (Map.Entry<String, Object> entry : params.entrySet()) {
//                    q.setParameter(entry.getKey(), entry.getValue());
//                }
//                if (q.getResultList().size() > 0) {
//                    ProposalLog proposalLog = (ProposalLog) q.getSingleResult();
//                    if (null != proposalLog) {
//                        logger.info("FOUND !!!! proposalLog : {} ", proposalLog.getId());
//                        if ("0".equalsIgnoreCase(status)) {
//                            if (this.checkUpdateStatusComplete(proposalLog)) {
//                                proposalLog.setFileStatus("C");
//                                logType = CAN_UPDATE;
//                            }
//                        } else {
//                            if (this.checkUpdateStatusReturn(proposalLog)) {
//                                proposalLog.setFileStatus("R");
//                                logType = CAN_UPDATE;
//                            }
//                        }
//                        logger.info("SAVE proposalLog SUCCESS GIRO : {} ", proposalLog.getId());
//                        proposalLog.setFileName(fileName);
//                        proposalLogRepository.save(proposalLog);
//                        ReturnLogResultResponse propLogResult = this.setProposalLogToResponse(proposalLog, logType);
//                        resultList.add(propLogResult);
//                    }
//                }
//            }
//        }
//        return resultList;
//    }
//
//    private List<ReturnLogResultResponse> updateReturnFileSmart(BufferedReader br, List<ReturnLogResultResponse> resultList, String fileName) throws Exception {
//        String strLine;
//        String effectiveDate = "";
//        while ((strLine = br.readLine()) != null) {
//            StringBuilder strDetailBuilder = new StringBuilder(strLine);
//            strDetailBuilder = new StringBuilder(strDetailBuilder.substring(320, strDetailBuilder.length()));
//            strDetailBuilder.reverse();
//            strDetailBuilder = new StringBuilder(strDetailBuilder.substring(320, strDetailBuilder.length()));
//            strDetailBuilder.reverse();
//            int itemLength = 0;
//            int itemIndex = 0;
//            if ((strDetailBuilder.length() % 320) == 0) {
//                while (itemLength <= strDetailBuilder.length() - 1) {
//                    String logType = CANNOT_UPDATE;
//                    StringBuilder strDetail = new StringBuilder(strDetailBuilder.substring(itemLength, itemLength + 320));
//                    StringBuilder crfSendInfo = new StringBuilder(strDetail.substring(129, 189));
//                    String paymentDocument = "";
//                    String paymentFiscalYear = "";
//                    String paymentName = "";
//                    String paymentCompCode = "";
//                    String paymentDate = "";
//                    String tradingPartnerCategoryBank = "";
//                    String paymentMethod = "";
//                    String vendorCode = "";
//                    String bankCode = strDetail.substring(9, 12);
//                    String bankBranch = strDetail.substring(12, 16);
//                    String bankAccount = strDetail.substring(16, 27);
//                    effectiveDate = strDetail.substring(45, 53);
//                    String amount = convertAmount(strDetail.substring(57, 69));
//                    if (crfSendInfo.toString().trim().length() >= 50) {
//                        vendorCode = strDetail.substring(129, 139);
//                        paymentCompCode = strDetail.substring(139, 143);
//                        if ("9999".equalsIgnoreCase(paymentCompCode)) {
//                            paymentCompCode = paymentCompCode + "9";
//                        } else {
//                            CompCode compCodeObj = compCodeRepository.findOneByOldValueCode("1302");
//                            if (null != compCodeObj) {
//                                paymentCompCode = compCodeObj.getValueCode();
//                            }
//                        }
//                        paymentDocument = strDetail.substring(143, 153);
//                        paymentFiscalYear = strDetail.substring(153, 157);
//                        paymentDate = strDetail.substring(157, 165);
//                        paymentName = strDetail.substring(165, 170);
//                        tradingPartnerCategoryBank = strDetail.substring(170, 174);
//                        paymentMethod = strDetail.substring(174, 175);
//                    } else {
//                        vendorCode = strDetail.substring(129, 139);
//                        paymentDate = strDetail.substring(147, 155);
//                        paymentName = strDetail.substring(155, 160);
//                        tradingPartnerCategoryBank = strDetail.substring(160, 164);
//                        paymentMethod = strDetail.substring(164, 165);
//                    }
//
//                    String paymentDateConvert = paymentDate.substring(0, 2) + "-" + paymentDate.substring(2, 4) + "-" + paymentDate.substring(4, 8);
//                    String effectiveDateConvert = effectiveDate.substring(0, 2) + "-" + effectiveDate.substring(2, 4) + "-" + effectiveDate.substring(4, 8);
//
//                    if (itemIndex == 0) {
//                        this.updateComplete(effectiveDateConvert);
//                    }
//
//                    itemLength += 320;
//                    itemIndex++;
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("bankKey", bankCode.trim());
//                    params.put("vendorCode", vendorCode.trim());
//                    params.put("paymentCompCode", paymentCompCode);
//                    params.put("paymentDocument", paymentDocument.trim());
//                    params.put("paymentFiscalYear", paymentFiscalYear.trim());
//                    params.put("paymentDate", paymentDateConvert);
//                    params.put("effectiveDateConvert", effectiveDateConvert);
//                    params.put("paymentName", paymentName.trim());
//                    params.put("fileType", "SMART");
//                    params.put("amount", amount);
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(" SELECT * FROM ");
//                    sb.append(" PROPOSAL_LOG ");
//                    sb.append(" WHERE VENDOR NOT IN ('BOT', '0000000000') ");
//                    sb.append(" AND SUBSTR(BANK_KEY,1,3) = :bankKey ");
////                    sb.append(" AND VENDOR_BANK_ACCOUNT = :bankAccount ");
//                    sb.append(" AND VENDOR = :vendorCode ");
//                    sb.append(" AND PAYMENT_COMP_CODE = :paymentCompCode ");
//                    sb.append(" AND PAYMENT_DOCUMENT = :paymentDocument ");
//                    sb.append(" AND PAYMENT_FISCAL_YEAR = :paymentFiscalYear ");
//                    sb.append(" AND AMOUNT = :amount ");
//                    sb.append(" AND TO_CHAR(PAYMENT_DATE,'DD-MM-YYYY') = :paymentDate ");
//                    sb.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = :effectiveDateConvert ");
//                    sb.append(" AND PAYMENT_NAME = :paymentName ");
//                    sb.append(" AND FILE_TYPE = :fileType ");
//                    Query q = entityManager.createNativeQuery(sb.toString(), ProposalLog.class);
//                    for (Map.Entry<String, Object> entry : params.entrySet()) {
//                        q.setParameter(entry.getKey(), entry.getValue());
//                    }
//                    if (q.getResultList().size() > 0) {
//                        ProposalLog proposalLog = (ProposalLog) q.getSingleResult();
//                        if (null != proposalLog) {
//                            logger.info("FOUND !!!! proposalLog : {} ", proposalLog.getId());
//                            if (this.checkUpdateStatusReturn(proposalLog)) {
//                                proposalLog.setFileStatus("R");
//                                logType = CAN_UPDATE;
//                            }
//                            proposalLog.setFileName(fileName);
//                            proposalLogRepository.save(proposalLog);
//                            logger.info("SAVE proposalLog SUCCESS SMART : {} ", proposalLog.getId());
//                            ReturnLogResultResponse propLogResult = this.setProposalLogToResponse(proposalLog, logType);
//                            resultList.add(propLogResult);
//                        }
//                    }
//                }
//            }
//        }
//        if (resultList.size() < 1) {
//            ReturnLogResultResponse propLogResult = new ReturnLogResultResponse();
//            propLogResult.setFileName(fileName);
//            logger.info("effectiveDate : {} " , effectiveDate);
//            SimpleDateFormat dfPattern_ddMMyyyy = new SimpleDateFormat("ddMMyyyy");
//            propLogResult.setTransferDate(dfPattern_ddMMyyyy.parse(effectiveDate));
//            propLogResult.setFileStatus("SMART");
//            resultList.add(propLogResult);
//        }
//        return resultList;
//    }
//
//    private String convertAmount(String amount) {
//        String amountCuttedDecimal = amount.substring(0, amount.length() - 2);
//        String amountDecimal = amount.substring(amount.length() - 2);
//        BigDecimal newAmount = new BigDecimal(amountCuttedDecimal + "." + amountDecimal);
//        logger.info(" convertAmount : {} ", newAmount.toString());
//        return newAmount.toString();
//    }
//
//    private ReturnLogResultResponse setProposalLogToResponse(ProposalLog proposalLog, String logType) {
//        ReturnLogResultResponse propLogResult = new ReturnLogResultResponse();
//        propLogResult.setId(proposalLog.getId());
//        propLogResult.setLogType(logType);
//        propLogResult.setFileStatus(proposalLog.getFileStatus());
//        propLogResult.setRefRunning(proposalLog.getRefRunning());
//        propLogResult.setRefLine(proposalLog.getRefLine());
//        propLogResult.setPaymentDate(proposalLog.getPaymentDate());
//        propLogResult.setPaymentName(proposalLog.getPaymentName());
//        propLogResult.setAccountNoFrom(proposalLog.getAccountNoFrom());
//        propLogResult.setAccountNoTo(proposalLog.getAccountNoFrom());
//        propLogResult.setFileType(proposalLog.getFileType());
//        propLogResult.setFileName(proposalLog.getFileName());
//        propLogResult.setTransferDate(proposalLog.getTransferDate());
//        propLogResult.setVendor(proposalLog.getVendor());
//        propLogResult.setAmount(proposalLog.getAmount());
//        propLogResult.setPaymentDocument(proposalLog.getPaymentDocument());
//        return propLogResult;
//    }
//
//}

package th.com.bloomcode.paymentservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.com.bloomcode.paymentservice.model.MessageRealRun;
import th.com.bloomcode.paymentservice.model.idem.CompanyCode;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.service.idem.CompanyCodeService;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.service.payment.impl.GLHeadServiceImpl;
import th.com.bloomcode.paymentservice.service.payment.impl.GLLineServiceImpl;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.*;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;
import th.com.bloomcode.paymentservice.webservice.model.response.FIPostResponse;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@EnableJms
@Slf4j
public class MessageConsumer {

  private final GLHeadServiceImpl glHeadService;
  private final GLLineServiceImpl glLineService;
  private final PaymentProcessService paymentProcessService;
  private final PaymentRunErrorDocumentDetailLogService paymentRunErrorDocumentDetailLogService;
  private final ProposalLogService proposalLogService;
  private final UnBlockDocumentLogService unBlockDocumentLogService;
  private final PaymentRealRunLogService paymentRealRunLogService;
  private final CompanyCodeService companyCodeService;
  private final ReverseDocumentDetailLogService reverseDocumentDetailLogService;
  private final ReverseDocumentService reverseDocumentService;
  private final UnblockDocumentDetailLogService unblockDocumentDetailLogService;
  private final JUHeadService juHeadService;
  private final CommonSequenceService commonSequenceService;
  private final ProposalReturnLogService proposalReturnLogService;
  private final UnBlockDocumentMQService unBlockDocumentMQService;
  private final MassChangeDocumentService massChangeDocumentService;
  private final MassChangeDocumentDetailLogService massChangeDocumentDetailLogService;
  private final InsertDocumentFromChangeDocumentService insertDocumentFromChangeDocumentService;
  private final ReturnService returnService;
  private final ReturnReverseDocumentService returnReverseDocumentService;

  @Autowired
  public MessageConsumer(
      GLHeadServiceImpl glHeadService,
      GLLineServiceImpl glLineService,
      PaymentProcessService paymentProcessService,
      PaymentRunErrorDocumentDetailLogService paymentRunErrorDocumentDetailLogService,
      ProposalLogService proposalLogService,
      UnBlockDocumentLogService unBlockDocumentLogService,
      PaymentRealRunLogService paymentRealRunLogService,
      CompanyCodeService companyCodeService,
      ReverseDocumentDetailLogService reverseDocumentDetailLogService,
      ReverseDocumentService reverseDocumentService,
      UnblockDocumentDetailLogService unblockDocumentDetailLogService,
      JUHeadService juHeadService,
      CommonSequenceService commonSequenceService,
      ProposalReturnLogService proposalReturnLogService,
      UnBlockDocumentMQService unBlockDocumentMQService,
      MassChangeDocumentService massChangeDocumentService,
      MassChangeDocumentDetailLogService massChangeDocumentDetailLogService, InsertDocumentFromChangeDocumentService insertDocumentFromChangeDocumentService, ReturnService returnService, ReturnReverseDocumentService returnReverseDocumentService) {
    this.glHeadService = glHeadService;
    this.glLineService = glLineService;
    this.paymentProcessService = paymentProcessService;
    this.paymentRunErrorDocumentDetailLogService = paymentRunErrorDocumentDetailLogService;
    this.proposalLogService = proposalLogService;
    this.unBlockDocumentLogService = unBlockDocumentLogService;
    this.paymentRealRunLogService = paymentRealRunLogService;
    this.companyCodeService = companyCodeService;
    this.reverseDocumentDetailLogService = reverseDocumentDetailLogService;
    this.reverseDocumentService = reverseDocumentService;
    this.unblockDocumentDetailLogService = unblockDocumentDetailLogService;
    this.juHeadService = juHeadService;
    this.commonSequenceService = commonSequenceService;
    this.proposalReturnLogService = proposalReturnLogService;
    this.unBlockDocumentMQService = unBlockDocumentMQService;
    this.massChangeDocumentService = massChangeDocumentService;
    this.massChangeDocumentDetailLogService = massChangeDocumentDetailLogService;
    this.insertDocumentFromChangeDocumentService = insertDocumentFromChangeDocumentService;
    this.returnService = returnService;
    this.returnReverseDocumentService = returnReverseDocumentService;
  }

  @JmsListener(destination = "AP.ReceiveDoc")
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void receiveDoc(String message) {
    try {
      log.info("Message received In {}", System.currentTimeMillis());

      log.info("Message received AP.ReceiveDoc {} ", message);

      log.info(
          "Message received AP.ReceiveDoc {} ", XMLUtil.xmlUnmarshall(FIMessage.class, message));
      FIMessage fiMessage = XMLUtil.xmlUnmarshall(FIMessage.class, message);
      log.info("fiMessage {}", fiMessage);
      ZFIDoc zfiDoc = XMLUtil.xmlUnmarshall(ZFIDoc.class, fiMessage.getData());
      ZFIHeader zfiHeader = zfiDoc.getHeader();
      log.info("zfiDoc {}", zfiDoc);
      List<ZFILine> zfiLines = zfiDoc.getLines();

      GLHead fixGlHead =
          glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
              zfiHeader.getCompCode(), zfiHeader.getAccDocNo(), zfiHeader.getFiscalYear());

      if (fixGlHead == null && zfiHeader.getDocStatus().equalsIgnoreCase("CO")) {
        GLHead glHead = new GLHead(zfiHeader);
        glHead.setCurrency("THB");
//        if (null != glHead.getCompanyCode() && !glHead.getCompanyCode().isEmpty()) {
//          CompanyCode companyCode = companyCodeService.findOneByValueCode(glHead.getCompanyCode());
//          glHead.setCompanyName(companyCode.getName());
//        }
        glHead = glHeadService.save(glHead);
        for (ZFILine zfiLine : zfiLines) {
          GLLine glLine = new GLLine(zfiLine, glHead);
//          if (glLine.getVendor() != null && !glLine.getVendor().isEmpty()) {
//            Vendor vendor = vendorService.findOneByValueCodeAndCompCode(glLine.getVendor(), glHead.getCompanyCode());
//            glLine.setVendorName(vendor.getName());
//            glLine.setConfirmVendor(vendor.getConfirmStatus().equalsIgnoreCase("Y"));
//            if (null != glLine.getPayee() && !glLine.getPayee().isEmpty()) {
//              Vendor payee = vendorService.findOneByValueCodeAndCompCode(glLine.getPayee(), glHead.getCompanyCode());
//              glLine.setPayeeName(payee.getName());
//            }
//          }
//          if (glLine.getBankAccountNo() != null && !glLine.getBankAccountNo().isEmpty()) {
//            VendorBankAccount vendorBankAccount =
//                vendorBankAccountService.findOneByAccountNo(glLine.getBankAccountNo());
//            glLine.setBankAccountHolderName(vendorBankAccount.getBankAccountHolderName());
//            glLine.setBankKey(vendorBankAccount.getBankKey());
//            glLine.setBankName(vendorBankAccount.getBankName());
//          }
//          if (glLine.getFiArea() != null && !glLine.getFiArea().isEmpty()) {
//            Area area = areaService.findOneByValueCode(glLine.getFiArea());
//            glLine.setFiAreaName(area.getName());
//          }
//          if (glLine.getPaymentCenter() != null && !glLine.getPaymentCenter().isEmpty()) {
//            PaymentCenter paymentCenter =
//                paymentCenterService.findOneByValueCode(glLine.getPaymentCenter());
//            glLine.setPaymentCenterName(paymentCenter.getName());
//          }
          //                if(glLine.getBankAccountNo() != null
          // &&!glLine.getBankAccountNo().isEmpty() && glLine.getPaymentMethod() != null
          // &&!glLine.getPaymentMethod().isEmpty() ){
          //                    HouseBankPaymentMethod houseBankPaymentMethod =
          // this.houseBankPaymentMethodService.findOneByHouseBankAndPaymentMethod("99999", "01000",
          // glLine.getPaymentMethod());
          //                    glLine.setHouseBankKey( houseBankPaymentMethod.getHouseBank());
          //                }
//          if (glLine.getFundSource() != null && !glLine.getFundSource().isEmpty()) {
//            FundSource fundSource =
//                fundSourceService.findOneByYearAndValueCode(
//                    glLine.getOriginalFiscalYear(), glLine.getFundSource());
//            glLine.setFundType(fundSource.getFundType());
//          }
          glLineService.save(glLine);
        }
      } else if (fixGlHead != null) {
        log.info("update {}", fixGlHead.getId());

        GLHead glHead = new GLHead(zfiHeader);
        glHead.setId(fixGlHead.getId());
        glHead.setSelectGroupDocument(fixGlHead.getSelectGroupDocument());
        glHead.setPaymentDocumentNo(fixGlHead.getPaymentDocumentNo());
        glHead.setPaymentId(fixGlHead.getPaymentId());
        glHead.setDocumentCreatedReal(fixGlHead.getDocumentCreatedReal());
        glHead.setCurrency("THB");
        glHead.setCreated(fixGlHead.getCreated());
        glHead.setCreatedBy(fixGlHead.getCreatedBy());


        fixGlHead = glHeadService.save(glHead);
        for (ZFILine zfiLine : zfiLines) {
          GLLine oldGLline =
              glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndLine(
                  fixGlHead.getCompanyCode(),
                  fixGlHead.getOriginalDocumentNo(),
                  fixGlHead.getOriginalFiscalYear(),
                  zfiLine.getLine());

          log.info("oldGLline {}", fixGlHead.getCompanyCode());
          log.info("oldGLline {}", fixGlHead.getOriginalDocumentNo());
          log.info("oldGLline {}", fixGlHead.getOriginalFiscalYear());
          log.info("oldGLline {}", zfiLine.getLine());
          log.info("up date oldGLline {}", oldGLline);
          if (oldGLline == null) {
            log.info("create line {}");
            GLLine glLine = new GLLine(zfiLine, fixGlHead);
//            if (glLine.getVendor() != null && !glLine.getVendor().isEmpty()) {
//              Vendor vendor = vendorService.findOne(glLine.getVendor());
//              glLine.setVendorName(vendor.getName());
//              glLine.setConfirmVendor(vendor.getConfirmStatus().equalsIgnoreCase("Y"));
//              if (null != glLine.getPayee() && !glLine.getPayee().isEmpty()) {
//                Vendor payee = vendorService.findOne(glLine.getPayee());
//                glLine.setPayeeName(payee.getName());
//              }
//            }
//            if (glLine.getBankAccountNo() != null && !glLine.getBankAccountNo().isEmpty()) {
//              VendorBankAccount vendorBankAccount =
//                  vendorBankAccountService.findOneByAccountNo(glLine.getBankAccountNo());
//              glLine.setBankAccountHolderName(vendorBankAccount.getBankAccountHolderName());
//              glLine.setBankKey(vendorBankAccount.getBankKey());
//              glLine.setBankName(vendorBankAccount.getBankName());
//            }
//            if (glLine.getFiArea() != null && !glLine.getFiArea().isEmpty()) {
//              Area area = areaService.findOneByValueCode(glLine.getFiArea());
//              glLine.setFiAreaName(area.getName());
//            }
//            if (glLine.getPaymentCenter() != null && !glLine.getPaymentCenter().isEmpty()) {
//              PaymentCenter paymentCenter =
//                  paymentCenterService.findOneByValueCode(glLine.getPaymentCenter());
//              glLine.setPaymentCenterName(paymentCenter.getName());
//            }
            //                if(glLine.getBankAccountNo() != null
            // &&!glLine.getBankAccountNo().isEmpty() && glLine.getPaymentMethod() != null
            // &&!glLine.getPaymentMethod().isEmpty() ){
            //                    HouseBankPaymentMethod houseBankPaymentMethod =
            // this.houseBankPaymentMethodService.findOneByHouseBankAndPaymentMethod("99999",
            // "01000", glLine.getPaymentMethod());
            //                    glLine.setHouseBankKey( houseBankPaymentMethod.getHouseBank());
            //                }
//            if (glLine.getFundSource() != null && !glLine.getFundSource().isEmpty()) {
//              FundSource fundSource =
//                  fundSourceService.findOneByYearAndValueCode(
//                      glLine.getOriginalFiscalYear(), glLine.getFundSource());
//              glLine.setFundType(fundSource.getFundType());
//            }
            glLineService.save(glLine);
          } else {
            Long glLineId = oldGLline.getId();
            GLLine glLine = new GLLine(zfiLine, fixGlHead);
            BeanUtils.copyProperties(glLine, oldGLline);
            oldGLline.setId(glLineId);
//            if (oldGLline.getVendor() != null && !oldGLline.getVendor().isEmpty()) {
//              Vendor vendor = vendorService.findOne(oldGLline.getVendor());
//              oldGLline.setVendorName(vendor.getName());
//              oldGLline.setConfirmVendor(vendor.getConfirmStatus().equalsIgnoreCase("Y"));
//              if (null != oldGLline.getPayee() && !oldGLline.getPayee().isEmpty()) {
//                Vendor payee = vendorService.findOne(oldGLline.getPayee());
//                oldGLline.setPayeeName(payee.getName());
//              }
//            }
//            if (oldGLline.getBankAccountNo() != null && !oldGLline.getBankAccountNo().isEmpty()) {
//              VendorBankAccount vendorBankAccount =
//                  vendorBankAccountService.findOneByAccountNo(oldGLline.getBankAccountNo());
//              oldGLline.setBankAccountHolderName(vendorBankAccount.getBankAccountHolderName());
//              oldGLline.setBankKey(vendorBankAccount.getBankKey());
//              oldGLline.setBankName(vendorBankAccount.getBankName());
//            }
//            if (oldGLline.getFiArea() != null && !oldGLline.getFiArea().isEmpty()) {
//              Area area = areaService.findOneByValueCode(oldGLline.getFiArea());
//              oldGLline.setFiAreaName(area.getName());
//            }
//            if (oldGLline.getPaymentCenter() != null && !oldGLline.getPaymentCenter().isEmpty()) {
//              PaymentCenter paymentCenter =
//                  paymentCenterService.findOneByValueCode(oldGLline.getPaymentCenter());
//              oldGLline.setPaymentCenterName(paymentCenter.getName());
//            }
            //                if(oldGLline.getBankAccountNo() != null
            // &&!oldGLline.getBankAccountNo().isEmpty() && oldGLline.getPaymentMethod() != null
            // &&!oldGLline.getPaymentMethod().isEmpty() ){
            //                    HouseBankPaymentMethod houseBankPaymentMethod =
            // this.houseBankPaymentMethodService.findOneByHouseBankAndPaymentMethod("99999",
            // "01000", oldGLline.getPaymentMethod());
            //                    oldGLline.setHouseBankKey( houseBankPaymentMethod.getHouseBank());
            //                }
//            if (oldGLline.getFundSource() != null && !oldGLline.getFundSource().isEmpty()) {
//              FundSource fundSource =
//                  fundSourceService.findOneByYearAndValueCode(
//                      oldGLline.getOriginalFiscalYear(), oldGLline.getFundSource());
//              oldGLline.setFundType(fundSource.getFundType());
//            }
            glLineService.save(oldGLline);
          }
        }
      } else {
        log.info("Message RE and not have in payment {}", "");
      }

      log.info("Message received Out {}", System.currentTimeMillis());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @JmsListener(destination = "AP.UpPbk")
  public void updatePbk(String message) {
    try {

      log.info("Message unBlock AP.UpPbk in {}", System.currentTimeMillis());

      log.info("Message unBlock AP.UpPbk {} ", message);

      log.info("Message unBlock AP.UpPbk {} ", XMLUtil.xmlUnmarshall(FIMessage.class, message));

      String username = "";
      Timestamp updateDate = null;
      FIMessage fiMessage = XMLUtil.xmlUnmarshall(FIMessage.class, message);

      FIPostResponse fiPostResponse =
          XMLUtil.xmlUnmarshall(FIPostResponse.class, fiMessage.getData());

      if (null != fiPostResponse) {
        username = null != fiPostResponse.getFiDoc() && null != fiPostResponse.getFiDoc().getHeader() ? fiPostResponse.getFiDoc().getHeader().getUserPost() : "";
        updateDate = new Timestamp(System.currentTimeMillis());
      }

      log.info(" getDataType {} ", fiMessage.getDataType());
      log.info(" FIMessage.DataType.PBK_ALL {} ", FIMessage.DataType.PBK_ALL.getCode());
      if (fiMessage.getDataType().equalsIgnoreCase(FIMessage.DataType.PBK_ALL.getCode())) {
        insertDocumentFromChangeDocumentService.insertDocument(fiPostResponse.getFiDoc(), username, updateDate);
      }
      log.info(" by payment Change {} ", fiPostResponse);
      if (fiPostResponse.getStatus() != null
          && fiPostResponse.getStatus().equalsIgnoreCase("S")) {
        List<FICreateResponseBase> fiCreateResponseBaseList = fiPostResponse.getAutoDoc();

        for (FICreateResponseBase list : fiCreateResponseBaseList) {
          if (null != list.getStatus() && list.getStatus().equalsIgnoreCase("S")) {
            if (null != list.getPaymentBlock() && list.getPaymentBlock().equalsIgnoreCase("E")) {
//                GLHead glHead =
//                    glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//                GLLine glLine =
//                    glLineService.findOneUnBlockDocumentByCondition(
//                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), false);
//                if (glHead != null) {
//                  glHead.setReverseOriginalDocumentNo(list.getRevAccDocNo());
//                  glHead.setReverseOriginalFiscalYear(list.getRevFiscalYear());
//                  glHeadService.save(glHead);
//                }
//                if (glLine != null) {
//                //                  glLine.setPaymentBlock("E");
//                log.info(" glLine.setPaymentBlock(E) {} ", list.getPaymentBlock());
//                  glLine.setPaymentBlock(list.getPaymentBlock());
//                  glLineService.save(glLine);
//                }
              glHeadService.updateBlockReverse(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getRevAccDocNo(), list.getRevFiscalYear(), username, updateDate, list.getDocStatus());
              glLineService.updateBlockStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getPaymentBlock(), false, username, updateDate);
//                UnBlockDocumentLog checkDuplicate =
//                    unBlockDocumentLogService
//                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//                if (null == checkDuplicate) {
//                  UnBlockDocumentLog unBlockDocumentLog = new UnBlockDocumentLog();
//                  unBlockDocumentLog.setValueNew("E");
//                  unBlockDocumentLog.setIdemStatus("S");
//                  unBlockDocumentLogService.save(unBlockDocumentLog);
//                } else {
//                  checkDuplicate.setIdemStatus("S");
//                  unBlockDocumentLogService.save(checkDuplicate);
//                }
              unBlockDocumentLogService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S", list.getPaymentBlock(), username, updateDate);
//                UnBlockDocumentMQ unBlockDocumentMQOld =
//                    unBlockDocumentMQService
//                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//                if (null != unBlockDocumentMQOld) {
//                  unBlockDocumentMQOld.setIdemStatus("S");
//                  unBlockDocumentMQOld.setReverseOriginalDocumentNo(list.getRevAccDocNo());
//                  unBlockDocumentMQOld.setReverseOriginalFiscalYear(list.getRevFiscalYear());
//                  unBlockDocumentMQOld.setReverseDocumentType(list.getRevDocType());
//                  unBlockDocumentMQOld.setReverseCompanyCode(list.getCompCode());
//                  unBlockDocumentMQService.save(unBlockDocumentMQOld);
//                }
              unBlockDocumentMQService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S", list.getRevAccDocNo(), list.getRevFiscalYear(), list.getRevDocType(), list.getCompCode(), username, updateDate);
              massChangeDocumentService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S", username, updateDate);
            } else {
//                GLLine glLine =
//                    glLineService.findOneUnBlockDocumentByCondition(
//                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), true);
//                if (glLine != null) {
//                  log.info(" glLine.setPaymentBlock(canpay ) {} ", list.getPaymentBlock());
//                  glLine.setPaymentBlock(list.getPaymentBlock());
//                  glLineService.save(glLine);
//                }
              glLineService.updateBlockStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getPaymentBlock(), true, username, updateDate);
//                UnBlockDocumentLog checkDuplicate =
//                    unBlockDocumentLogService
//                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//
//                if (null != checkDuplicate) {
//                  checkDuplicate.setIdemStatus("S");
//                  unBlockDocumentLogService.save(checkDuplicate);
//                }
              unBlockDocumentLogService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S", list.getPaymentBlock(), username, updateDate);
//                UnBlockDocumentMQ unBlockDocumentMQOld =
//                    unBlockDocumentMQService
//                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//                if (null != unBlockDocumentMQOld) {
//                  unBlockDocumentMQOld.setIdemStatus("S");
//                  unBlockDocumentMQService.save(unBlockDocumentMQOld);
//                }
              unBlockDocumentMQService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S", list.getPaymentBlock(), username, updateDate);
//                MassChangeDocument massChangeDocument =
//                    massChangeDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
//                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//                if (null != massChangeDocument) {
//                  massChangeDocument.setStatus("S");
//                  massChangeDocumentService.save(massChangeDocument);
//                }
              massChangeDocumentService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S", username, updateDate);
            }
          }
        }

      } else if (fiPostResponse.getStatus() != null
          && fiPostResponse.getStatus().equalsIgnoreCase("E")) {
        List<FICreateResponseBase> fiCreateResponseBaseList = fiPostResponse.getAutoDoc();
        if (fiCreateResponseBaseList.size() > 0) {

          for (FICreateResponseBase list : fiCreateResponseBaseList) {
//              GLHead glHead =
//                  glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                      list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//              GLLine glLine =
//                  glLineService.findOneUnBlockDocumentByCondition(
//                      list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), false);
//              if (glHead != null) {
//                glHead.setReverseOriginalDocumentNo(list.getRevAccDocNo());
//                glHead.setReverseOriginalFiscalYear(list.getRevFiscalYear());
//                glHeadService.save(glHead);
//              }
//
//              if (glLine != null) {
//                glLine.setPaymentBlock("E");
//                glLineService.save(glLine);
//              }
            glHeadService.updateBlockReverse(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getRevAccDocNo(), list.getRevFiscalYear(), username, updateDate, list.getDocStatus());
            glLineService.updateBlockStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getPaymentBlock(), false, username, updateDate);
            //              UnBlockDocumentLog checkDuplicate =
            //                  unBlockDocumentLogService
            //
            // .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
            //                          glLine.getCompanyCode(),
            //                          glLine.getOriginalDocumentNo(),
            //                          glLine.getOriginalFiscalYear());
            //              if (null == checkDuplicate) {
            //                UnBlockDocumentLog unBlockDocumentLog = new UnBlockDocumentLog();
            //                unBlockDocumentLog.setValueNew("E");
            //                unBlockDocumentLog.setIdemStatus("S");
            //                unBlockDocumentLogService.save(unBlockDocumentLog);
            //              } else {
            //                checkDuplicate.setIdemStatus("S");
            //                unBlockDocumentLogService.save(checkDuplicate);
            //              }
            unBlockDocumentLogService.updateStatus(
                list.getCompCode(),
                list.getAccDocNo(),
                list.getFiscalYear(),
                "E",
                list.getPaymentBlock(), username, updateDate);
//              UnBlockDocumentMQ unBlockDocumentMQOld =
//                  unBlockDocumentMQService
//                      .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                          glLine.getCompanyCode(),
//                          glLine.getOriginalDocumentNo(),
//                          glLine.getOriginalFiscalYear());
//              if (null != unBlockDocumentMQOld) {
//                unBlockDocumentMQOld.setIdemStatus("S");
//                unBlockDocumentMQService.save(unBlockDocumentMQOld);
//              }
            unBlockDocumentMQService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "E", list.getPaymentBlock(), username, updateDate);
//              MassChangeDocument massChangeDocument =
//                  massChangeDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
//                      fiPostResponse.getCompCode(),
//                      fiPostResponse.getAccDocNo(),
//                      fiPostResponse.getFiscalYear());
//              if (null != massChangeDocument) {
//                massChangeDocument.setStatus("S");
//                massChangeDocumentService.save(massChangeDocument);
//              }
            massChangeDocumentService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "E", username, updateDate);

            UnblockDocumentDetailLog unblockDocumentDetailLog = new UnblockDocumentDetailLog();
            unblockDocumentDetailLog.setOriginalCompanyCode(fiPostResponse.getCompCode());
            unblockDocumentDetailLog.setOriginalDocumentNo(fiPostResponse.getAccDocNo());
            unblockDocumentDetailLog.setOriginalFiscalYear(fiPostResponse.getFiscalYear());
            if (list.getRemark() != null && list.getRemark().length() > 4000) {
              unblockDocumentDetailLog.setText(list.getRemark().substring(0, 3999));
            } else {
              unblockDocumentDetailLog.setText(list.getRemark());
            }

            unblockDocumentDetailLogService.save(unblockDocumentDetailLog);
          }
        } else {
//            UnBlockDocumentLog checkDuplicate =
//                unBlockDocumentLogService
//                    .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                        fiPostResponse.getCompCode(),
//                        fiPostResponse.getAccDocNo(),
//                        fiPostResponse.getFiscalYear());
//            if (null == checkDuplicate) {
//              UnBlockDocumentLog unBlockDocumentLog = new UnBlockDocumentLog();
//              unBlockDocumentLog.setValueNew("E");
//              unBlockDocumentLog.setIdemStatus("E");
//              unBlockDocumentLogService.save(unBlockDocumentLog);
//            } else {
//              checkDuplicate.setIdemStatus("E");
//              unBlockDocumentLogService.save(checkDuplicate);
//            }
          unBlockDocumentLogService.updateStatus(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear(), "E", "B", username, updateDate);
//            UnBlockDocumentMQ unBlockDocumentMQOld =
//                unBlockDocumentMQService
//                    .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                        fiPostResponse.getCompCode(),
//                        fiPostResponse.getAccDocNo(),
//                        fiPostResponse.getFiscalYear());
//            if (null != unBlockDocumentMQOld) {
//              unBlockDocumentMQOld.setIdemStatus("E");
//              unBlockDocumentMQService.save(unBlockDocumentMQOld);
//            }
          unBlockDocumentMQService.updateStatus(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear(), "E", "B", username, updateDate);
//            MassChangeDocument massChangeDocument =
//                massChangeDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
//                    fiPostResponse.getCompCode(),
//                    fiPostResponse.getAccDocNo(),
//                    fiPostResponse.getFiscalYear());
//            if (null != massChangeDocument) {
//              massChangeDocument.setStatus("E");
//              massChangeDocumentService.save(massChangeDocument);
//            }
          massChangeDocumentService.updateStatus(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear(), "E", username, updateDate);

          UnblockDocumentDetailLog unblockDocumentDetailLog = new UnblockDocumentDetailLog();
          unblockDocumentDetailLog.setOriginalCompanyCode(fiPostResponse.getCompCode());
          unblockDocumentDetailLog.setOriginalDocumentNo(fiPostResponse.getAccDocNo());
          unblockDocumentDetailLog.setOriginalFiscalYear(fiPostResponse.getFiscalYear());
          if (fiPostResponse.getRemark().length() > 4000) {
            unblockDocumentDetailLog.setText(fiPostResponse.getRemark().substring(0, 3999));
          } else {
            unblockDocumentDetailLog.setText(fiPostResponse.getRemark());
          }

          unblockDocumentDetailLogService.save(unblockDocumentDetailLog);

          MassChangeDocumentDetailLog massChangeDocumentDetailLog =
              new MassChangeDocumentDetailLog();
          massChangeDocumentDetailLog.setCompanyCode(fiPostResponse.getCompCode());
          massChangeDocumentDetailLog.setDocumentNo(fiPostResponse.getAccDocNo());
          massChangeDocumentDetailLog.setFiscalYear(fiPostResponse.getFiscalYear());
          if (fiPostResponse.getRemark().length() > 4000) {
            massChangeDocumentDetailLog.setText(fiPostResponse.getRemark().substring(0, 3999));
          } else {
            massChangeDocumentDetailLog.setText(fiPostResponse.getRemark());
          }

          massChangeDocumentDetailLogService.save(massChangeDocumentDetailLog);

          log.info("fiPostResponse getCompCode {}", fiPostResponse.getCompCode());
          log.info("fiPostResponse getAccDocNo {}", fiPostResponse.getAccDocNo());
          log.info("fiPostResponse getFiscalYear {}", fiPostResponse.getFiscalYear());

//            GLLine glLine =
//                glLineService.findOneUnBlockDocumentByCondition(
//                    fiPostResponse.getCompCode(),
//                    fiPostResponse.getAccDocNo(),
//                    fiPostResponse.getFiscalYear(),
//                    true);
//
//            if (glLine != null) {
//              glLine.setPaymentBlock("B");
//              glLineService.save(glLine);
//            }

          glLineService.updateBlockStatusErrorCase(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear(), username, updateDate);
        }
      }

      log.info("Message unBlock AP.UpPbk out {}", System.currentTimeMillis());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

//  @JmsListener(destination = "AP.UpPbk1")
//  public void updatePbk1(String message) {
//    try {
//
//      log.info("Message unBlock AP.UpPbk in {}", System.currentTimeMillis());
//
//      log.info("Message unBlock AP.UpPbk {} ", message);
//
//      log.info("Message unBlock AP.UpPbk {} ", XMLUtil.xmlUnmarshall(FIMessage.class, message));
//
//      FIMessage fiMessage = XMLUtil.xmlUnmarshall(FIMessage.class, message);
//
//      FIPostResponse fiPostResponse =
//              XMLUtil.xmlUnmarshall(FIPostResponse.class, fiMessage.getData());
//
//
//
//      log.info(" getDataType {} ", fiMessage.getDataType());
//      log.info(" FIMessage.DataType.PBK_ALL {} ",FIMessage.DataType.PBK_ALL.getCode());
//      if (fiMessage.getDataType().equalsIgnoreCase(FIMessage.DataType.PBK_ALL.getCode())) {
//        insertDocumentFromChangeDocumentService.insertDocument(fiPostResponse.getFiDoc());
//      }
//      log.info(" by payment Change {} ", fiPostResponse);
//      if (fiPostResponse.getStatus() != null
//              && fiPostResponse.getStatus().equalsIgnoreCase("S")) {
//        List<FICreateResponseBase> fiCreateResponseBaseList = fiPostResponse.getAutoDoc();
//
//        for (FICreateResponseBase list : fiCreateResponseBaseList) {
//          if (null != list.getStatus() && list.getStatus().equalsIgnoreCase("S")) {
//            if (null != list.getPaymentBlock() && list.getPaymentBlock().equalsIgnoreCase("E")) {
////                GLHead glHead =
////                    glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
////                GLLine glLine =
////                    glLineService.findOneUnBlockDocumentByCondition(
////                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), false);
////                if (glHead != null) {
////                  glHead.setReverseOriginalDocumentNo(list.getRevAccDocNo());
////                  glHead.setReverseOriginalFiscalYear(list.getRevFiscalYear());
////                  glHeadService.save(glHead);
////                }
////                if (glLine != null) {
////                //                  glLine.setPaymentBlock("E");
////                log.info(" glLine.setPaymentBlock(E) {} ", list.getPaymentBlock());
////                  glLine.setPaymentBlock(list.getPaymentBlock());
////                  glLineService.save(glLine);
////                }
//              glHeadService.updateBlockReverse(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getRevAccDocNo(), list.getRevFiscalYear());
//              glLineService.updateBlockStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getPaymentBlock(), false);
////                UnBlockDocumentLog checkDuplicate =
////                    unBlockDocumentLogService
////                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
////                if (null == checkDuplicate) {
////                  UnBlockDocumentLog unBlockDocumentLog = new UnBlockDocumentLog();
////                  unBlockDocumentLog.setValueNew("E");
////                  unBlockDocumentLog.setIdemStatus("S");
////                  unBlockDocumentLogService.save(unBlockDocumentLog);
////                } else {
////                  checkDuplicate.setIdemStatus("S");
////                  unBlockDocumentLogService.save(checkDuplicate);
////                }
//              unBlockDocumentLogService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S",  list.getPaymentBlock());
////                UnBlockDocumentMQ unBlockDocumentMQOld =
////                    unBlockDocumentMQService
////                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
////                if (null != unBlockDocumentMQOld) {
////                  unBlockDocumentMQOld.setIdemStatus("S");
////                  unBlockDocumentMQOld.setReverseOriginalDocumentNo(list.getRevAccDocNo());
////                  unBlockDocumentMQOld.setReverseOriginalFiscalYear(list.getRevFiscalYear());
////                  unBlockDocumentMQOld.setReverseDocumentType(list.getRevDocType());
////                  unBlockDocumentMQOld.setReverseCompanyCode(list.getCompCode());
////                  unBlockDocumentMQService.save(unBlockDocumentMQOld);
////                }
//              unBlockDocumentMQService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S", list.getRevAccDocNo(), list.getRevFiscalYear(), list.getRevDocType(), list.getCompCode());
//              massChangeDocumentService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S");
//            } else {
////                GLLine glLine =
////                    glLineService.findOneUnBlockDocumentByCondition(
////                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), true);
////                if (glLine != null) {
////                  log.info(" glLine.setPaymentBlock(canpay ) {} ", list.getPaymentBlock());
////                  glLine.setPaymentBlock(list.getPaymentBlock());
////                  glLineService.save(glLine);
////                }
//              glLineService.updateBlockStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getPaymentBlock(), true);
////                UnBlockDocumentLog checkDuplicate =
////                    unBlockDocumentLogService
////                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
////
////                if (null != checkDuplicate) {
////                  checkDuplicate.setIdemStatus("S");
////                  unBlockDocumentLogService.save(checkDuplicate);
////                }
//              unBlockDocumentLogService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S",  list.getPaymentBlock());
////                UnBlockDocumentMQ unBlockDocumentMQOld =
////                    unBlockDocumentMQService
////                        .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                            list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
////                if (null != unBlockDocumentMQOld) {
////                  unBlockDocumentMQOld.setIdemStatus("S");
////                  unBlockDocumentMQService.save(unBlockDocumentMQOld);
////                }
//              unBlockDocumentMQService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S",list.getPaymentBlock());
////                MassChangeDocument massChangeDocument =
////                    massChangeDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
////                        list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
////                if (null != massChangeDocument) {
////                  massChangeDocument.setStatus("S");
////                  massChangeDocumentService.save(massChangeDocument);
////                }
//              massChangeDocumentService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "S");
//            }
//          }
//        }
//
//      } else if (fiPostResponse.getStatus() != null
//              && fiPostResponse.getStatus().equalsIgnoreCase("E")) {
//        List<FICreateResponseBase> fiCreateResponseBaseList = fiPostResponse.getAutoDoc();
//        if (fiCreateResponseBaseList.size() > 0) {
//
//          for (FICreateResponseBase list : fiCreateResponseBaseList) {
////              GLHead glHead =
////                  glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                      list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
////              GLLine glLine =
////                  glLineService.findOneUnBlockDocumentByCondition(
////                      list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), false);
////              if (glHead != null) {
////                glHead.setReverseOriginalDocumentNo(list.getRevAccDocNo());
////                glHead.setReverseOriginalFiscalYear(list.getRevFiscalYear());
////                glHeadService.save(glHead);
////              }
////
////              if (glLine != null) {
////                glLine.setPaymentBlock("E");
////                glLineService.save(glLine);
////              }
//            glHeadService.updateBlockReverse(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getRevAccDocNo(), list.getRevFiscalYear());
//            glLineService.updateBlockStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getPaymentBlock(), false);
//            //              UnBlockDocumentLog checkDuplicate =
//            //                  unBlockDocumentLogService
//            //
//            // .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//            //                          glLine.getCompanyCode(),
//            //                          glLine.getOriginalDocumentNo(),
//            //                          glLine.getOriginalFiscalYear());
//            //              if (null == checkDuplicate) {
//            //                UnBlockDocumentLog unBlockDocumentLog = new UnBlockDocumentLog();
//            //                unBlockDocumentLog.setValueNew("E");
//            //                unBlockDocumentLog.setIdemStatus("S");
//            //                unBlockDocumentLogService.save(unBlockDocumentLog);
//            //              } else {
//            //                checkDuplicate.setIdemStatus("S");
//            //                unBlockDocumentLogService.save(checkDuplicate);
//            //              }
//            unBlockDocumentLogService.updateStatus(
//                    list.getCompCode(),
//                    list.getAccDocNo(),
//                    list.getFiscalYear(),
//                    "E",
//                    list.getPaymentBlock());
////              UnBlockDocumentMQ unBlockDocumentMQOld =
////                  unBlockDocumentMQService
////                      .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                          glLine.getCompanyCode(),
////                          glLine.getOriginalDocumentNo(),
////                          glLine.getOriginalFiscalYear());
////              if (null != unBlockDocumentMQOld) {
////                unBlockDocumentMQOld.setIdemStatus("S");
////                unBlockDocumentMQService.save(unBlockDocumentMQOld);
////              }
//            unBlockDocumentMQService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "E", list.getPaymentBlock());
////              MassChangeDocument massChangeDocument =
////                  massChangeDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
////                      fiPostResponse.getCompCode(),
////                      fiPostResponse.getAccDocNo(),
////                      fiPostResponse.getFiscalYear());
////              if (null != massChangeDocument) {
////                massChangeDocument.setStatus("S");
////                massChangeDocumentService.save(massChangeDocument);
////              }
//            massChangeDocumentService.updateStatus(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), "E");
//
//            UnblockDocumentDetailLog unblockDocumentDetailLog = new UnblockDocumentDetailLog();
//            unblockDocumentDetailLog.setOriginalCompanyCode(fiPostResponse.getCompCode());
//            unblockDocumentDetailLog.setOriginalDocumentNo(fiPostResponse.getAccDocNo());
//            unblockDocumentDetailLog.setOriginalFiscalYear(fiPostResponse.getFiscalYear());
//            if (list.getRemark() != null && list.getRemark().length() > 4000) {
//              unblockDocumentDetailLog.setText(list.getRemark().substring(0, 3999));
//            } else {
//              unblockDocumentDetailLog.setText(list.getRemark());
//            }
//
//            unblockDocumentDetailLogService.save(unblockDocumentDetailLog);
//          }
//        } else {
////            UnBlockDocumentLog checkDuplicate =
////                unBlockDocumentLogService
////                    .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                        fiPostResponse.getCompCode(),
////                        fiPostResponse.getAccDocNo(),
////                        fiPostResponse.getFiscalYear());
////            if (null == checkDuplicate) {
////              UnBlockDocumentLog unBlockDocumentLog = new UnBlockDocumentLog();
////              unBlockDocumentLog.setValueNew("E");
////              unBlockDocumentLog.setIdemStatus("E");
////              unBlockDocumentLogService.save(unBlockDocumentLog);
////            } else {
////              checkDuplicate.setIdemStatus("E");
////              unBlockDocumentLogService.save(checkDuplicate);
////            }
//          unBlockDocumentLogService.updateStatus(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear(), "E",  "B");
////            UnBlockDocumentMQ unBlockDocumentMQOld =
////                unBlockDocumentMQService
////                    .findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
////                        fiPostResponse.getCompCode(),
////                        fiPostResponse.getAccDocNo(),
////                        fiPostResponse.getFiscalYear());
////            if (null != unBlockDocumentMQOld) {
////              unBlockDocumentMQOld.setIdemStatus("E");
////              unBlockDocumentMQService.save(unBlockDocumentMQOld);
////            }
//          unBlockDocumentMQService.updateStatus(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear(), "E","B");
////            MassChangeDocument massChangeDocument =
////                massChangeDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
////                    fiPostResponse.getCompCode(),
////                    fiPostResponse.getAccDocNo(),
////                    fiPostResponse.getFiscalYear());
////            if (null != massChangeDocument) {
////              massChangeDocument.setStatus("E");
////              massChangeDocumentService.save(massChangeDocument);
////            }
//          massChangeDocumentService.updateStatus(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear(), "E");
//
//          UnblockDocumentDetailLog unblockDocumentDetailLog = new UnblockDocumentDetailLog();
//          unblockDocumentDetailLog.setOriginalCompanyCode(fiPostResponse.getCompCode());
//          unblockDocumentDetailLog.setOriginalDocumentNo(fiPostResponse.getAccDocNo());
//          unblockDocumentDetailLog.setOriginalFiscalYear(fiPostResponse.getFiscalYear());
//          if (fiPostResponse.getRemark().length() > 4000) {
//            unblockDocumentDetailLog.setText(fiPostResponse.getRemark().substring(0, 3999));
//          } else {
//            unblockDocumentDetailLog.setText(fiPostResponse.getRemark());
//          }
//
//          unblockDocumentDetailLogService.save(unblockDocumentDetailLog);
//
//          MassChangeDocumentDetailLog massChangeDocumentDetailLog =
//                  new MassChangeDocumentDetailLog();
//          massChangeDocumentDetailLog.setCompanyCode(fiPostResponse.getCompCode());
//          massChangeDocumentDetailLog.setDocumentNo(fiPostResponse.getAccDocNo());
//          massChangeDocumentDetailLog.setFiscalYear(fiPostResponse.getFiscalYear());
//          if (fiPostResponse.getRemark().length() > 4000) {
//            massChangeDocumentDetailLog.setText(fiPostResponse.getRemark().substring(0, 3999));
//          } else {
//            massChangeDocumentDetailLog.setText(fiPostResponse.getRemark());
//          }
//
//          massChangeDocumentDetailLogService.save(massChangeDocumentDetailLog);
//
//          log.info("fiPostResponse getCompCode {}", fiPostResponse.getCompCode());
//          log.info("fiPostResponse getAccDocNo {}", fiPostResponse.getAccDocNo());
//          log.info("fiPostResponse getFiscalYear {}", fiPostResponse.getFiscalYear());
//
////            GLLine glLine =
////                glLineService.findOneUnBlockDocumentByCondition(
////                    fiPostResponse.getCompCode(),
////                    fiPostResponse.getAccDocNo(),
////                    fiPostResponse.getFiscalYear(),
////                    true);
////
////            if (glLine != null) {
////              glLine.setPaymentBlock("B");
////              glLineService.save(glLine);
////            }
//
//          glLineService.updateBlockStatusErrorCase(fiPostResponse.getCompCode(), fiPostResponse.getAccDocNo(), fiPostResponse.getFiscalYear());
//        }
//      }
//
//      log.info("Message unBlock AP.UpPbk out {}", System.currentTimeMillis());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

  @JmsListener(destination = "AP.Payment")
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void requestPayment(String message) {
    try {
      log.info("Message Ap Payment in {}", System.currentTimeMillis());
      log.info("message");
      log.info(message);
      log.info("Message received {} ", XMLUtil.xmlUnmarshall(FIMessage.class, message));
      log.info("xmlUnmarshall {}", XMLUtil.xmlUnmarshall(FIMessage.class, message));
      FIMessage fiMessage = XMLUtil.xmlUnmarshall(FIMessage.class, message);
      APPaymentResponse apPaymentResponse =
          XMLUtil.xmlUnmarshall(APPaymentResponse.class, fiMessage.getData());
      Timestamp updateDate = new Timestamp(System.currentTimeMillis());
      if (fiMessage.getDataType().equalsIgnoreCase("REV")) {
        if (apPaymentResponse.getStatus().equalsIgnoreCase("S")) {
          List<FICreateResponseBase> fiCreateResponseBaseList = apPaymentResponse.getAutoDoc();
          if (fiCreateResponseBaseList.size() > 0) {
            fiCreateResponseBaseList.forEach(
                (autoDoc) -> {
                  if ("S".equalsIgnoreCase(autoDoc.getStatus())) {
                    List<PaymentProcess> paymentProcesses =
                        this.paymentProcessService
                            .findAllByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(
                                autoDoc.getAccDocNo(),
                                autoDoc.getCompCode(),
                                autoDoc.getFiscalYear());
                    for (PaymentProcess paymentProcess : paymentProcesses) {
                      if (null != paymentProcess) {
                        log.info(" ===== {} ", paymentProcess.getInvoiceDocumentNo());

                        paymentProcess.setReversePaymentCompanyCode(autoDoc.getCompCode());
                        paymentProcess.setReversePaymentDocumentNo(autoDoc.getRevAccDocNo());
                        paymentProcess.setReversePaymentFiscalYear(autoDoc.getFiscalYear());
                        paymentProcess.setReversePaymentDocumentType(autoDoc.getRevDocType());
                        paymentProcess.setReversePaymentStatus("S");
                        paymentProcessService.save(paymentProcess);
                        glHeadService.updateGLHeadAfterReversePayment(paymentProcess.getOriginalCompanyCode(),
                            paymentProcess.getOriginalDocumentNo(),
                            paymentProcess.getOriginalFiscalYear(), paymentProcess.getUpdatedBy(), updateDate);
                        ProposalLog proposalLog =
                            this.proposalLogService
                                .findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(
                                    autoDoc.getAccDocNo(),
                                    autoDoc.getCompCode(),
                                    autoDoc.getFiscalYear());
                        log.info("Payment (payment) Prop log");
                        if (null != proposalLog) {
                          log.info("Payment Prop log autoDocItem FOUND!!!!");
                          log.info("Payment Prop log getRevAccDocNo {} ", autoDoc.getRevAccDocNo());
                          log.info("Payment Prop log getFiscalYear {} ", autoDoc.getFiscalYear());
                          proposalLog.setRevPaymentDocument(autoDoc.getRevAccDocNo());
                          proposalLog.setRevPaymentFiscalYear(autoDoc.getFiscalYear());
                          proposalLogService.save(proposalLog);
                          log.info("Payment Prop log SAVE SUCCESS ");
//                          ProposalReturnLog proposalReturnLog =
//                              this.proposalReturnLogService
//                                  .findOneByInvoiceDocumentAndPaymentDocument(proposalLog);
                          this.proposalReturnLogService.updateProposalReturnLogAfterStep3(proposalLog, autoDoc.getRevAccDocNo(), autoDoc.getFiscalYear());
//                          if (null != proposalReturnLog) {
//                            log.info("Payment Return autoDocItem FOUND!!!!");
//                            log.info("Payment Return getRevAccDocNo {} ", autoDoc.getRevAccDocNo());
//                            log.info("Payment Return getFiscalYear {} ", autoDoc.getFiscalYear());
//                            proposalReturnLog.setRevPaymentDocNo(autoDoc.getRevAccDocNo());
//                            proposalReturnLog.setRevPaymentFiscalYear(autoDoc.getFiscalYear());
//                            proposalReturnLogService.save(proposalReturnLog);
//                            log.info("Payment Return SAVE SUCCESS ");
//                          }
                        }
                      }
                    }

                  }
                  ReverseDocument reverseDocument =
                      reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                          autoDoc.getCompCode(), autoDoc.getAccDocNo(), autoDoc.getFiscalYear());
                  if (null != reverseDocument) {
                    reverseDocument.setReverseCompanyCode(autoDoc.getCompCode());
                    reverseDocument.setReverseDocumentNo(autoDoc.getRevAccDocNo());
                    reverseDocument.setReverseFiscalYear(autoDoc.getRevFiscalYear());
                    reverseDocument.setReverseDocumentType(autoDoc.getRevDocType());
                    reverseDocument.setStatus("S");
                    reverseDocumentService.save(reverseDocument);
                  }
                  ReturnReverseDocument returnReverseDocument =
                      returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                          autoDoc.getCompCode(), autoDoc.getAccDocNo(), autoDoc.getFiscalYear(), true);
                  if (null != returnReverseDocument) {
//                      returnReverseDocument.setReversePaymentCompanyCode(autoDoc.getCompCode());
//                      returnReverseDocument.setReversePaymentDocumentNo(autoDoc.getRevAccDocNo());
//                      returnReverseDocument.setReversePaymentFiscalYear(autoDoc.getRevFiscalYear());
//                      returnReverseDocument.setPaymentIdemStatus("S");
//                      returnReverseDocumentService.save(returnReverseDocument);
                    returnReverseDocumentService.updateReversePayment(autoDoc.getCompCode(), autoDoc.getRevAccDocNo(), autoDoc.getRevFiscalYear(), autoDoc.getCompCode(), autoDoc.getAccDocNo(), autoDoc.getFiscalYear());
                  }
//                  }
                  //call step4 auto
                  List<ProposalReturnLog> proposalReturnLogList = proposalReturnLogService.findOneReturnDocumentProposalReturnLog(autoDoc.getCompCode(), autoDoc.getAccDocNo(), autoDoc.getFiscalYear(), true);

                  for (ProposalReturnLog proposalReturnLog : proposalReturnLogList) {
                    if (null != proposalReturnLog && proposalReturnLog.isAutoStep3()) {
                      returnService.autoStep4ByMessageQueue(proposalReturnLog);
                    }
                  }

                });
          }

        } else {
          PaymentProcess paymentProcess =
              this.paymentProcessService
                  .findOneByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(
                      apPaymentResponse.getAccDocNo(),
                      apPaymentResponse.getCompCode(),
                      apPaymentResponse.getFiscalYear());
          if (paymentProcess != null) {
            paymentProcess.setReversePaymentStatus("E");
            paymentProcessService.save(paymentProcess);
          }
          ReverseDocument reverseDocument =
              reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                  apPaymentResponse.getCompCode(),
                  apPaymentResponse.getAccDocNo(),
                  apPaymentResponse.getFiscalYear());
          if (reverseDocument != null) {
            reverseDocument.setStatus("E");
            reverseDocumentService.save(reverseDocument);
          }
          ReturnReverseDocument returnReverseDocument =
              returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                  apPaymentResponse.getCompCode(), apPaymentResponse.getAccDocNo(), apPaymentResponse.getFiscalYear(), true);
          if (null != returnReverseDocument) {
            returnReverseDocument.setPaymentIdemStatus("E");
            returnReverseDocumentService.save(returnReverseDocument);
          }

          ReverseDocumentDetailLog reverseDocumentDetailLog = new ReverseDocumentDetailLog();
          reverseDocumentDetailLog.setCompanyCode(apPaymentResponse.getCompCode());
          reverseDocumentDetailLog.setDocumentNo(apPaymentResponse.getAccDocNo());
          reverseDocumentDetailLog.setFiscalYear(apPaymentResponse.getFiscalYear());
          //                    reverseDocumentDetailLog.setErrorCode(error.getCode());
          //                    reverseDocumentDetailLog.setLine(String.valueOf(error.getLine()));
          reverseDocumentDetailLog.setText(apPaymentResponse.getRemark());
          reverseDocumentDetailLogService.save(reverseDocumentDetailLog);
        }
      } else if (fiMessage.getDataType().equalsIgnoreCase("REINV")) {
        if (apPaymentResponse.getStatus().equalsIgnoreCase("S")) {
          List<FICreateResponseBase> fiCreateResponseBaseList = apPaymentResponse.getAutoDoc();
          for (FICreateResponseBase list : fiCreateResponseBaseList) {
            log.info("list {} ", list);


//            GLHead glHead =
//                glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
//                    list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
//
//            if (glHead != null) {
//              glHead.setReverseOriginalDocumentNo(list.getRevAccDocNo());
//              glHead.setReverseOriginalFiscalYear(list.getRevFiscalYear());
//              glHeadService.save(glHead);
//            }
            glHeadService.updateGLHeadAfterReverseInvoice(list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), list.getRevAccDocNo(), list.getRevFiscalYear(), updateDate, list.getDocStatus());

            ProposalLog proposalLog =
                this.proposalLogService
                    .findOneByOriginalDocNoAndOriginalCompCodeAndOriginalFiscalYear(
                        list.getAccDocNo(), list.getCompCode(), list.getFiscalYear());
            log.info("Payment Prop log (Invoice) proposalLog ");
            if (null != proposalLog) {
              log.info("Payment Prop log (Invoice) FOUND!!!  {} ", proposalLog.getId());
              log.info("Payment Prop log autoDocItem FOUND!!!!");
              log.info("Payment Prop log getRevAccDocNo {} ", list.getRevAccDocNo());
              log.info("Payment Prop log getFiscalYear {} ", list.getFiscalYear());
              proposalLog.setRevOriginalDocNo(list.getRevAccDocNo());
              proposalLog.setRevOriginalFiscalYear(list.getFiscalYear());
              proposalLog.setRevInvDocNo(list.getRevAccDocNo());
              proposalLog.setRevInvFiscalYear(list.getFiscalYear());
              proposalLogService.save(proposalLog);
              log.info("Payment Prop log (Invoice) SAVE SUCCESS ");
              if (!list.getDocType().equalsIgnoreCase("KY")) {
                ProposalReturnLog proposalReturnLog =
                    this.proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(
                        proposalLog);
                if (null != proposalReturnLog) {
                  log.info("Payment Return invoice autoDocItem FOUND!!!!");
                  log.info("Payment Return invoice getRevAccDocNo {} ", list.getRevAccDocNo());
                  log.info("Payment Return getFiscalYear {} ", list.getFiscalYear());
                  proposalReturnLog.setRevOriginalDocumentNo(list.getRevAccDocNo());
                  proposalReturnLog.setResetReverseFlag("X");
                  proposalReturnLog.setRevOriginalFiscalYear(list.getFiscalYear());
                  proposalReturnLogService.save(proposalReturnLog);
                  log.info("Payment Return invoice SAVE SUCCESS ");
                }
              }
            }
            //special case for step 4 child document
            if (list.getDocType().equalsIgnoreCase("KX") || list.getDocType().equalsIgnoreCase("K3")) {
              ProposalReturnLog proposalReturnLog =
                  this.proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(
                      list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), null, null, null);
              if (null != proposalReturnLog) {
                log.info("Payment Return invoice autoDocItem FOUND!!!!");
                log.info("Payment Return invoice getRevAccDocNo {} ", list.getRevAccDocNo());
                log.info("Payment Return getFiscalYear {} ", list.getFiscalYear());
                proposalReturnLog.setRevOriginalDocumentNo(list.getRevAccDocNo());
                proposalReturnLog.setResetReverseFlag("X");
                proposalReturnLog.setRevOriginalFiscalYear(list.getFiscalYear());
                proposalReturnLogService.save(proposalReturnLog);
                log.info("Payment Return invoice SAVE SUCCESS ");
              }
            }

            ReverseDocument reverseDocument =
                reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                    list.getCompCode(), list.getAccDocNo(), list.getFiscalYear());
            if (null != reverseDocument) {
              reverseDocument.setReverseCompanyCode(list.getCompCode());
              reverseDocument.setReverseDocumentNo(list.getRevAccDocNo());
              reverseDocument.setReverseFiscalYear(list.getRevFiscalYear());
              reverseDocument.setReverseDocumentType(list.getRevDocType());
              reverseDocument.setStatus("S");
              reverseDocumentService.save(reverseDocument);
            }
            ReturnReverseDocument returnReverseDocument =
                returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                    list.getCompCode(), list.getAccDocNo(), list.getFiscalYear(), false);
            if (null != returnReverseDocument) {
              returnReverseDocument.setReverseOriginalCompanyCode(list.getCompCode());
              returnReverseDocument.setReverseOriginalDocumentNo(list.getRevAccDocNo());
              returnReverseDocument.setReverseOriginalFiscalYear(list.getRevFiscalYear());
              returnReverseDocument.setOriginalIdemStatus("S");
              returnReverseDocumentService.save(returnReverseDocument);
            }
          }
        } else {
          ReverseDocument reverseDocument =
              reverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                  apPaymentResponse.getCompCode(),
                  apPaymentResponse.getAccDocNo(),
                  apPaymentResponse.getFiscalYear());
          if (null != reverseDocument) {
            reverseDocument.setStatus("E");
            reverseDocumentService.save(reverseDocument);
          }
          ReturnReverseDocument returnReverseDocument =
              returnReverseDocumentService.findOneByCompanyCodeAndDocumentNoAndFiscalYear(
                  apPaymentResponse.getCompCode(), apPaymentResponse.getAccDocNo(), apPaymentResponse.getFiscalYear(), false);
          if (null != returnReverseDocument) {
            returnReverseDocument.setOriginalIdemStatus("E");
            returnReverseDocumentService.save(returnReverseDocument);
          }

          ReverseDocumentDetailLog reverseDocumentDetailLog = new ReverseDocumentDetailLog();
          reverseDocumentDetailLog.setCompanyCode(apPaymentResponse.getCompCode());
          reverseDocumentDetailLog.setDocumentNo(apPaymentResponse.getAccDocNo());
          reverseDocumentDetailLog.setFiscalYear(apPaymentResponse.getFiscalYear());
          reverseDocumentDetailLog.setText(apPaymentResponse.getRemark());
          reverseDocumentDetailLogService.save(reverseDocumentDetailLog);
        }
      } else if (fiMessage.getDataType().equalsIgnoreCase("CREJU")) {
        APPaymentResponse test =
            XMLUtil.xmlUnmarshall(APPaymentResponse.class, fiMessage.getData());
        log.info(" create JU {}", fiMessage.getData());
        log.info(" create JU {}", test);
        log.info(" create JU {}", test.getAccDocNo());
        if (apPaymentResponse.getStatus().equalsIgnoreCase("S")) {
          juHeadService.updateJuDocument(apPaymentResponse, fiMessage.getId());
        } else {

        }
      } else {
        if (apPaymentResponse.getStatus().equalsIgnoreCase("S")) {
          log.info("updateSuccessPaymentInformationAfterGetPaymentDocNoByIdem {}", "");
          if (!apPaymentResponse.getCompCode().isEmpty()) {
            CompanyCode companyCode =
                companyCodeService.findOneByValueCode(apPaymentResponse.getCompCode());
            this.paymentProcessService.updatePaymentDocument(
                apPaymentResponse, companyCode.getName());
          } else {
            this.paymentProcessService.updatePaymentDocument(apPaymentResponse);
          }
          List<PaymentRealRunLog> paymentRealRunLog = new ArrayList<>();
          PaymentProcess paymentProcess =
              paymentProcessService.findOneByPmGroupDocAndPmGroupNoAndProposalFalse(
                  apPaymentResponse.getPmGroupDoc(), apPaymentResponse.getPmGroupNo());
          //
          // paymentAlias.setRunSuccessDocument(paymentAlias.getRunSuccessDocument() + 1);

          if (paymentProcess.getInvoiceDocumentType().equalsIgnoreCase("KC")
              || paymentProcess.getInvoiceDocumentType().equalsIgnoreCase("KB")
              || paymentProcess.getInvoiceDocumentType().equalsIgnoreCase("KG")
              || paymentProcess.getInvoiceDocumentType().equalsIgnoreCase("KA")) {
            PaymentProcess paymentProcessChild =
                paymentProcessService
                    .findOneByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalAndIsChild(
                        paymentProcess.getPaymentAliasId(),
                        paymentProcess.getInvoiceCompanyCode(),
                        paymentProcess.getInvoiceDocumentNo(),
                        paymentProcess.getInvoiceFiscalYear(),
                        false,
                        true);

            if (null != paymentProcessChild) {
              paymentProcessChild.setPaymentDocumentNo(apPaymentResponse.getAccDocNo());
              paymentProcessChild.setPaymentCompanyCode(apPaymentResponse.getCompCode());
              paymentProcessChild.setPaymentFiscalYear(apPaymentResponse.getFiscalYear());
              paymentProcessService.save(paymentProcessChild);
            }
          }

          //                    AtomicLong paymentRealRunLogId = new
          // AtomicLong(this.paymentRealRunLogService.getNextSeries());
          //                    AtomicInteger seq = new
          // AtomicInteger(paymentRealRunLogService.getSequenceByPaymentAliasId(paymentProcess.getPaymentAliasId()).intValue() + 1);
          //                    AtomicInteger seq = new AtomicInteger(5);

          Long seq =
              commonSequenceService.getCurrentSeq("LOG_SEQ", apPaymentResponse.getPmGroupNo());

          this.paymentRealRunLogService.addSuccessLog(
              this.paymentRealRunLogService.getNextSeries(),
              seq,
              MessageFormat.format(
                  MessageRealRun.MessageSuccess.getMessageText(),
                  paymentProcess.getInvoiceDocumentNo(),
                  paymentProcess.getInvoiceFiscalYear(),
                  paymentProcess.getInvoiceCompanyCode(),
                  apPaymentResponse.getAccDocNo(),
                  apPaymentResponse.getFiscalYear(),
                  apPaymentResponse.getCompCode()),
              paymentProcess.getPaymentAliasId(),
              paymentRealRunLog);

          this.paymentRealRunLogService.saveBatch(paymentRealRunLog);
          //
          // this.paymentRealRunLogService.updateNextSeries(Long.parseLong(paymentRealRunLogId.toString()));

          //
          // updateSuccessPaymentInformationAfterGetPaymentDocNoByIdem(apPaymentResponse);
          //
          // updateSuccessPaymentProcessAfterGetPaymentDocNoByIdem(apPaymentResponse);
          //                    PaymentInformation paymentInformation =
          // paymentInformationService.findOneByPmGroupDocAndPmGroupNoAndProposalFalse(apPaymentResponse.getPmGroupDoc(), apPaymentResponse.getPmGroupNo());
          //                    if (paymentInformation != null) {
          //
          // paymentInformation.setPaymentDocNo(apPaymentResponse.getAccDocNo());
          //
          // paymentInformation.setPaymentCompCode(apPaymentResponse.getCompCode());
          //
          // paymentInformation.setPaymentFiscalYear(apPaymentResponse.getFiscalYear());
          //                        paymentInformation.setIdemStatus("S");
          //                        paymentInformationService.save(paymentInformation);
          //                    }
          //                    PaymentProcess paymentProcess =
          // paymentProcessService.findOneByPmGroupDocAndPmGroupNoAndProposalFalse(apPaymentResponse.getPmGroupDoc(), apPaymentResponse.getPmGroupNo());
          //                    if (paymentProcess != null) {
          //                        paymentProcess.setPaymentDocNo(apPaymentResponse.getAccDocNo());
          //
          // paymentProcess.setPaymentCompCode(apPaymentResponse.getCompCode());
          //
          // paymentProcess.setPaymentFiscalYear(apPaymentResponse.getFiscalYear());
          //                        paymentProcess.setIdemStatus("S");
          //                        paymentInformationService.save(paymentInformation);
          //                    }
        } else {
          log.info(" run payment Error {}", fiMessage);
          log.info(" run payment Error {}", apPaymentResponse);
          PaymentProcess paymentProcess = null;
          if (null == apPaymentResponse.getPmGroupDoc()
              || null == apPaymentResponse.getPmGroupNo()) {
            paymentProcess =
                paymentProcessService.findOneByPmGroupDocAndPmGroupNoAndProposalFalse(
                    fiMessage.getId(), "");

          } else {
            paymentProcess =
                paymentProcessService.findOneByPmGroupDocAndPmGroupNoAndProposalFalse(
                    apPaymentResponse.getPmGroupDoc(), apPaymentResponse.getPmGroupNo());
          }

          if (paymentProcess != null) {
            paymentProcess.setIdemStatus(apPaymentResponse.getStatus());
            paymentProcess.setIdemUpdate(new Timestamp(System.currentTimeMillis()));
            paymentProcessService.save(paymentProcess);
          }
          if (paymentProcess != null) {
            List<BaseErrorLine> listError = apPaymentResponse.getErrors();
            List<PaymentRealRunLog> paymentRealRunLog = new ArrayList<>();
            AtomicLong paymentRealRunLogId =
                new AtomicLong(this.paymentRealRunLogService.getNextSeries());

            Long seq =
                commonSequenceService.getCurrentSeq(
                    "LOG_SEQ", apPaymentResponse.getPmGroupNo(), 20);

            this.paymentRealRunLogService.addErrorLog(
                this.paymentRealRunLogService.getNextSeries(),
                seq++,
                MessageFormat.format(MessageRealRun.MessageErrorStep.getMessageText(), ""),
                paymentProcess.getPaymentAliasId(),
                paymentRealRunLog);
            this.paymentRealRunLogService.addErrorLog(
                this.paymentRealRunLogService.getNextSeries(),
                seq++,
                MessageFormat.format(
                    MessageRealRun.MessageErrorStep1.getMessageText(),
                    paymentProcess.getInvoiceDocumentNo(),
                    paymentProcess.getInvoiceFiscalYear(),
                    paymentProcess.getInvoiceCompanyCode()),
                paymentProcess.getPaymentAliasId(),
                paymentRealRunLog);
            this.paymentRealRunLogService.addErrorLog(
                this.paymentRealRunLogService.getNextSeries(),
                seq++,
                MessageFormat.format(MessageRealRun.MessageErrorStep2.getMessageText(), ""),
                paymentProcess.getPaymentAliasId(),
                paymentRealRunLog);

            for (BaseErrorLine error : listError) {
              PaymentRunErrorDocumentDetailLog paymentRunErrorDocumentDetailLog =
                  new PaymentRunErrorDocumentDetailLog();
              paymentRunErrorDocumentDetailLog.setInvoiceCompanyCode(
                  paymentProcess.getInvoiceCompanyCode());
              paymentRunErrorDocumentDetailLog.setInvoiceDocumentNo(
                  paymentProcess.getInvoiceDocumentNo());
              paymentRunErrorDocumentDetailLog.setInvoiceFiscalYear(
                  paymentProcess.getInvoiceFiscalYear());
              paymentRunErrorDocumentDetailLog.setPmGroupDoc(paymentProcess.getPmGroupDoc());
              paymentRunErrorDocumentDetailLog.setPmGroupNo(paymentProcess.getPmGroupNo());
              paymentRunErrorDocumentDetailLog.setErrorCode(error.getCode());
              paymentRunErrorDocumentDetailLog.setLine(String.valueOf(error.getLine()));
              paymentRunErrorDocumentDetailLog.setText(error.getText());
              paymentRunErrorDocumentDetailLog.setPaymentAliasId(
                  paymentProcess.getPaymentAliasId());
              paymentRunErrorDocumentDetailLogService.save(paymentRunErrorDocumentDetailLog);

              String errorText = error.getLine() + " " + error.getCode() + " " + error.getText();
              this.paymentRealRunLogService.addErrorLog(
                  this.paymentRealRunLogService.getNextSeries(),
                  seq++,
                  errorText,
                  paymentProcess.getPaymentAliasId(),
                  paymentRealRunLog);
            }
            this.paymentRealRunLogService.addErrorLog(
                this.paymentRealRunLogService.getNextSeries(),
                seq++,
                MessageFormat.format(MessageRealRun.MessageErrorStep.getMessageText(), ""),
                paymentProcess.getPaymentAliasId(),
                paymentRealRunLog);
            this.paymentRealRunLogService.saveBatch(paymentRealRunLog);
            this.paymentRealRunLogService.updateNextSeries(
                Long.parseLong(paymentRealRunLogId.toString()));
          }
        }
      }
      log.info("Message Ap Payment out {}", System.currentTimeMillis());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //    public void updateSuccessPaymentInformationAfterGetPaymentDocNoByIdem(APPaymentResponse
  // apPaymentResponse) {
  //        String sql = "UPDATE PAYMENT_INFORMATION SET PAYMENT_DOC_NO = ?, PAYMENT_COMP_CODE = ?,
  // PAYMENT_FISCAL_YEAR = ?, IDEM_STATUS = ? WHERE PM_GROUP_DOC = ? AND PM_GROUP_NO = ? AND
  // PROPOSAL = ?";
  //
  //        DBConnection.getJdbcTemplate("payment").update(sql, apPaymentResponse.getAccDocNo(),
  // apPaymentResponse.getCompCode(), apPaymentResponse.getFiscalYear(), "S",
  // apPaymentResponse.getPmGroupDoc(), apPaymentResponse.getPmGroupNo(), "0");
  //    }
  //
  //    public void updateSuccessPaymentProcessAfterGetPaymentDocNoByIdem(APPaymentResponse
  // apPaymentResponse) {
  //        String sql = "UPDATE PAYMENT_PROCESS SET PAYMENT_DOC_NO = ?, PAYMENT_COMP_CODE = ?,
  // PAYMENT_FISCAL_YEAR = ?, IDEM_STATUS = ? WHERE PM_GROUP_DOC = ? AND PM_GROUP_NO = ? AND
  // PROPOSAL = ?";
  //
  //        DBConnection.getJdbcTemplate("payment").update(sql, apPaymentResponse.getAccDocNo(),
  // apPaymentResponse.getCompCode(), apPaymentResponse.getFiscalYear(), "S",
  // apPaymentResponse.getPmGroupDoc(), apPaymentResponse.getPmGroupNo(), "0");
  //    }

}

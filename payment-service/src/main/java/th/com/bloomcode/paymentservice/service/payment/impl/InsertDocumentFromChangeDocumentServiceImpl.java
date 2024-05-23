package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.GLLine;
import th.com.bloomcode.paymentservice.service.idem.AreaService;
import th.com.bloomcode.paymentservice.service.idem.CompanyCodeService;
import th.com.bloomcode.paymentservice.service.idem.PaymentCenterService;
import th.com.bloomcode.paymentservice.service.idem.VendorService;
import th.com.bloomcode.paymentservice.service.master.FundSourceService;
import th.com.bloomcode.paymentservice.service.master.VendorBankAccountService;
import th.com.bloomcode.paymentservice.service.payment.GLHeadService;
import th.com.bloomcode.paymentservice.service.payment.GLLineService;
import th.com.bloomcode.paymentservice.service.payment.InsertDocumentFromChangeDocumentService;
import th.com.bloomcode.paymentservice.webservice.model.ZFIDoc;
import th.com.bloomcode.paymentservice.webservice.model.ZFIHeader;
import th.com.bloomcode.paymentservice.webservice.model.ZFILine;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class InsertDocumentFromChangeDocumentServiceImpl
    implements InsertDocumentFromChangeDocumentService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final CompanyCodeService companyCodeService;

  private final AreaService areaService;
  private final FundSourceService fundSourceService;
  private final VendorService vendorService;
  private final VendorBankAccountService vendorBankAccountService;
  private final PaymentCenterService paymentCenterService;
  private final GLHeadService glHeadService;
  private final GLLineService glLineService;

  public InsertDocumentFromChangeDocumentServiceImpl(
      CompanyCodeService companyCodeService,
      AreaService areaService,
      FundSourceService fundSourceService,
      VendorService vendorService,
      VendorBankAccountService vendorBankAccountService,
      PaymentCenterService paymentCenterService,
      GLHeadService glHeadService,
      GLLineService glLineService) {

    this.companyCodeService = companyCodeService;
    this.areaService = areaService;
    this.fundSourceService = fundSourceService;
    this.vendorService = vendorService;
    this.vendorBankAccountService = vendorBankAccountService;
    this.paymentCenterService = paymentCenterService;
    this.glHeadService = glHeadService;
    this.glLineService = glLineService;
  }

  @Override
  public void insertDocument(ZFIDoc zfiDoc, String username, Timestamp updateDate) {
    try {

      ZFIHeader zfiHeader = zfiDoc.getHeader();
      log.info("insertDocument {}", zfiDoc);
      List<ZFILine> zfiLines = zfiDoc.getLines();
      GLHead fixGlHead =
              glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
              zfiHeader.getCompCode(), zfiHeader.getAccDocNo(), zfiHeader.getFiscalYear());

      if (fixGlHead == null && zfiHeader.getDocStatus().equalsIgnoreCase("CO")) {
        GLHead glHead = new GLHead(zfiHeader);
        glHead.setCurrency("THB");
        glHead.setUpdated(updateDate);
        glHead.setUpdatedBy(username);
//        if (null != glHead.getCompanyCode() && !glHead.getCompanyCode().isEmpty()) {
//          CompanyCode companyCode = companyCodeService.findOneByValueCode(glHead.getCompanyCode());
//          glHead.setCompanyName(companyCode.getName());
//        }
        glHead = glHeadService.save(glHead);
        for (ZFILine zfiLine : zfiLines) {
          GLLine glLine = new GLLine(zfiLine, glHead);
          glLine.setUpdatedBy(username);
          glLine.setUpdated(updateDate);
//          if (glLine.getVendor() != null && !glLine.getVendor().isEmpty()) {
//            Vendor vendor = vendorService.findOne(glLine.getVendor());
//            glLine.setVendorName(vendor.getName());
//            glLine.setConfirmVendor(vendor.getConfirmStatus().equalsIgnoreCase("Y"));
//            if (null != glLine.getPayee() && !glLine.getPayee().isEmpty()) {
//              Vendor payee = vendorService.findOne(glLine.getPayee());
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
        glHead.setUpdated(updateDate);
        glHead.setUpdatedBy(username);

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
            glLine.setUpdatedBy(username);
            glLine.setUpdated(updateDate);
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
            log.info("update line {}");
            Long glLineId = oldGLline.getId();
            GLLine glLine = new GLLine(zfiLine, fixGlHead);
            glLine.setUpdatedBy(username);
            glLine.setUpdated(updateDate);
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
        log.info(" Insert document by upbk {}", "");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

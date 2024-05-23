package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

//@SqlResultSetMapping(name = "PaymentDocumentMapping", classes = @ConstructorResult(targetClass = PaymentDocument.class, columns = {
//        @ColumnResult(name = "DOC_TYPE", type = String.class),
//        @ColumnResult(name = "COMP_CODE", type = String.class),
//        @ColumnResult(name = "DATE_DOC", type = Timestamp.class),
//        @ColumnResult(name = "DATE_ACCT", type = Timestamp.class),
//        @ColumnResult(name = "PERIOD", type = Integer.class),
//        @ColumnResult(name = "CURRENCY", type = String.class),
//        @ColumnResult(name = "INV_DOC_NO", type = String.class),
//        @ColumnResult(name = "REV_INV_DOC_NO", type = String.class),
//        @ColumnResult(name = "ACC_DOC_NO", type = String.class),
//        @ColumnResult(name = "REV_ACC_DOC_NO", type = String.class),
//        @ColumnResult(name = "FISCAL_YEAR", type = String.class),
//        @ColumnResult(name = "REV_FISCAL_YEAR", type = String.class),
//        @ColumnResult(name = "COST_CENTER1", type = String.class),
//        @ColumnResult(name = "COST_CENTER2", type = String.class),
//        @ColumnResult(name = "HD_REFERENCE", type = String.class),
//        @ColumnResult(name = "DOC_HEADER_TEXT", type = String.class),
////        @ColumnResult(name = "HEADER_DESC"),
//        @ColumnResult(name = "HD_REFERENCE2", type = String.class),
//        @ColumnResult(name = "REV_DATE_ACCT", type = Timestamp.class),
//        @ColumnResult(name = "REV_REASON", type = String.class),
//        @ColumnResult(name = "ORIGINAL_DOC_NO", type = String.class),
////        @ColumnResult(name = "HD_REF_LOG"),
//        @ColumnResult(name = "CREATED", type = Timestamp.class),
//        @ColumnResult(name = "USER_PARK", type = String.class),
//        @ColumnResult(name = "USER_POST", type = String.class),
//        @ColumnResult(name = "POSTING_KEY", type = String.class),
//        @ColumnResult(name = "ACCOUNT_TYPE", type = String.class),
//        @ColumnResult(name = "DR_CR", type = String.class),
//        @ColumnResult(name = "GL_ACCOUNT", type = String.class),
//        @ColumnResult(name = "FI_AREA", type = String.class),
//        @ColumnResult(name = "COST_CENTER", type = String.class),
//        @ColumnResult(name = "FUND_SOURCE", type = String.class),
//        @ColumnResult(name = "BG_CODE", type = String.class),
//        @ColumnResult(name = "BG_ACTIVITY", type = String.class),
//        @ColumnResult(name = "COST_ACTIVITY", type = String.class),
//        @ColumnResult(name = "AMOUNT", type = String.class),
//        @ColumnResult(name = "REFERENCE3", type = String.class),
//        @ColumnResult(name = "ASSIGNMENT", type = String.class),
//        @ColumnResult(name = "BR_DOC_NO", type = String.class),
//        @ColumnResult(name = "BR_LINE", type = Integer.class),
//        @ColumnResult(name = "PAYMENT_CENTER", type = String.class),
//        @ColumnResult(name = "BANK_BOOK", type = String.class),
//        @ColumnResult(name = "SUB_ACCOUNT", type = String.class),
//        @ColumnResult(name = "SUB_ACCOUNT_OWNER", type = String.class),
//        @ColumnResult(name = "DEPOSIT_ACCOUNT", type = String.class),
//        @ColumnResult(name = "DEPOSIT_ACCOUNT_OWNER", type = String.class),
//        @ColumnResult(name = "GPSC", type = String.class),
//        @ColumnResult(name = "GPSC_GROUP", type = String.class),
//        @ColumnResult(name = "LINE_ITEM_TEXT", type = String.class),
//        @ColumnResult(name = "LINE_DESC", type = String.class),
//        @ColumnResult(name = "PAYMENT_TERM", type = String.class),
//        @ColumnResult(name = "PAYMENT_METHOD", type = String.class),
//        @ColumnResult(name = "WTX_TYPE", type = String.class),
//        @ColumnResult(name = "WTX_CODE", type = String.class),
//        @ColumnResult(name = "WTX_BASE", type = String.class),
//        @ColumnResult(name = "WTX_AMOUNT", type = String.class),
//        @ColumnResult(name = "WTX_TYPE_P", type = String.class),
//        @ColumnResult(name = "WTX_CODE_P", type = String.class),
//        @ColumnResult(name = "WTX_BASE_P", type = String.class),
//        @ColumnResult(name = "WTX_AMOUNT_P", type = String.class),
//        @ColumnResult(name = "VENDOR", type = String.class),
//        @ColumnResult(name = "VENDOR_TAX_ID", type = String.class),
//        @ColumnResult(name = "PAYEE_CODE", type = String.class),
//        @ColumnResult(name = "PAYEE_TAX_ID", type = String.class),
//        @ColumnResult(name = "BANK_ACCOUNT_NO", type = String.class),
//        @ColumnResult(name = "BANK_BRANCH_NO", type = String.class),
//        @ColumnResult(name = "TRADING_PARTNER", type = String.class),
//        @ColumnResult(name = "TRADING_PARTNER_PARK", type = String.class),
//        @ColumnResult(name = "SPECIAL_GL", type = String.class),
//        @ColumnResult(name = "CN_DOC_NO", type = String.class),
//        @ColumnResult(name = "CN_FISCAL_YEAR", type = String.class),
//        @ColumnResult(name = "DATE_BASELINE", type = Timestamp.class),
//        @ColumnResult(name = "DATE_VALUE", type = Timestamp.class),
//        @ColumnResult(name = "ASSET_NO", type = String.class),
//        @ColumnResult(name = "ASSET_SUB_NO", type = String.class),
//        @ColumnResult(name = "QTY", type = String.class),
//        @ColumnResult(name = "UOM", type = String.class),
//        @ColumnResult(name = "REFERENCE1", type = String.class),
//        @ColumnResult(name = "REFERENCE2", type = String.class),
//        @ColumnResult(name = "PO_DOC_NO", type = String.class),
//        @ColumnResult(name = "PO_LINE", type = Integer.class),
//        @ColumnResult(name = "INCOME", type = String.class),
//        @ColumnResult(name = "PAYMENT_BLOCK", type = String.class),
//        @ColumnResult(name = "PAYMENT_REF", type = String.class),
//        @ColumnResult(name = "LINE_NO", type = Integer.class),
//        @ColumnResult(name = "COMP_CODE_NAME", type = String.class),
//        @ColumnResult(name = "PAYMENT_CENTER_NAME", type = String.class),
//        @ColumnResult(name = "COST_CENTER_NAME", type = String.class),
//        @ColumnResult(name = "PAYMENT_METHOD_NAME", type = String.class),
//        @ColumnResult(name = "FUND_SOURCE_NAME", type = String.class),
//        @ColumnResult(name = "BG_CODE_NAME", type = String.class),
//        @ColumnResult(name = "BG_ACTIVITY_NAME", type = String.class),
//        @ColumnResult(name = "NAME1", type = String.class),
//        @ColumnResult(name = "NAME2", type = String.class),
//        @ColumnResult(name = "TAX_ID", type = String.class),
//        @ColumnResult(name = "ADDRESS1", type = String.class),
//        @ColumnResult(name = "ADDRESS2", type = String.class),
//        @ColumnResult(name = "ADDRESS3", type = String.class),
//        @ColumnResult(name = "ADDRESS4", type = String.class),
//        @ColumnResult(name = "ADDRESS5", type = String.class),
//        @ColumnResult(name = "CITY", type = String.class),
//        @ColumnResult(name = "POSTAL", type = String.class),
//        @ColumnResult(name = "REGION_NAME", type = String.class),
//        @ColumnResult(name = "COUNTRY", type = String.class),
//        @ColumnResult(name = "COUNTRY_CODE", type = String.class),
//        @ColumnResult(name = "PAYEE_BANK_ACCOUNT_TYPE", type = String.class),
//        @ColumnResult(name = "PAYEE_BANK_NO", type = String.class),
//        @ColumnResult(name = "PAYEE_BANK_ACCOUNT_NO", type = String.class),
//        @ColumnResult(name = "ACCOUNT_HOLDER_NAME", type = String.class),
//        @ColumnResult(name = "PAYEE_BANK_NAME", type = String.class),
//        @ColumnResult(name = "PAYEE_BANK_KEY", type = String.class),
//        @ColumnResult(name = "SWIFT_CODE", type = String.class),
//        @ColumnResult(name = "PAYEE_BANK_REFERENCE", type = String.class)
//})

//)
@Entity
@Data
public class PaymentDocument {

    @Id
    private Long id;

//    @Column(name = "MAP_INV_DOC_NO")
//    private String mapInvDocNo;
//
//    @Column(name = "MAP_INV_FISCAL_YEAR")
//    private String mapInvFiscalYear;

    @Column(name = "DOC_TYPE")
    private String docType;

    @Column(name = "COMP_CODE")
    private String compCode;

    @Column(name = "DATE_DOC")
    private Timestamp dateDoc;

    @Column(name = "DATE_ACCT")
    private Timestamp dateAcct;

    @Column(name = "PERIOD")
    private int period;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "INV_DOC_NO")
    private String invDocNo;

    @Column(name = "REV_INV_DOC_NO")
    private String revInvDocNo;

    @Column(name = "ACC_DOC_NO")
    private String accDocNo;

    @Column(name = "REV_ACC_DOC_NO")
    private String revAccDocNo;

    @Column(name = "FISCAL_YEAR")
    private String fiscalYear;

    @Column(name = "REV_FISCAL_YEAR")
    private String revFiscalYear;

    @Column(name = "COST_CENTER1")
    private String costCenter1;

    @Column(name = "COST_CENTER2")
    private String costCenter2;

    @Column(name = "HD_REFERENCE")
    private String hdReference;

    @Column(name = "DOC_HEADER_TEXT")
    private String docHeaderText;
//  private String headerDesc;

    @Column(name = "HD_REFERENCE2")
    private String hdReference2;

    @Column(name = "REV_DATE_ACCT")
    private Timestamp revDateAcct;

    @Column(name = "REV_REASON")
    private String revReason;

    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;
//  private String hdRefLog;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "USER_PARK")
    private String userPark;

    @Column(name = "USER_POST")
    private String userPost;

    @Column(name = "POSTING_KEY")
    private String postingKey;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "DR_CR")
    private String drCr;

    @Column(name = "GL_ACCOUNT")
    private String glAccount;

    @Column(name = "FI_AREA")
    private String fiArea;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "FUND_SOURCE")
    private String fundSource;

    @Column(name = "BG_CODE")
    private String bgCode;

    @Column(name = "BG_ACTIVITY")
    private String bgActivity;

    @Column(name = "COST_ACTIVITY")
    private String costActivity;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "REFERENCE3")
    private String reference3;

    @Column(name = "ASSIGNMENT")
    private String assignment;

    @Column(name = "BR_DOC_NO")
    private String brDocNo;

    @Column(name = "BR_LINE")
    private int brLine;

    @Column(name = "PAYMENT_CENTER")
    private String paymentCenter;

    @Column(name = "BANK_BOOK")
    private String bankBook;

    @Column(name = "SUB_ACCOUNT")
    private String subAccount;

    @Column(name = "SUB_ACCOUNT_OWNER")
    private String subAccountOwner;

    @Column(name = "DEPOSIT_ACCOUNT")
    private String depositAccount;

    @Column(name = "DEPOSIT_ACCOUNT_OWNER")
    private String depositAccountOwner;

    @Column(name = "GPSC")
    private String gpsc;

    @Column(name = "GPSC_GROUP")
    private String gpscGroup;

    @Column(name = "LINE_ITEM_TEXT")
    private String lineItemText;

    @Column(name = "LINE_DESC")
    private String lineDesc;

    @Column(name = "PAYMENT_TERM")
    private String paymentTerm;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "WTX_TYPE")
    private String wtxType;

    @Column(name = "WTX_CODE")
    private String wtxCode;

    @Column(name = "WTX_BASE")
    private BigDecimal wtxBase;

    @Column(name = "WTX_AMOUNT")
    private BigDecimal wtxAmount;

    @Column(name = "WTX_TYPE_P")
    private String wtxTypeP;

    @Column(name = "WTX_CODE_P")
    private String wtxCodeP;

    @Column(name = "WTX_BASE_P")
    private BigDecimal wtxBaseP;

    @Column(name = "WTX_AMOUNT_P")
    private BigDecimal wtxAmountP;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "VENDOR_TAX_ID")
    private String vendorTaxId;

    @Column(name = "PAYEE_CODE")
    private String payee;

    @Column(name = "PAYEE_TAX_ID")
    private String payeeTaxId;

    @Column(name = "BANK_ACCOUNT_NO")
    private String bankAccountNo;

    @Column(name = "BANK_BRANCH_NO")
    private String bankBranchNo;

    @Column(name = "TRADING_PARTNER")
    private String tradingPartner;

    @Column(name = "TRADING_PARTNER_PARK")
    private String tradingPartnerPark;

    @Column(name = "SPECIAL_GL")
    private String specialGL;

    @Column(name = "DATE_BASELINE")
    private Timestamp dateBaseline;

    @Column(name = "DATE_VALUE")
    private Timestamp dateValue;

    @Column(name = "ASSET_NO")
    private String assetNo;

    @Column(name = "ASSET_SUB_NO")
    private String assetSubNo;

    @Column(name = "QTY")
    private BigDecimal qty;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "REFERENCE1")
    private String reference1;

    @Column(name = "REFERENCE2")
    private String reference2;

    @Column(name = "PO_DOC_NO")
    private String poDocNo;

    @Column(name = "PO_LINE")
    private int poLine;

    @Column(name = "INCOME")
    private String income;

    @Column(name = "PAYMENT_BLOCK")
    private String paymentBlock;

    @Column(name = "PAYMENT_REF")
    private String paymentRef;

    @Column(name = "LINE_NO")
    private int lineNo;

    @Column(name = "COMP_CODE_NAME")
    private String compCodeName;

    @Column(name = "PAYMENT_CENTER_NAME")
    private String paymentCenterName;

    @Column(name = "COST_CENTER_NAME")
    private String costCenterName;

    @Column(name = "PAYMENT_METHOD_NAME")
    private String paymentMethodName;

    @Column(name = "FUND_SOURCE_NAME")
    private String fundSourceName;

    @Column(name = "BG_CODE_NAME")
    private String bgCodeName;

    @Column(name = "BG_ACTIVITY_NAME")
    private String bgActivityName;

    @Column(name = "NAME1")
    private String name1;

    @Column(name = "NAME2")
    private String name2;

    @Column(name = "TAX_ID")
    private String taxId;

    @Column(name = "ADDRESS1")
    private String address1;

    @Column(name = "ADDRESS2")
    private String address2;

    @Column(name = "ADDRESS3")
    private String address3;

    @Column(name = "ADDRESS4")
    private String address4;

    @Column(name = "ADDRESS5")
    private String address5;

    @Column(name = "CITY")
    private String city;

    @Column(name = "POSTAL")
    private String postal;

    @Column(name = "REGION_NAME")
    private String regionName;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "PAYEE_BANK_ACCOUNT_TYPE")
    private String payeeBankAccountType;

    @Column(name = "PAYEE_BANK_NO")
    private String payeeBankNo;

    @Column(name = "PAYEE_BANK_ACCOUNT_NO")
    private String payeeBankAccountNo;

    @Column(name = "ACCOUNT_HOLDER_NAME")
    private String accountHolderName;

    @Column(name = "PAYEE_BANK_NAME")
    private String payeeBankName;

    @Column(name = "PAYEE_BANK_KEY")
    private String payeeBankKey;

    @Column(name = "SWIFT_CODE")
    private String swiftCode;

    @Column(name = "PAYEE_BANK_REFERENCE")
    private String payeeBankReference;

    @Column(name = "AREA_NAME")
    private String areaName;

//  private Timestamp paymentClearingDate;
//  private Timestamp paymentClearingEntryDate;
//  private String paymentClearingDocNo;
//  private String paymentClearingYear;
//  private Long paymentId;
//  private Date paymentDate;
//  private String paymentName;
//  private Timestamp paymentDateAcct;
//  private String houseBank;
//  private String payingCompCode;
//  private String payingCompCodeName;
//  private String payingBankCode;
//  private String payingHouseBank;
//  private String payingBankAccountNo;
//  private String payingBankCountry;
//  private String payingBankNo;
//  private String payingGLAccount;
//  private String payingBankKey;
//  private BigDecimal amountPaid;
//
//  private String status = "S";
//  private String errorCode;

    public PaymentDocument() {
    }

//  public PaymentDocument(String docType, String compCode, Timestamp dateDoc, Timestamp dateAcct, int period, String currency,
//                         String invDocNo, String revInvDocNo, String accDocNo, String revAccDocNo, String fiscalYear,
//                         String revFiscalYear, String costCenter1, String costCenter2, String hdReference,
//                         String docHeaderText, String hdReference2, Timestamp revDateAcct,
//                         String revReason, String originalDocNo,  Timestamp created, String userPark,
//                         String userPost, String postingKey, String accountType, String drCr, String glAccount,
//                         String fiArea, String costCenter, String fundSource, String bgCode, String bgActivity,
//                         String costActivity, BigDecimal amount, String reference3, String assignment, String brDocNo,
//                         int brLine, String paymentCenter, String bankBook, String subAccount, String subAccountOwner,
//                         String depositAccount, String depositAccountOwner, String gpsc, String gpscGroup,
//                         String lineItemText, String lineDesc, String paymentTerm, String paymentMethod, String wtxType,
//                         String wtxCode, BigDecimal wtxBase, BigDecimal wtxAmount, String wtxTypeP, String wtxCodeP,
//                         BigDecimal wtxBaseP, BigDecimal wtxAmountP, String vendor, String vendorTaxId, String payee,
//                         String payeeTaxId, String bankAccountNo, String bankBranchNo, String tradingPartner,
//                         String tradingPartnerPark, String specialGL, String cnDocNo, String cnFiscalYear,
//                         Timestamp dateBaseline, Timestamp dateValue, String assetNo, String assetSubNo, BigDecimal qty,
//                         String uom, String reference1, String reference2, String poDocNo, int poLine, String income,
//                         String paymentBlock, String paymentRef, int lineNo, String compCodeName, String paymentCenterName,
//                         String costCenterName, String paymentMethodName, String fundSourceName, String bgCodeName,
//                         String bgActivityName, String name1, String name2, String taxId, String address1,
//                         String address2, String address3, String address4, String address5, String city, String postal,
//                         String regionName, String country, String countryCode, String payeeBankAccountType,
//                         String payeeBankNo, String payeeBankAccountNo, String accountHolderName, String payeeBankName,
//                         String payeeBankKey, String swiftCode, String payeeBankReference
//  ) {
//    this.docType = docType;
//    this.compCode = compCode;
//    this.dateDoc = dateDoc;
//    this.dateAcct = dateAcct;
//    this.period = period;
//    this.currency = currency;
//    this.invDocNo = invDocNo;
//    this.revInvDocNo = revInvDocNo;
//    this.accDocNo = accDocNo;
//    this.revAccDocNo = revAccDocNo;
//    this.fiscalYear = fiscalYear;
//    this.revFiscalYear = revFiscalYear;
//    this.costCenter1 = costCenter1;
//    this.costCenter2 = costCenter2;
//    this.hdReference = hdReference;
//    this.docHeaderText = docHeaderText;
////    this.headerDesc = headerDesc;
//    this.hdReference2 = hdReference2;
//    this.revDateAcct = revDateAcct;
//    this.revReason = revReason;
//    this.originalDocNo = originalDocNo;
////    this.hdRefLog = hdRefLog;
//    this.created = created;
//    this.userPark = userPark;
//    this.userPost = userPost;
//    this.postingKey = postingKey;
//    this.accountType = accountType;
//    this.drCr = drCr;
//    this.glAccount = glAccount;
//    this.fiArea = fiArea;
//    this.costCenter = costCenter;
//    this.fundSource = fundSource;
//    this.bgCode = bgCode;
//    this.bgActivity = bgActivity;
//    this.costActivity = costActivity;
//    this.amount = amount;
//    this.reference3 = reference3;
//    this.assignment = assignment;
//    this.brDocNo = brDocNo;
//    this.brLine = brLine;
//    this.paymentCenter = paymentCenter;
//    this.bankBook = bankBook;
//    this.subAccount = subAccount;
//    this.subAccountOwner = subAccountOwner;
//    this.depositAccount = depositAccount;
//    this.depositAccountOwner = depositAccountOwner;
//    this.gpsc = gpsc;
//    this.gpscGroup = gpscGroup;
//    this.lineItemText = lineItemText;
//    this.lineDesc = lineDesc;
//    this.paymentTerm = paymentTerm;
//    this.paymentMethod = paymentMethod;
//    this.wtxType = wtxType;
//    this.wtxCode = wtxCode;
//    this.wtxBase = wtxBase;
//    this.wtxAmount = wtxAmount;
//    this.wtxTypeP = wtxTypeP;
//    this.wtxCodeP = wtxCodeP;
//    this.wtxBaseP = wtxBaseP;
//    this.wtxAmountP = wtxAmountP;
//    this.vendor = vendor;
//    this.vendorTaxId = vendorTaxId;
//    this.payee = payee;
//    this.payeeTaxId = payeeTaxId;
//    this.bankAccountNo = bankAccountNo;
//    this.bankBranchNo = bankBranchNo;
//    this.tradingPartner = tradingPartner;
//    this.tradingPartnerPark = tradingPartnerPark;
//    this.specialGL = specialGL;
//    this.cnDocNo = cnDocNo;
//    this.cnFiscalYear = cnFiscalYear;
//    this.dateBaseline = dateBaseline;
//    this.dateValue = dateValue;
//    this.assetNo = assetNo;
//    this.assetSubNo = assetSubNo;
//    this.qty = qty;
//    this.uom = uom;
//    this.reference1 = reference1;
//    this.reference2 = reference2;
//    this.poDocNo = poDocNo;
//    this.poLine = poLine;
//    this.income = income;
//    this.paymentBlock = paymentBlock;
//    this.paymentRef = paymentRef;
//    this.lineNo = lineNo;
//    this.compCodeName = compCodeName;
//    this.paymentCenterName = paymentCenterName;
//    this.costCenterName = costCenterName;
//    this.paymentMethodName = paymentMethodName;
//    this.fundSourceName = fundSourceName;
//    this.bgCodeName = bgCodeName;
//    this.bgActivityName = bgActivityName;
//    this.name1 = name1;
//    this.name2 = name2;
//    this.taxId = taxId;
//    this.address1 = address1;
//    this.address2 = address2;
//    this.address3 = address3;
//    this.address4 = address4;
//    this.address5 = address5;
//    this.city = city;
//    this.postal = postal;
//    this.regionName = regionName;
//    this.country = country;
//    this.countryCode = countryCode;
//    this.payeeBankAccountType = payeeBankAccountType;
//    this.payeeBankNo = payeeBankNo;
//    this.payeeBankAccountNo = payeeBankAccountNo;
//    this.accountHolderName = accountHolderName;
//    this.payeeBankName = payeeBankName;
//    this.payeeBankKey = payeeBankKey;
//    this.swiftCode = swiftCode;
//    this.payeeBankReference = payeeBankReference;
//  }
}

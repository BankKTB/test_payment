package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
public class PaymentRealRun {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PARENT_COMP_CODE")
    private String parentCompCode;

    @Column(name = "PARENT_DOC_NO")
    private String parentDocNo;

    @Column(name = "PARENT_FISCAL_YEAR")
    private String parentFiscalYear;

    @Column(name = "ACCOUNT_HOLDER_NAME")
    private String accountHolderName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "AMOUNT_PAID")
    private BigDecimal amountPaid;

    @Column(name = "CITY")
    private String city;

    @Column(name = "COMP_CODE")
    private String compCode;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "COUNTRY_NAME")
    private String countryName;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "DATE_DUE")
    private Timestamp dateDue;

    @Column(name = "DATE_VALUE")
    private Timestamp dateValue;

    @Column(name = "FI_AREA")
    private String fiArea;

    @Column(name = "AREA_NAME")
    private String areaName;

    @Column(name = "NAME1")
    private String name1;

    @Column(name = "NAME2")
    private String name2;

    @Column(name = "NAME3")
    private String name3;

    @Column(name = "NAME4")
    private String name4;

    @Column(name = "PAYEE_ADDRESS")
    private String payeeAddress;

    @Column(name = "PAYEE_BANK_ACCOUNT_NO")
    private String payeeBankAccountNo;

    @Column(name = "PAYEE_BANK_KEY")
    private String payeeBankKey;

    @Column(name = "PAYEE_BANK_NO")
    private String payeeBankNo;

    @Column(name = "PAYEE_BANK_REFERENCE")
    private String payeeBankReference;

    @Column(name = "PAYEE_CITY")
    private String payeeCity;

    @Column(name = "PAYEE_CODE")
    private String payeeCode;

    @Column(name = "PAYEE_COUNTRY")
    private String payeeCountry;

    @Column(name = "PAYEE_NAME1")
    private String payeeName1;

    @Column(name = "PAYEE_NAME2")
    private String payeeName2;

    @Column(name = "PAYEE_NAME3")
    private String payeeName3;

    @Column(name = "PAYEE_NAME4")
    private String payeeName4;

    @Column(name = "PAYEE_PORTAL_CODE")
    private String payeePortalCode;

    @Column(name = "PAYEE_TAX_ID")
    private String payeeTaxId;

    @Column(name = "PAYEE_TITLE")
    private String payeeTitle;

    @Column(name = "PAYING_BANK_ACCOUNT_NO")
    private String payingBankAccountNo;

    @Column(name = "PAYING_BANK_CODE")
    private String payingBankCode;

    @Column(name = "PAYING_BANK_COUNTRY")
    private String payingBankCountry;

    @Column(name = "PAYING_BANK_KEY")
    private String payingBankKey;

    @Column(name = "PAYING_BANK_NO")
    private String payingBankNo;

    @Column(name = "PAYING_COMP_CODE")
    private String payingCompCode;

    @Column(name = "PAYING_GL_ACCOUNT")
    private String payingGlAccount;

    @Column(name = "PAYING_HOUSE_BANK")
    private String payingHouseBank;

    @Column(name = "PAYMENT_BLOCK")
    private String paymentBlock;

    @Column(name = "PAYMENT_DATE")
    private Timestamp paymentDate;

    @Column(name = "PAYMENT_DATE_ACCT")
    private Timestamp paymentDateAcct;

    @Column(name = "PAYMENT_DOC_NO")
    private String paymentDocNo;

    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    @Column(name = "PAYMENT_REF")
    private String paymentRef;

    @Column(name = "PAYMENT_SPECIAL_GL")
    private String paymentSpecialGl;

    @Column(name = "PORTAL_CODE")
    private String portalCode;

    @Column(name = "PROPOSAL")
    private String proposal;

    @Column(name = "SWIFT_CODE")
    private String swiftCode;

    @Column(name = "TAX_ID")
    private String taxId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "ACC_DOC_NO")
    private String accDocNo;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "ASSET_NO")
    private String assetNo;

    @Column(name = "ASSET_SUB_NO")
    private String assetSubNo;

    @Column(name = "ASSIGNMENT")
    private String assignment;

    @Column(name = "BANK_ACCOUNT_NO")
    private String bankAccountNo;

    @Column(name = "BR_DOC_NO")
    private String brDocNo;

    @Column(name = "BR_LINE")
    private int brLine;

    @Column(name = "BUDGET_ACCOUNT")
    private String budgetAccount;

    @Column(name = "BUDGET_ACTIVITY")
    private String budgetActivity;

    @Column(name = "BUDGET_ACTIVITY_NAME")
    private String budgetActivityName;

    @Column(name = "CN_DOC_NO")
    private String cnDocNo;

    @Column(name = "CN_FISCAL_YEAR")
    private String cnFiscalYear;

    @Column(name = "CN_LINE")
    private String cnLine;

    @Column(name = "COMP_CODE_NAME")
    private String compCodeName;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "COST_CENTER_NAME")
    private String costCenterName;

    @Column(name = "DATE_ACCT")
    private Timestamp dateAcct;

    @Column(name = "DATE_BASELINE")
    private Timestamp dateBaseline;

    @Column(name = "DATE_DOC")
    private Timestamp dateDoc;

    @Column(name = "DOC_TYPE")
    private String docType;

    @Column(name = "DR_CR")
    private String drCr;

    @Column(name = "ERROR_CODE")
    private String errorCode;

    @Column(name = "FISCAL_YEAR")
    private String fiscalYear;

    @Column(name = "FUND_CENTER")
    private String fundCenter;

    @Column(name = "FUND_CENTER_NAME")
    private String fundCenterName;

    @Column(name = "FUND_SOURCE")
    private String fundSource;

    @Column(name = "FUND_SOURCE_NAME")
    private String fundSourceName;

    @Column(name = "GL_ACCOUNT")
    private String glAccount;

    @Column(name = "GL_ACCOUNT_NAME")
    private String glAccountName;

    @Column(name = "HD_REFERENCE")
    private String hdReference;

    @Column(name = "HOUSE_BANK")
    private String houseBank;

    @Column(name = "LINE")
    private int line;

    @Column(name = "LINE_ITEM_TEXT")
    private String lineItemText;

    @Column(name = "PAYMENT_METHOD_NAME")
    private String paymentMethodName;

    @Column(name = "PAYMENT_TERM")
    private String paymentTerm;

    @Column(name = "PO_DOC_NO")
    private String poDocNo;

    @Column(name = "PO_LINE")
    private int poLine;

    @Column(name = "POSTING_KEY")
    private String postingKey;

    @Column(name = "RECON_ACCOUNT")
    private String reconAccount;

    @Column(name = "RECON_ACCOUNT_NAME")
    private String reconAccountName;

    @Column(name = "REFERENCE1")
    private String reference1;

    @Column(name = "REFERENCE2")
    private String reference2;

    @Column(name = "REFERENCE3")
    private String reference3;

    @Column(name = "SPECIAL_GL")
    private String specialGl;

    @Column(name = "SPECIAL_GL_NAME")
    private String specialGlName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TRADING_PARTNER")
    private String tradingPartner;

    @Column(name = "TRADING_PARTNER_NAME")
    private String tradingPartnerName;

    @Column(name = "VENDOR_FLAG")
    private String venderFlag;

    @Column(name = "VENDOR_NAME")
    private String venderName;

    @Column(name = "WTX_AMOUNT")
    private BigDecimal wtxAmount;

    @Column(name = "WTX_BASE")
    private BigDecimal wtxBase;

    @Column(name = "WTX_CODE")
    private String wtxCode;

    @Column(name = "PM_GROUP_NO")
    private String pmGroupNo;

    @Column(name = "PM_GROUP_DOC")
    private String pmGroupDoc;

    @Column(name = "PAYING_BANK_NAME")
    private String payingBankName;

    @Column(name = "PROPOSAL_BLOCK")
    private boolean proposalBlock;

    @Column(name = "INVOICE_ACC_DOC_NO")
    private String invoiceAccDocNo;
    @Column(name = "INVOICE_FISCAL_YEAR")
    private String invoiceFiscalYear;
    @Column(name = "INVOICE_COMP_CODE")
    private String invoiceCompCode;
    @Column(name = "INVOICE_DOC_TYPE")
    private String invoiceDocType;

    @Column(name = "INVOICE_WTX_AMOUNT")
    private BigDecimal invoiceWtxAmount;
    @Column(name = "INVOICE_WTX_BASE")
    private BigDecimal invoiceWtxBase;
    @Column(name = "INVOICE_WTX_AMOUNT_P")
    private BigDecimal invoiceWtxAmountP;
    @Column(name = "INVOICE_WTX_BASE_P")
    private BigDecimal invoiceWtxBaseP;

    @Column(name = "PAYMENT_FISCAL_YEAR")
    private String paymentFiscalYear;
    @Column(name = "PAYMENT_COMP_CODE")
    private String paymentCompCode;
    @Column(name = "PAYMENT_CENTER")
    private String paymentCenter;

    @Column(name = "ACC_DOC_NO_PAYMENT_CENTER")
    private String accDocNoPaymentCenter;

    @Column(name = "INVOICE_PAYMENT_CENTER")
    private String invoicePaymentCenter;
}

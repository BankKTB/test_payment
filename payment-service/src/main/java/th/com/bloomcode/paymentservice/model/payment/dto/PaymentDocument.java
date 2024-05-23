package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDocument extends BaseModel {

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

    @Column(name = "HD_REFERENCE2")
    private String hdReference2;

    @Column(name = "REV_DATE_ACCT")
    private Timestamp revDateAcct;

    @Column(name = "REV_REASON")
    private String revReason;

    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;

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

}

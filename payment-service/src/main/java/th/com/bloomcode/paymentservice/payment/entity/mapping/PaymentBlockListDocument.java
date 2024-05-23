package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
public class PaymentBlockListDocument {

    @Id
    @Column(name = "ID")
    private Long id;

//    @Column(name = "PARENT_DOC_NO")
//    private String parentDocNo;
//    @Column(name = "PARENT_COMP_CODE")
//    private String parentCompCode;
//    @Column(name = "PARENT_FISCAL_YEAR")
//    private String parentFiscalYear;
    @Column(name = "BR_LINE")
    private int brLine;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "LINE_INV_DOC_NO")
    private String lineInvDocNo;
    @Column(name = "LINE_INV_FISCAL_YEAR")
    private String lineInvFiscalYear;
    @Column(name = "DOC_TYPE")
    private String docType;
    @Column(name = "COMP_CODE")
    private String compCode;
    @Column(name = "DATE_DOC")
    private Date dateDoc;
    @Column(name = "DATE_ACCT")
    private Date dateAcct;
    @Column(name = "PERIOD")
    private int period;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "PAYMENT_CENTER")
    private String paymentCenter;
    @Column(name = "BR_DOC_NO")
    private String brDocNo;
    @Column(name = "PO_DOC_NO")
    private String poDocNo;
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
    private String revDateAcct;
    @Column(name = "REV_REASON")
    private String revReason;
    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;
    @Column(name = "CREATED")
    private Date created;
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
    @Column(name = "REFERENCE3")
    private String reference3;
    @Column(name = "ASSIGNMENT")
    private String assignment;
    @Column(name = "LINE_BR_DOC_NO")
    private String lineBrDocNO;
    @Column(name = "LINE_BR_LINE")
    private String lineBrLine;
    @Column(name = "LINE_PAYMENT_CENTER")
    private String linePaymentCenter;
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
    @Column(name = "PAYEE")
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
    private Date dateBaseline;
    @Column(name = "DATE_VALUE")
    private Date dateValue;
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
    @Column(name = "INCOME")
    private String income;
    @Column(name = "LINE_PO_LINE")
    private String linePoLine;
    @Column(name = "PAYMENT_BLOCK")
    private String paymentBlock;
    @Column(name = "PAYMENT_REF")
    private String paymentRef;
    @Column(name = "VENDOR_NAME")
    private String vendorName;
    @Column(name = "BANK_ACCOUNT_HOLDER_NAME")
    private String bankAccountHolderName;
    @Column(name = "CONFIRM_VENDOR")
    private boolean confirmVendor;
    @Column(name = "FUND_TYPE")
    private String fundType;

    @Column(name = "DOC_BASE_TYPE")
    private String docBaseType;


}

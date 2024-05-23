package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SqlResultSetMapping(name = "PaymentBlockDocumentMapping", classes = @ConstructorResult(targetClass = PaymentBlockDocument.class, columns = {
        @ColumnResult(name = "DOC_TYPE"), @ColumnResult(name = "COMP_CODE"), @ColumnResult(name = "DATE_DOC"),
        @ColumnResult(name = "DATE_ACCT"), @ColumnResult(name = "PERIOD", type = Integer.class),
        @ColumnResult(name = "CURRENCY"), @ColumnResult(name = "AMOUNT"), @ColumnResult(name = "PAYMENT_CENTER"),
        @ColumnResult(name = "INV_DOC_NO"), @ColumnResult(name = "REV_INV_DOC_NO"), @ColumnResult(name = "ACC_DOC_NO"),
        @ColumnResult(name = "REV_ACC_DOC_NO"), @ColumnResult(name = "FISCAL_YEAR"),
        @ColumnResult(name = "REV_FISCAL_YEAR"), @ColumnResult(name = "PAYMENT_METHOD"),
        @ColumnResult(name = "COST_CENTER1"), @ColumnResult(name = "COST_CENTER2"),
        @ColumnResult(name = "HD_REFERENCE"), @ColumnResult(name = "DOC_HEADER_TEXT"),
//                        @ColumnResult(name = "HEADER_DESC"),
        @ColumnResult(name = "HD_REFERENCE2"), @ColumnResult(name = "REV_REASON"),
        @ColumnResult(name = "ORIGINAL_DOC_NO"),
//		@ColumnResult(name = "HD_REF_LOG"),
        @ColumnResult(name = "CREATED"),
        @ColumnResult(name = "USER_PARK"), @ColumnResult(name = "USER_POST"), @ColumnResult(name = "POSTING_KEY"),
        @ColumnResult(name = "ACCOUNT_TYPE"), @ColumnResult(name = "DR_CR"), @ColumnResult(name = "GL_ACCOUNT"),
        @ColumnResult(name = "FI_AREA"), @ColumnResult(name = "COST_CENTER"), @ColumnResult(name = "FUND_SOURCE"),
        @ColumnResult(name = "BG_CODE"), @ColumnResult(name = "BG_ACTIVITY"), @ColumnResult(name = "COST_ACTIVITY"),
        @ColumnResult(name = "REFERENCE3"), @ColumnResult(name = "ASSIGNMENT"), @ColumnResult(name = "BR_DOC_NO"),
        @ColumnResult(name = "BR_LINE", type = Integer.class), @ColumnResult(name = "BANK_BOOK"),
        @ColumnResult(name = "SUB_ACCOUNT"), @ColumnResult(name = "SUB_ACCOUNT_OWNER"),
        @ColumnResult(name = "DEPOSIT_ACCOUNT"), @ColumnResult(name = "DEPOSIT_ACCOUNT_OWNER"),
        @ColumnResult(name = "GPSC"), @ColumnResult(name = "GPSC_GROUP"), @ColumnResult(name = "LINE_ITEM_TEXT"),
        @ColumnResult(name = "LINE_DESC"), @ColumnResult(name = "PAYMENT_TERM"), @ColumnResult(name = "WTX_TYPE"),
        @ColumnResult(name = "WTX_CODE"), @ColumnResult(name = "WTX_BASE"), @ColumnResult(name = "WTX_AMOUNT"),
        @ColumnResult(name = "WTX_TYPE_P"), @ColumnResult(name = "WTX_CODE_P"), @ColumnResult(name = "WTX_BASE_P"),
        @ColumnResult(name = "WTX_AMOUNT_P"), @ColumnResult(name = "VENDOR"), @ColumnResult(name = "VENDOR_TAX_ID"),
        @ColumnResult(name = "PAYEE"), @ColumnResult(name = "PAYEE_TAX_ID"), @ColumnResult(name = "BANK_ACCOUNT_NO"),
        @ColumnResult(name = "BANK_BRANCH_NO"), @ColumnResult(name = "TRADING_PARTNER"),
        @ColumnResult(name = "TRADING_PARTNER_PARK"), @ColumnResult(name = "SPECIAL_GL"),
        @ColumnResult(name = "DATE_BASELINE"), @ColumnResult(name = "DATE_VALUE"), @ColumnResult(name = "ASSET_NO"),
        @ColumnResult(name = "ASSET_SUB_NO"), @ColumnResult(name = "QTY"), @ColumnResult(name = "UOM"),
        @ColumnResult(name = "REFERENCE1"), @ColumnResult(name = "REFERENCE2"), @ColumnResult(name = "PO_DOC_NO"),
        @ColumnResult(name = "PO_LINE", type = Integer.class), @ColumnResult(name = "INCOME"),
        @ColumnResult(name = "PAYMENT_BLOCK"), @ColumnResult(name = "PAYMENT_REF"),
        @ColumnResult(name = "VENDOR_NAME"), @ColumnResult(name = "BANK_ACCOUNT_HOLDER_NAME"),
        @ColumnResult(name = "CONFIRM_VENDOR", type = Boolean.class),
        @ColumnResult(name = "FUND_TYPE"),
}))

@Entity
@Data
public class PaymentBlockDocument {

    @Id
    private Long id;

    private String docType;
    private String compCode;
    private Date dateDoc;
    private Date dateAcct;
    private int period;
    private String currency;
    private BigDecimal amount;
    private String paymentCenter;
    private String invDocNo;
    private String revInvDocNo;
    private String accDocNo;
    private String revAccDocNo;
    private String fiscalYear;
    private String revFiscalYear;
    private String paymentMethod;
    private String costCenter1;
    private String costCenter2;
    private String hdReference;
    private String docHeaderText;
    //  private String headerDesc;
    private String hdReference2;
    private String revReason;
    private String originalDocNo;
    private String hdRefLog;
    private Date created;
    private String userPark;
    private String userPost;
    private String postingKey;
    private String accountType;
    private String drCr;
    private String glAccount;
    private String fiArea;
    private String costCenter;
    private String fundSource;
    private String bgCode;
    private String bgActivity;
    private String costActivity;
    private String reference3;
    private String assignment;
    private String brDocNo;
    private int brLine;
    private String bankBook;
    private String subAccount;
    private String subAccountOwner;
    private String depositAccount;
    private String depositAccountOwner;
    private String gpsc;
    private String gpscGroup;
    private String lineItemText;
    private String lineDesc;
    private String paymentTerm;
    private String wtxType;
    private String wtxCode;
    private BigDecimal wtxBase;
    private BigDecimal wtxAmount;
    private String wtxTypeP;
    private String wtxCodeP;
    private BigDecimal wtxBaseP;
    private BigDecimal wtxAmountP;
    private String vendor;
    private String vendorName;
    private String vendorTaxId;
    private String payee;
    private String payeeTaxId;
    private String bankAccountNo;
    private String bankAccountHolderName;
    private String bankBranchNo;
    private String tradingPartner;
    private String tradingPartnerPark;
    private String specialGL;
    private String cnDocNo;
    private String cnFiscalYear;
    private Date dateBaseline;
    private Date dateValue;
    private String assetNo;
    private String assetSubNo;
    private BigDecimal qty;
    private String uom;
    private String reference1;
    private String reference2;
    private String poDocNo;
    private int poLine;
    private String income;
    private String paymentBlock;
    private String paymentRef;
    private boolean confirmVendor;
    private String fundType;

    public PaymentBlockDocument() {
    }

    public PaymentBlockDocument(String docType, String compCode, Date dateDoc, Date dateAcct, int period,
                                String currency, BigDecimal amount, String paymentCenter, String invDocNo, String revInvDocNo,
                                String accDocNo, String revAccDocNo, String fiscalYear, String revFiscalYear, String paymentMethod,
                                String costCenter1, String costCenter2, String hdReference, String docHeaderText,
                                String hdReference2, String revReason, String originalDocNo, Date created, String userPark,
                                String userPost, String postingKey, String accountType, String drCr, String glAccount, String fiArea,
                                String costCenter, String fundSource, String bgCode, String bgActivity, String costActivity,
                                String reference3, String assignment, String brDocNo, int brLine, String bankBook, String subAccount,
                                String subAccountOwner, String depositAccount, String depositAccountOwner, String gpsc, String gpscGroup,
                                String lineItemText, String lineDesc, String paymentTerm, String wtxType, String wtxCode,
                                BigDecimal wtxBase, BigDecimal wtxAmount, String wtxTypeP, String wtxCodeP, BigDecimal wtxBaseP,
                                BigDecimal wtxAmountP, String vendor, String vendorTaxId, String payee, String payeeTaxId,
                                String bankAccountNo, String bankBranchNo, String tradingPartner, String tradingPartnerPark,
                                String specialGL, Date dateBaseline, Date dateValue, String assetNo,
                                String assetSubNo, BigDecimal qty, String uom, String reference1, String reference2, String poDocNo,
                                int poLine, String income, String paymentBlock, String paymentRef, String vendorName, String bankAccountHolderName, boolean confirmVendor, String fundType) {
        this.docType = docType;
        this.compCode = compCode;
        this.dateDoc = dateDoc;
        this.dateAcct = dateAcct;
        this.period = period;
        this.currency = currency;
        this.amount = amount;
        this.paymentCenter = paymentCenter;
        this.invDocNo = invDocNo;
        this.revInvDocNo = revInvDocNo;
        this.accDocNo = accDocNo;
        this.revAccDocNo = revAccDocNo;
        this.fiscalYear = fiscalYear;
        this.revFiscalYear = revFiscalYear;
        this.paymentMethod = paymentMethod;
        this.costCenter1 = costCenter1;
        this.costCenter2 = costCenter2;
        this.hdReference = hdReference;
        this.docHeaderText = docHeaderText;
//    this.headerDesc = headerDesc;
        this.hdReference2 = hdReference2;
        this.revReason = revReason;
        this.originalDocNo = originalDocNo;
//		this.hdRefLog = hdRefLog;
        this.created = created;
        this.userPark = userPark;
        this.userPost = userPost;
        this.postingKey = postingKey;
        this.accountType = accountType;
        this.drCr = drCr;
        this.glAccount = glAccount;
        this.fiArea = fiArea;
        this.costCenter = costCenter;
        this.fundSource = fundSource;
        this.bgCode = bgCode;
        this.bgActivity = bgActivity;
        this.costActivity = costActivity;
        this.reference3 = reference3;
        this.assignment = assignment;
        this.brDocNo = brDocNo;
        this.brLine = brLine;
        this.bankBook = bankBook;
        this.subAccount = subAccount;
        this.subAccountOwner = subAccountOwner;
        this.depositAccount = depositAccount;
        this.depositAccountOwner = depositAccountOwner;
        this.gpsc = gpsc;
        this.gpscGroup = gpscGroup;
        this.lineItemText = lineItemText;
        this.lineDesc = lineDesc;
        this.paymentTerm = paymentTerm;
        this.wtxType = wtxType;
        this.wtxCode = wtxCode;
        this.wtxBase = wtxBase;
        this.wtxAmount = wtxAmount;
        this.wtxTypeP = wtxTypeP;
        this.wtxCodeP = wtxCodeP;
        this.wtxBaseP = wtxBaseP;
        this.wtxAmountP = wtxAmountP;
        this.vendor = vendor;
        this.vendorTaxId = vendorTaxId;
        this.payee = payee;
        this.payeeTaxId = payeeTaxId;
        this.bankAccountNo = bankAccountNo;
        this.bankBranchNo = bankBranchNo;
        this.tradingPartner = tradingPartner;
        this.tradingPartnerPark = tradingPartnerPark;
        this.specialGL = specialGL;
        this.dateBaseline = dateBaseline;
        this.dateValue = dateValue;
        this.assetNo = assetNo;
        this.assetSubNo = assetSubNo;
        this.qty = qty;
        this.uom = uom;
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.poDocNo = poDocNo;
        this.poLine = poLine;
        this.income = income;
        this.paymentBlock = paymentBlock;
        this.paymentRef = paymentRef;
        this.vendorName = vendorName;
        this.bankAccountHolderName = bankAccountHolderName;
        this.confirmVendor = confirmVendor;
        this.fundType = fundType;
    }
}

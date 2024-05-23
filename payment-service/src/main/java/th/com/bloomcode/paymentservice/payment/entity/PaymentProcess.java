//package th.com.bloomcode.paymentservice.payment.entity;
//
//import lombok.Data;
//import th.com.bloomcode.paymentservice.payment.entity.mapping.PaymentRealRun;
//import th.com.bloomcode.paymentservice.payment.entity.mapping.PaymentRunDocument;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
///*
// * SAP REGUP Table
// * */
//@Entity
//@Table(name = "PAYMENT_PROCESS")
//@Data
//public class PaymentProcess implements Serializable {
//
//    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_PROCESS_SEQ")
//    @SequenceGenerator(sequenceName = "PAYMENT_PROCESS_SEQ", allocationSize = 1, name = "PAYMENT_PROCESS_SEQ")
//    private Long id;
//
//    private Long paymentId;
//    private Date paymentDate;
//    private String paymentName;
//    private boolean proposal;
//    private String payingCompCode;
//    private String vendor;
//    private String vendorName;
//    private String payeeCode;
//    private String paymentDocNo;
//    private String paymentCompCode;
//    private String paymentFiscalYear;
//    private String paymentCenter;
//
//    private String compCode;
//    private String compCodeName;
//    private String accDocNo;
//    private String fiscalYear;
//    private int line;
//    private String paymentMethod;
//    private String paymentMethodName;
//    private String currency;
//    private String houseBank;
//    private String bankAccountNo;
//    private String hdReference;
//    private String docType;
//    private Date dateAcct;
//    private Date dateDoc;
//    private String accountType;
//    private String postingKey;
//    private String glAccount;
//    private String glAccountName;
//    private String reconAccount;
//    private String reconAccountName;
//
//    @Column(name = "SPECIAL_GL")
//    private String specialGL;
//
//    @Column(name = "SPECIAL_GL_NAME")
//    private String specialGLName;
//
//    private String drCr;
//    private BigDecimal amount;
//    private String fiArea;
//    private String lineItemText;
//    private Date dateBaseline;
//    private String paymentTerm;
//    private String paymentBlock;
//    private String wtxCode;
//    private BigDecimal wtxBase;
//    private BigDecimal wtxAmount;
//    private String assetNo;
//    private String assetSubNo;
//    private String poDocNo;
//    private int poLine;
//    private String tradingPartner;
//    private String tradingPartnerName;
//    private String budgetAccount;
//    private String assignment;
//    private String cnDocNo;
//    private String cnFiscalYear;
//    private String cnLine;
//    private String costCenter;
//    private String costCenterName;
//    private String fundCenter;
//    private String fundCenterName;
//    private String fundSource;
//    private String fundSourceName;
//    private String reference1;
//    private String reference2;
//    private String reference3;
//    private String paymentRef;
//    private String vendorFlag;
//    private String brDocNo;
//    private int brLine;
//    private String budgetActivity;
//    private String budgetActivityName;
//
//    private String status;
//    private String errorCode;
//
//
//    private String pmGroupNo;
//    private String pmGroupDoc;
//    private boolean proposalBlock;
//    private String idemStatus;
//
//    private boolean isChild;
//    private String parentDocNo;
//    private String parentFiscalYear;
//    private String parentCompCode;
//
//
//    private String invoiceAccDocNo;
//    private String invoiceFiscalYear;
//    private String invoiceCompCode;
//    private String invoiceDocType;
//    private String invoicePaymentCenter;
//
//
//    private BigDecimal invoiceWtxAmount;
//    private BigDecimal invoiceWtxBase;
//    @Column(name = "INVOICE_WTX_AMOUNT_P")
//    private BigDecimal invoiceWtxAmountP;
//    @Column(name = "INVOICE_WTX_BASE_P")
//    private BigDecimal invoiceWtxBaseP;
//
//    private String accDocNoPaymentCenter;
//
//
//    public PaymentProcess() {
//    }
//
//    public PaymentProcess(PaymentRunDocument paymentDocument, boolean isProposal,boolean isChild) {
//        this.paymentId = paymentDocument.getPaymentId();
//        this.paymentDate = paymentDocument.getPaymentDate();
//        this.paymentName = paymentDocument.getPaymentName();
//        this.proposal = isProposal;
//        this.payingCompCode = paymentDocument.getPayingCompCode();
//        this.vendor = paymentDocument.getVendor();
//        this.vendorName = paymentDocument.getName1();
//        this.payeeCode = paymentDocument.getPayee();
//        this.paymentDocNo = paymentDocument.getPaymentClearingDocNo();
//        this.compCode = paymentDocument.getCompCode();
//        this.compCodeName = paymentDocument.getCompCodeName();
//        this.accDocNo = paymentDocument.getAccDocNo();
////        this.accDocNoPaymentCenter = paymentDocument.getPaymentCenter();
//        this.fiscalYear = paymentDocument.getFiscalYear();
//        this.line = paymentDocument.getLineNo();
//        this.paymentMethod = paymentDocument.getPaymentMethod();
//        this.paymentMethodName = paymentDocument.getPaymentMethodName();
//        this.currency = paymentDocument.getCurrency();
//        this.houseBank = paymentDocument.getHouseBank();
//        this.bankAccountNo = paymentDocument.getBankAccountNo();
//        this.hdReference = paymentDocument.getHdReference();
//        this.docType = paymentDocument.getDocType();
//        this.dateAcct = paymentDocument.getDateAcct();
//        this.dateDoc = paymentDocument.getDateDoc();
//        this.accountType = paymentDocument.getAccountType();
//        this.postingKey = paymentDocument.getPostingKey();
//        this.glAccount = paymentDocument.getPayingGLAccount();
//        this.glAccountName = paymentDocument.getGlAccount();
////		this.reconAccount = paymentDocument;
////		this.reconAccountName = paymentDocument;
//        this.specialGL = paymentDocument.getSpecialGL();
//        this.specialGLName = paymentDocument.getSpecialGL();
//        this.drCr = paymentDocument.getDrCr();
//        this.amount = paymentDocument.getAmount();
//        this.fiArea = paymentDocument.getFiArea();
//        this.lineItemText = paymentDocument.getLineItemText();
//        this.dateBaseline = paymentDocument.getDateBaseline();
//        this.paymentTerm = paymentDocument.getPaymentTerm();
//        this.paymentBlock = paymentDocument.getPaymentBlock();
//        this.wtxCode = paymentDocument.getWtxCode();
//        this.wtxBase = paymentDocument.getWtxBase();
//        this.wtxAmount = paymentDocument.getWtxAmount();
//        this.assetNo = paymentDocument.getAssetNo();
//        this.assetSubNo = paymentDocument.getAssetSubNo();
//        this.poDocNo = paymentDocument.getPoDocNo();
//        this.poLine = paymentDocument.getPoLine();
//        this.tradingPartner = paymentDocument.getTradingPartner();
//        this.tradingPartnerName = paymentDocument.getTradingPartner();
////		this.budgetAccount = paymentDocument;
//        this.assignment = paymentDocument.getAssignment();
////        this.cnDocNo = paymentDocument.getCnDocNo();
////        this.cnFiscalYear = paymentDocument.getCnFiscalYear();
////		this.cnLine = paymentDocument;
//        this.costCenter = paymentDocument.getCostCenter();
//        this.costCenterName = paymentDocument.getCostCenterName();
////		this.fundCenter = paymentDocument;
////		this.fundCenterName = paymentDocument;
//        this.fundSource = paymentDocument.getFundSource();
//        this.fundSourceName = paymentDocument.getFundSourceName();
//        this.reference1 = paymentDocument.getReference1();
//        this.reference2 = paymentDocument.getReference2();
//        this.reference3 = paymentDocument.getReference3();
//        this.paymentRef = paymentDocument.getPaymentRef();
////		this.vendorFlag = paymentDocument;
//        this.brDocNo = paymentDocument.getBrDocNo();
//        this.brLine = paymentDocument.getBrLine();
//        this.budgetActivity = paymentDocument.getBgActivity();
//        this.budgetActivityName = paymentDocument.getBgActivityName();
//        this.status = paymentDocument.getStatus();
//        this.errorCode = paymentDocument.getErrorCode();
//        this.idemStatus = "";
//        this.isChild  = isChild;
//    }
//
//    public PaymentProcess(PaymentRealRun paymentDocument) {
//        this.paymentId = paymentDocument.getPaymentId();
//        this.paymentDate = paymentDocument.getPaymentDate();
//        this.paymentName = paymentDocument.getPaymentName();
//        this.proposal = false;
//        this.payingCompCode = paymentDocument.getPayingCompCode();
//        this.vendor = paymentDocument.getVendor();
//        this.vendorName = paymentDocument.getName1();
//        this.payeeCode = paymentDocument.getPayeeCode();
//        this.paymentDocNo = null;
//        this.compCode = paymentDocument.getCompCode();
//        this.compCodeName = paymentDocument.getCompCodeName();
//        this.accDocNo = paymentDocument.getAccDocNo();
////        this.accDocNoPaymentCenter = paymentDocument.getPaymentCenter();
//        this.fiscalYear = paymentDocument.getFiscalYear();
//        this.line = paymentDocument.getLine();
//        this.paymentMethod = paymentDocument.getPaymentMethod();
//        this.paymentMethodName = paymentDocument.getPaymentMethodName();
//        this.currency = paymentDocument.getCurrency();
//        this.houseBank = paymentDocument.getHouseBank();
//        this.bankAccountNo = paymentDocument.getBankAccountNo();
//        this.hdReference = paymentDocument.getHdReference();
//        this.docType = paymentDocument.getDocType();
//        this.dateAcct = paymentDocument.getDateAcct();
//        this.dateDoc = paymentDocument.getDateDoc();
//        this.accountType = paymentDocument.getAccountType();
//        this.postingKey = paymentDocument.getPostingKey();
//        this.glAccount = paymentDocument.getGlAccount();
//        this.glAccountName = paymentDocument.getGlAccount();
////		this.reconAccount = paymentDocument;
////		this.reconAccountName = paymentDocument;
//        this.specialGL = paymentDocument.getSpecialGl();
//        this.specialGLName = paymentDocument.getSpecialGlName();
//        this.drCr = paymentDocument.getDrCr();
//        this.amount = paymentDocument.getAmount();
//        this.fiArea = paymentDocument.getFiArea();
//        this.lineItemText = paymentDocument.getLineItemText();
//        this.dateBaseline = paymentDocument.getDateBaseline();
//        this.paymentTerm = paymentDocument.getPaymentTerm();
//        this.paymentBlock = paymentDocument.getPaymentBlock();
//        this.wtxCode = paymentDocument.getWtxCode();
//        this.wtxBase = paymentDocument.getWtxBase();
//        this.wtxAmount = paymentDocument.getWtxAmount();
//        this.assetNo = paymentDocument.getAssetNo();
//        this.assetSubNo = paymentDocument.getAssetSubNo();
//        this.poDocNo = paymentDocument.getPoDocNo();
//        this.poLine = paymentDocument.getPoLine();
//        this.tradingPartner = paymentDocument.getTradingPartner();
//        this.tradingPartnerName = paymentDocument.getTradingPartner();
////		this.budgetAccount = paymentDocument;
//        this.assignment = paymentDocument.getAssignment();
//        this.cnDocNo = paymentDocument.getCnDocNo();
//        this.cnFiscalYear = paymentDocument.getCnFiscalYear();
////		this.cnLine = paymentDocument;
//        this.costCenter = paymentDocument.getCostCenter();
//        this.costCenterName = paymentDocument.getCostCenterName();
////		this.fundCenter = paymentDocument;
////		this.fundCenterName = paymentDocument;
//        this.fundSource = paymentDocument.getFundSource();
//        this.fundSourceName = paymentDocument.getFundSourceName();
//        this.reference1 = paymentDocument.getReference1();
//        this.reference2 = paymentDocument.getReference2();
//        this.reference3 = paymentDocument.getReference3();
//        this.paymentRef = paymentDocument.getPaymentRef();
////		this.vendorFlag = paymentDocument;
//        this.brDocNo = paymentDocument.getBrDocNo();
//        this.brLine = paymentDocument.getBrLine();
//        this.budgetActivity = paymentDocument.getBudgetActivity();
//        this.budgetActivityName = paymentDocument.getBudgetActivityName();
//        this.status = paymentDocument.getStatus();
//        this.errorCode = paymentDocument.getErrorCode();
//        this.idemStatus = "W";
//    }
//
//
//}

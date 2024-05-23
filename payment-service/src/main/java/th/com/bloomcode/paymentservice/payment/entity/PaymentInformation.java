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
//@Entity
//@Table(name = "PAYMENT_INFORMATION")
//@Data
//public class PaymentInformation implements Serializable {
//
//    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_INFORMATION_SEQ")
//    @SequenceGenerator(sequenceName = "PAYMENT_INFORMATION_SEQ", allocationSize = 1, name = "PAYMENT_INFORMATION_SEQ")
//    private Long id;
//
//    private Long paymentId;
//    private Date paymentDate;
//    private String paymentName;
//    private boolean proposal;
//    private String payingCompCode;
//    private String vendor;
//    private String payeeCode;
//    private String paymentDocNo;
//    private String paymentCompCode;
//    private String paymentFiscalYear;
//    private String currency;
//    private String fiArea;
//    private String areaName;
//    private String title;
//    private String name1;
//    private String name2;
//    private String name3;
//    private String name4;
//    private String portalCode;
//    private String city;
//    private String address;
//    private String country;
//    private String countryName;
//    private String taxId;
//    private String payeeTitle;
//    private String payeeName1;
//    private String payeeName2;
//    private String payeeName3;
//    private String payeeName4;
//    private String payeePortalCode;
//    private String payeeCity;
//    private String payeeAddress;
//    private String payeeCountry;
//    private String payeeTaxId;
//    private String payeeBankAccountNo;
//    private String payeeBankNo;
//    private String swiftCode;
//    private Date paymentDateAcct;
//    private String paymentMethod;
//
//    @Column(name = "PAYMENT_SPECIAL_GL")
//    private String paymentSpecialGL;
//
//    private String payingBankCode;
//    private String payingHouseBank;
//    private String payingBankAccountNo;
//    private String payingBankCountry;
//    private String payingBankNo;
//    private String payingBankName;
//
//    @Column(name = "PAYING_GL_ACCOUNT")
//    private String payingGLAccount;
//
//    private Date dateValue;
//    private BigDecimal amount;
//    private BigDecimal amountPaid;
//    private String paymentBlock;
//    private String payingBankKey;
//
//    @Column(name = "PAYEE_BANK_KEY")
//    private String payeeBankKey;
//
//    private String compCode;
//    private String payeeBankReference;
//    private Date dateDue;
//    private String accountHolderName;
//    private String paymentRef;
//
//    private String accDocNo;
//    private String fiscalYear;
//
//    private String pmGroupNo;
//    private String pmGroupDoc;
//    private String idemStatus;
//
//    private boolean isChild;
//    private String parentDocNo;
//    private String parentFiscalYear;
//    private String parentCompCode;
//
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "paymentId", referencedColumnName = "paymentId", insertable = false, updatable = false),
//            @JoinColumn(name = "vendor", referencedColumnName = "vendor", insertable = false, updatable = false),
////			@JoinColumn(name = "paymentDocNo", referencedColumnName = "paymentDocNo", insertable = false, updatable = false),
//            @JoinColumn(name = "proposal", referencedColumnName = "proposal", insertable = false, updatable = false),
//            @JoinColumn(name = "pmGroupDoc", referencedColumnName = "pmGroupDoc", insertable = false, updatable = false)})
//    private PaymentProcess paymentProcess;
//
//    public PaymentInformation() {
//    }
//
//    public PaymentInformation(PaymentRunDocument paymentDocument, boolean isProposal, boolean isChild) {
//        this.paymentId = paymentDocument.getPaymentId();
//        this.paymentDate = paymentDocument.getPaymentDate();
//        this.paymentName = paymentDocument.getPaymentName();
//        this.proposal = isProposal;
//        this.payingCompCode = paymentDocument.getPayingCompCode();
//        this.vendor = paymentDocument.getVendor();
//        this.payeeCode = paymentDocument.getPayee();
//        this.paymentDocNo = paymentDocument.getPaymentClearingDocNo();
//        this.currency = paymentDocument.getCurrency();
//        this.fiArea = paymentDocument.getFiArea();
//        this.areaName = paymentDocument.getAreaName();
////		this.title = paymentDocument;
//        this.name1 = paymentDocument.getName1();
//        this.name2 = paymentDocument.getName2();
////		this.name3 = paymentDocument;
////		this.name4 = paymentDocument;
//        this.portalCode = paymentDocument.getPostal();
//        this.city = paymentDocument.getCity();
//        this.address = null == paymentDocument.getAddress1() ? "" : paymentDocument.getAddress1() + " " + null == paymentDocument.getAddress3() ? "" : paymentDocument.getAddress3() + " " + null == paymentDocument.getAddress4() ? "" : paymentDocument.getAddress4() + " " + null == paymentDocument.getAddress5() ? "" : paymentDocument.getAddress5();
//        this.country = paymentDocument.getCountryCode();
//        this.countryName = paymentDocument.getCountry();
//        this.taxId = paymentDocument.getTaxId();
////		this.payeeTitle = paymentDocument;
//        this.payeeName1 = paymentDocument.getName1();
//        this.payeeName2 = paymentDocument.getName2();
////		this.payeeName3 = paymentDocument;
////		this.payeeName4 = paymentDocument;
//        this.payeePortalCode = paymentDocument.getPostal();
//        this.payeeCity = paymentDocument.getCity();
//        this.payeeAddress = null == paymentDocument.getAddress1() ? "" : paymentDocument.getAddress1() + " " + null == paymentDocument.getAddress3() ? "" : paymentDocument.getAddress3() + " " + null == paymentDocument.getAddress4() ? "" : paymentDocument.getAddress4() + " " + null == paymentDocument.getAddress5() ? "" : paymentDocument.getAddress5();
//        this.payeeCountry = paymentDocument.getCountry();
//        this.payeeTaxId = paymentDocument.getPayeeTaxId();
//        this.payeeBankAccountNo = paymentDocument.getPayeeBankAccountNo();
//        this.payeeBankNo = paymentDocument.getPayeeBankNo();
//        this.swiftCode = paymentDocument.getSwiftCode();
//        this.paymentDateAcct = paymentDocument.getPaymentDateAcct();
//        this.paymentMethod = paymentDocument.getPaymentMethod();
////		this.paymentSpecialGL = paymentDocument.getPayment;
//        this.payingBankCode = paymentDocument.getPayingBankCode();
//        this.payingHouseBank = paymentDocument.getPayingHouseBank();
//        this.payingBankAccountNo = paymentDocument.getPayingBankAccountNo();
//        this.payingBankCountry = paymentDocument.getPayingBankCountry();
//        this.payingBankNo = paymentDocument.getPayingBankNo();
//        this.payingGLAccount = paymentDocument.getPayingGLAccount();
//        this.dateValue = paymentDocument.getDateValue();
//        this.amount = paymentDocument.getAmount();
//        BigDecimal wtx = null != paymentDocument.getWtxAmount() && paymentDocument.getWtxAmount().compareTo(BigDecimal.ZERO) > 0 ? paymentDocument.getWtxAmount() : BigDecimal.ZERO;
//        this.amountPaid = paymentDocument.getAmount().subtract(wtx);
//        this.paymentBlock = paymentDocument.getPaymentBlock();
//        this.payingBankKey = paymentDocument.getPayingBankKey();
//        this.payeeBankKey = paymentDocument.getPayeeBankKey();
//        this.compCode = paymentDocument.getCompCode();
//        this.payeeBankReference = paymentDocument.getPayeeBankReference();
////		this.dateDue = paymentDocument.date;
//        this.accountHolderName = paymentDocument.getAccountHolderName();
//        this.paymentRef = paymentDocument.getPaymentRef();
//
//        this.accDocNo = paymentDocument.getAccDocNo();
//        this.fiscalYear = paymentDocument.getFiscalYear();
//        this.payingBankName = paymentDocument.getPayingBankName();
//        this.idemStatus = "";
//        this.isChild = isChild;
//    }
//
//
//    public PaymentInformation(PaymentRealRun paymentDocument) {
//        this.paymentId = paymentDocument.getPaymentId();
//        this.paymentDate = paymentDocument.getPaymentDate();
//        this.paymentName = paymentDocument.getPaymentName();
//        this.proposal = false;
//        this.payingCompCode = paymentDocument.getPayingCompCode();
//        this.vendor = paymentDocument.getVendor();
//        this.payeeCode = paymentDocument.getPayeeCode();
//        this.paymentDocNo = null;
//        this.currency = paymentDocument.getCurrency();
//        this.fiArea = paymentDocument.getFiArea();
//        this.areaName = paymentDocument.getAreaName();
////		this.title = paymentDocument;
//        this.name1 = paymentDocument.getName1();
//        this.name2 = paymentDocument.getName2();
////		this.name3 = paymentDocument;
////		this.name4 = paymentDocument;
//        this.portalCode = paymentDocument.getPortalCode();
//        this.city = paymentDocument.getCity();
//        this.address = paymentDocument.getAddress();
//        this.country = paymentDocument.getCountry();
//        this.countryName = paymentDocument.getCountryName();
//        this.taxId = paymentDocument.getTaxId();
////		this.payeeTitle = paymentDocument;
//        this.payeeName1 = paymentDocument.getName1();
//        this.payeeName2 = paymentDocument.getName2();
////		this.payeeName3 = paymentDocument;
////		this.payeeName4 = paymentDocument;
//        this.payeePortalCode = paymentDocument.getPayeePortalCode();
//        this.payeeCity = paymentDocument.getCity();
//        this.payeeAddress = paymentDocument.getPayeeAddress();
//        this.payeeCountry = paymentDocument.getCountry();
//        this.payeeTaxId = paymentDocument.getPayeeTaxId();
//        this.payeeBankAccountNo = paymentDocument.getPayeeBankAccountNo();
//        this.payeeBankNo = paymentDocument.getPayeeBankNo();
//        this.swiftCode = paymentDocument.getSwiftCode();
//        this.paymentDateAcct = paymentDocument.getPaymentDateAcct();
//        this.paymentMethod = paymentDocument.getPaymentMethod();
////		this.paymentSpecialGL = paymentDocument.getPayment;
//        this.payingBankCode = paymentDocument.getPayingBankCode();
//        this.payingHouseBank = paymentDocument.getPayingHouseBank();
//        this.payingBankAccountNo = paymentDocument.getPayingBankAccountNo();
//        this.payingBankCountry = paymentDocument.getPayingBankCountry();
//        this.payingBankNo = paymentDocument.getPayingBankNo();
//        this.payingGLAccount = paymentDocument.getPayingGlAccount();
//        this.dateValue = paymentDocument.getDateValue();
//        this.amount = paymentDocument.getAmount();
//        BigDecimal wtx = null != paymentDocument.getWtxAmount() && paymentDocument.getWtxAmount().compareTo(BigDecimal.ZERO) > 0 ? paymentDocument.getWtxAmount() : BigDecimal.ZERO;
//        this.amountPaid = paymentDocument.getAmount().subtract(wtx);
//        this.paymentBlock = paymentDocument.getPaymentBlock();
//        this.payingBankKey = paymentDocument.getPayingBankKey();
//        this.payeeBankKey = paymentDocument.getPayeeBankKey();
//        this.compCode = paymentDocument.getCompCode();
//        this.payeeBankReference = paymentDocument.getPayeeBankReference();
////		this.dateDue = paymentDocument.date;
//        this.accountHolderName = paymentDocument.getAccountHolderName();
//        this.paymentRef = paymentDocument.getPaymentRef();
//
//        this.accDocNo = paymentDocument.getAccDocNo();
//        this.fiscalYear = paymentDocument.getFiscalYear();
//        this.payingBankName = paymentDocument.getPayingBankName();
//        this.idemStatus = "W";
//    }
//}

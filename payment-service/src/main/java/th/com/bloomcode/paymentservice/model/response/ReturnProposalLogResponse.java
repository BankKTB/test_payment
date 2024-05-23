package th.com.bloomcode.paymentservice.model.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
public class ReturnProposalLogResponse{
    @Column(name = "ID")
    private Long id;

    @Column(name = "FILE_STATUS")
    private String fileStatus;

    @Column(name = "REF_RUNNING")
    private String refRunning;

    @Column(name = "REF_LINE")
    private String refLine;

    @Column(name = "PAYMENT_DATE")
    private String paymentDate;

    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    @Column(name = "ACCOUNT_NO_FROM")
    private String accountNoFrom;

    @Column(name = "ACCOUNT_NO_TO")
    private String accountNoTo;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "TRANSFER_DATE")
    private String transferDate;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "BANK_KEY")
    private String bankKey;

    @Column(name = "VENDOR_BANK_ACCOUNT")
    private String vendorBankAccount;

    @Column(name = "TRANSFER_LEVEL")
    private String transferLevel;

    @Column(name = "PAY_ACCOUNT")
    private String payAccount;

    @Column(name = "PAYMENT_COMP_CODE")
    private String paymentCompCode;

    @Column(name = "PAYMENT_DOCUMENT")
    private String paymentDocument;

    @Column(name = "AMOUNT")
    private BigDecimal amount;
//    public static final String TABLE_NAME = "PROPOSAL_LOG";
//
//    public static final String COLUMN_NAME_PROPOSAL_LOG_ID = "ID";
//    public static final String COLUMN_NAME_FILE_STATUS = "FILE_STATUS";
//    public static final String COLUMN_NAME_REF_RUNNING = "REF_RUNNING";
//    public static final String COLUMN_NAME_REF_LINE = "REF_LINE";
//    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
//    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
//    public static final String COLUMN_NAME_ACCOUNT_NO_FROM = "ACCOUNT_NO_FROM";
//    public static final String COLUMN_NAME_ACCOUNT_NO_TO = "ACCOUNT_NO_TO";
//    public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
//    public static final String COLUMN_NAME_FILE_NAME = "FILE_NAME";
//    public static final String COLUMN_NAME_TRANSFER_DATE = "TRANSFER_DATE";
//    public static final String COLUMN_NAME_TRANSFER_LEVEL = "TRANSFER_LEVEL";
//    public static final String COLUMN_NAME_VENDOR = "VENDOR";
//    public static final String COLUMN_NAME_BANK_KEY = "BANK_KEY";
//    public static final String COLUMN_NAME_VENDOR_BANK_ACCOUNT = "VENDOR_BANK_ACCOUNT";
//    public static final String COLUMN_NAME_PAY_ACCOUNT = "PAY_ACCOUNT";
//    public static final String COLUMN_NAME_PAYING_COMP_CODE = "PAYING_COMP_CODE";
//    public static final String COLUMN_NAME_PAYMENT_DOCUMENT = "PAYMENT_DOCUMENT";
//    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
//
//    private String fileStatus;
//    private Long refRunning;
//    private Integer refLine;
//    private Timestamp paymentDate;
//    private String paymentName;
//    private String accountNoFrom;
//    private String accountNoTo;
//    private String fileType;
//    private String fileName;
//    private Timestamp transferDate;
//    private String transferLevel;
//    private String vendor;
//    private String bankKey;
//    private String vendorBankAccount;
//    private String payAccount;
//    private String payingCompCode;
//    private String paymentDocument;
//    private BigDecimal amount;
//
//    public ReturnProposalLogResponse(Long id,
//                                     String fileStatus,
//                                     Long refRunning,
//                                     Integer refLine,
//                                     Timestamp paymentDate,
//                                     String paymentName,
//                                     String accountNoFrom,
//                                     String accountNoTo,
//                                     String fileType,
//                                     String fileName,
//                                     Timestamp transferDate,
//                                     String transferLevel,
//                                     String vendor,
//                                     String bankKey,
//                                     String vendorBankAccount,
//                                     String payAccount,
//                                     String payingCompCode,
//                                     String paymentDocument,
//                                     BigDecimal amount) {
//        super(id);
//        this.accountNoFrom = accountNoFrom;
//        this.accountNoTo = accountNoTo;
//        this.amount = amount;
//        this.bankKey = bankKey;
//        this.fileName = fileName;
//        this.fileStatus = fileStatus;
//        this.fileType = fileType;
//        this.payAccount = payAccount;
//        this.payingCompCode = payingCompCode;
//        this.paymentDate = paymentDate;
//        this.paymentDocument = paymentDocument;
//        this.paymentName = paymentName;
//        this.refLine = refLine;
//        this.refRunning = refRunning;
//        this.transferDate = transferDate;
//        this.transferLevel = transferLevel;
//        this.vendor = vendor;
//        this.vendorBankAccount = vendorBankAccount;
//    }

}

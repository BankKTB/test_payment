package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SwiftFile {

    public static final String TABLE_NAME = "SWIFT_FILE";

    public static final String COLUMN_NAME_SWIFT_FILE_ID = "ID";
    public static final String COLUMN_NAME_SEND_REF = "SEND_REF";
    public static final String COLUMN_NAME_BANK_CODE = "BANK_CODE";
    public static final String COLUMN_NAME_TRANSFER_TYPE = "TRANSFER_TYPE";
    public static final String COLUMN_NAME_VALUE_DATE = "VALUE_DATE";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_SET_AMOUNT = "SET_AMOUNT";
    public static final String COLUMN_NAME_ORD_ACCT = "ORD_ACCT";
    public static final String COLUMN_NAME_ORD_NAME1 = "ORD_NAME1";
    public static final String COLUMN_NAME_ORD_NAME2 = "ORD_NAME2";
    public static final String COLUMN_NAME_ORD_NAME3 = "ORD_NAME3";
    public static final String COLUMN_NAME_ORD_NAME4 = "ORD_NAME4";
    public static final String COLUMN_NAME_SEND_CODE = "SEND_CODE";
    public static final String COLUMN_NAME_SEND_ACCT = "SEND_ACCT";
    public static final String COLUMN_NAME_REC_CODE = "REC_CODE";
    public static final String COLUMN_NAME_REC_ACCT = "REC_ACCT";
    public static final String COLUMN_NAME_REC_INSTI = "REC_INSTI";
    public static final String COLUMN_NAME_BEN_ACCT = "BEN_ACCT";
    public static final String COLUMN_NAME_BEN_NAME1 = "BEN_NAME1";
    public static final String COLUMN_NAME_BEN_NAME2 = "BEN_NAME2";
    public static final String COLUMN_NAME_BEN_NAME3 = "BEN_NAME3";
    public static final String COLUMN_NAME_BEN_NAME4 = "BEN_NAME4";
    public static final String COLUMN_NAME_DETAIL_CHARG = "DETAIL_CHARG";
    public static final String COLUMN_NAME_SEND_TO_REC1 = "SEND_TO_REC1";
    public static final String COLUMN_NAME_SEND_TO_REC2 = "SEND_TO_REC2";
    public static final String COLUMN_NAME_SEND_TO_REC3 = "SEND_TO_REC3";
    public static final String COLUMN_NAME_SEND_TO_REC4 = "SEND_TO_REC4";
    public static final String COLUMN_NAME_REGAL_REP1 = "REGAL_REP1";
    public static final String COLUMN_NAME_REGAL_REP2 = "REGAL_REP2";
    public static final String COLUMN_NAME_PAY_ACCOUNT = "PAY_ACCOUNT";
    public static final String COLUMN_NAME_IS_PROPOSAL = "IS_PROPOSAL";
    public static final String COLUMN_NAME_GENERATE_FILE_ALIAS_ID = "GENERATE_FILE_ALIAS_ID";

    private String fileName;
    private String sendRef;
    private String bankCode;
    private String transferType;
    private String valueDate;
    private String currency;
    private BigDecimal setAmount;
    private String ordAcct;
    private String ordName1;
    private String ordName2;
    private String ordName3;
    private String ordName4;
    private String sendCode;
    private String sendAcct;
    private String recCode;
    private String recAcct;
    private String recInsti;
    private String benAcct;
    private String benName1;
    private String benName2;
    private String benName3;
    private String benName4;
    private String detailCharg;
    private String sendToRec1;
    private String sendToRec2;
    private String sendToRec3;
    private String sendToRec4;
    private String regalRep1;
    private String regalRep2;
    private String payAccount;
    private boolean proposal;
    private Long generateFileAliasId;

    // Pull from child
    private SwiftFileLog swiftFileLog;

    // log sum
    private boolean sumFile;
    private BigDecimal totalTransferAmount;

    @Override
    public String toString() {
        return StringUtils.rightPad(null == fileName ? "" : fileName, 16, " ") +
                StringUtils.rightPad(null == bankCode ? "" : bankCode, 4, " ") +
                StringUtils.rightPad(null == transferType ? "" : transferType, 3, " ") +
                StringUtils.rightPad(null == valueDate ? "" : valueDate, 10, " ") +
                StringUtils.rightPad(null == currency ? "" : currency, 3, " ") +
                StringUtils.rightPad(null == setAmount ? "" : String.format("%.2f", setAmount), 15, " ") +
                StringUtils.rightPad(null == ordAcct ? "" : ordAcct, 34, " ") +
                StringUtils.rightPad(null == ordName1 ? "" : ordName1, 35, " ");
    }

    public SwiftFile(SwiftFile file) {
        this.fileName = file.getFileName();
        this.sendRef = file.getSendRef();
        this.bankCode = file.getBankCode();
        this.transferType = file.getTransferType();
        this.valueDate = file.getValueDate();
        this.currency = file.getCurrency();
        this.setAmount = file.getSetAmount();
        this.ordAcct = file.getOrdAcct();
        this.ordName1 = file.getOrdName1();
        this.ordName2 = file.getOrdName2();
        this.ordName3 = file.getOrdName3();
        this.ordName4 = file.getOrdName4();
        this.sendCode = file.getSendCode();
        this.sendAcct = file.getSendAcct();
        this.recCode = file.getRecCode();
        this.recAcct = file.getRecAcct();
        this.recInsti = file.getRecInsti();
        this.benAcct = file.getBenAcct();
        this.benName1 = file.getBenName1();
        this.benName2 = file.getBenName2();
        this.benName3 = file.getBenName3();
        this.benName4 = file.getBenName4();
        this.detailCharg = file.getDetailCharg();
        this.sendToRec1 = file.getSendToRec1();
        this.sendToRec2 = file.getSendToRec2();
        this.sendToRec3 = file.getSendToRec3();
        this.sendToRec4 = file.getSendToRec4();
        this.regalRep1 = file.getRegalRep1();
        this.regalRep2 = file.getRegalRep2();
        this.payAccount = file.getPayAccount();
        this.proposal = file.isProposal();
        this.generateFileAliasId = file.getGenerateFileAliasId();
        this.swiftFileLog = file.getSwiftFileLog();
        this.sumFile = file.isSumFile();
        this.totalTransferAmount = file.getTotalTransferAmount();
    }
}

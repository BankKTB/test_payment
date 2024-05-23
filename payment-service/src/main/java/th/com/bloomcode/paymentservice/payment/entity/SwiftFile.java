package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SWIFT_FILE")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SwiftFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SWIFT_FILE_SEQ")
    @SequenceGenerator(sequenceName = "SWIFT_FILE_SEQ", allocationSize = 1, name = "SWIFT_FILE_SEQ")
    private Long id;

    @Column(name = "SEND_REF", length = 16)
    private String sendRef;

    @Column(name = "BANK_CODE", length = 4)
    private String bankCode;

    @Column(name = "TRANSFER_TYPE", length = 3)
    private String transferType;

    @Column(name = "VALUE_DATE", length = 10)
    private String valueDate;

    @Column(name = "CURRENCY", length = 3)
    private String currency;

    @Column(name = "SET_AMOUNT", length = 15)
    private BigDecimal setAmount;

    @Column(name = "ORD_ACCT", length = 34)
    private String ordAcct;

    @Column(name = "ORD_NAME1", length = 35)
    private String ordName1;

    @Column(name = "ORD_NAME2", length = 35)
    private String ordName2;

    @Column(name = "ORD_NAME3", length = 35)
    private String ordName3;

    @Column(name = "ORD_NAME4", length = 35)
    private String ordName4;

    @Column(name = "SEND_CODE", length = 1)
    private String sendCode;

    @Column(name = "SEND_ACCT", length = 34)
    private String sendAcct;

    @Column(name = "REC_CODE", length = 1)
    private String recCode;

    @Column(name = "REC_ACCT", length = 34)
    private String recAcct;

    @Column(name = "REC_INSTI", length = 11)
    private String recInsti;

    @Column(name = "BEN_ACCT", length = 34)
    private String benAcct;

    @Column(name = "BEN_NAME1", length = 35)
    private String benName1;

    @Column(name = "BEN_NAME2", length = 35)
    private String benName2;

    @Column(name = "BEN_NAME3", length = 35)
    private String benName3;

    @Column(name = "BEN_NAME4", length = 35)
    private String benName4;

    @Column(name = "DETAIL_CHARG", length = 3)
    private String detailCharg;

    @Column(name = "SEND_TO_REC1", length = 35)
    private String sendToRec1;

    @Column(name = "SEND_TO_REC2", length = 35)
    private String sendToRec2;

    @Column(name = "SEND_TO_REC3", length = 35)
    private String sendToRec3;

    @Column(name = "SEND_TO_REC4", length = 29)
    private String sendToRec4;

    @Column(name = "REGAL_REP1", length = 35)
    private String regalRep1;

    @Column(name = "REGAL_REP2", length = 35)
    private String regalRep2;

    @Column(name = "PAY_ACCOUNT", length = 2)
    private String payAccount;

    @Column(name = "IS_PROPOSAL")
    private boolean proposal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SWIFT_FILE_LOG_ID", referencedColumnName = "id")
    private SwiftFileLog swiftFileLog;

    @ManyToOne(fetch = FetchType.LAZY)
    private GenerateFileAlias generateFileAlias;

    @Override
    public String toString() {
        return StringUtils.rightPad(null == sendRef ? "" : sendRef, 16, " ") +
                StringUtils.rightPad(null == bankCode ? "" : bankCode, 4, " ") +
                StringUtils.rightPad(null == transferType ? "" : transferType, 3, " ") +
                StringUtils.rightPad(null == valueDate ? "" : valueDate, 10, " ") +
                StringUtils.rightPad(null == currency ? "" : currency, 3, " ") +
                StringUtils.rightPad(null == setAmount ? "" : String.format("%.2f", setAmount), 15, " ") +
                StringUtils.rightPad(null == ordAcct ? "" : ordAcct, 34, " ") +
                StringUtils.rightPad(null == ordName1 ? "" : ordName1, 35, " ");
//                ordName2 +
//                ordName3 +
//                ordName4 +
//                sendCode +
//                sendAcct +
//                recCode +
//                recAcct +
//                recInsti +
//                benAcct +
//                benName1 +
//                benName2 +
//                benName3 +
//                benName4 +
//                detailCharg +
//                sendToRec1 +
//                sendToRec2 +
//                sendToRec3 +
//                sendToRec4 +
//                regalRep1 +
//                regalRep2;
    }
}

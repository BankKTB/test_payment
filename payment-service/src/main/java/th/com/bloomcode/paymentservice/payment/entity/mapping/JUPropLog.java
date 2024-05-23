package th.com.bloomcode.paymentservice.payment.entity.mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import th.com.bloomcode.paymentservice.payment.entity.GIROFileTrailer;
import th.com.bloomcode.paymentservice.payment.entity.InhouseFileHeader;
import th.com.bloomcode.paymentservice.payment.entity.JUHead;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "JU_PROP_LOG")
@Data
public class JUPropLog implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "REF_RUNNING")
    private Long refRunning;
    @Column(name = "REF_LINE")
    private int refLine;
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;
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
    private Date transferDate;
    //    private String vendor;
//    private String bankKey;
//    private String vendorBankAccount;
//    private String transferLevel;
    @Column(name = "PAY_ACCOUNT")
    private String payAccount;
    //    private String payingCompCode;
//    private String paymentDocument;
//    private String paymentFiscalYear;
//    private String paymentCompCode;
//
//    private String fiscalYear;
//    private String fiArea;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
//    private BigDecimal bankFee;
//    private BigDecimal creditMemoAmount;
//    private Timestamp cancelDate;
}

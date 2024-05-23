package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReturnLogDetail {
    private Long id;
    private String logType;
    private Long refRunning;
    private int refLine;
    private Date paymentDate;
    private String paymentName;
    private String accountNoFrom;
    private String accountNoTo;
    private String fileType;
    private String fileName;
    private Date transferDate;
    private String vendor;
    private BigDecimal amount;
    private BigDecimal bankFee;
    private String fileStatus;
    private String documentNo;
    private String compCode;
    private String fiscalYear;
    private String accountDocNo;
}

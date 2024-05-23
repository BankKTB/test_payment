package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class GenerateFileAliasResponse {


    @Id
    @Column(name = "PAYMENT_ALIAS_ID")
    private Long paymentAliasId;
    @Column(name = "PAYMENT_DATE")
    private Date generateFileDate;
    @Column(name = "PAYMENT_NAME")
    private String generateFileName;
    @Column(name = "PAYMENT_RUN_STATUS")
    private String paymentRunStatus;
    @Column(name = "SWIFT_AMOUNT_DAY")
    private int swiftAmountDay;
    @Column(name = "SWIFT_DATE")
    private Date swiftDate;
    @Column(name = "SMART_AMOUNT_DAY")
    private int smartAmountDay;
    @Column(name = "SMART_DATE")
    private Date smartDate;
    @Column(name = "GIRO_AMOUNT_DAY")
    private int giroAmountDay;
    @Column(name = "GIRO_DATE")
    private Date giroDate;
    @Column(name = "INHOUSE_AMOUNT_DAY")
    private int inHouseAmountDay;
    @Column(name = "INHOUSE_DATE")
    private Date inHouseDate;
    @Column(name = "GENERATE_FILE_RUN_STATUS")
    private String generateFileRunStatus;
    @Column(name = "GENERATE_FILE_ID")
    private Long generateFileId;

    @Column(name = "GENERATE_FILE_RUN_STATUS_NAME")
    private String generateFileRunStatusName;
    @Column(name = "PAYMENT_RUN_STATUS_NAME")
    private String paymentRunStatusName;

}
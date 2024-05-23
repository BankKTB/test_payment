package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SUM_FILE_CONDITION")
@Data
public class SumFileCondition {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUM_FILE_CONDITION_SEQ")
    @SequenceGenerator(sequenceName = "SUM_FILE_CONDITION_SEQ", allocationSize = 1, name = "SUM_FILE_CONDITION_SEQ")
    private Long id;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "VENDOR_FROM")
    private String vendorFrom;

    @Column(name = "VENDOR_TO")
    private String vendorTo;

    @Column(name = "VALID_FROM")
    private Timestamp validFrom;

    @Column(name = "VALID_TO")
    private Timestamp validTo;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "UPDATED")
    private Timestamp updated;

    @Column(name = "CREATE_BY")
    private String createdBy;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "ACTIVE")
    private boolean active;
}

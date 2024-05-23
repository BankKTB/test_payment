package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENT_INDEPENDENT_CONFIG")
@Data
public class PaymentIndependentConfig {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_INDEPENDENT_CONFIG_SEQ")
    @SequenceGenerator(sequenceName = "PAYMENT_INDEPENDENT_CONFIG_SEQ", allocationSize = 1, name = "PAYMENT_INDEPENDENT_CONFIG_SEQ")
    private Long id;

    private String fieldName;
    private String dbName;
    private String dataType;
    private String tableName;
    private String groupName;

    @Column(name = "IS_STANDARD")
    private boolean standard;

    @Column(name = "IS_ACTIVE")
    private boolean active;

}

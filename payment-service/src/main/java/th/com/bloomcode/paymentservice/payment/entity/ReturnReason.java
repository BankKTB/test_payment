package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "RETURN_REASON")
@Data
public class ReturnReason {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RETURN_REASON_SEQ")
    @SequenceGenerator(sequenceName = "RETURN_REASON_SEQ", allocationSize = 1, name = "RETURN_REASON_SEQ")
    private Long id;

    private String reasonNo;
    private String description;


}

package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TH_CAPAYMENTMETHOD")
@Data
public class PaymentMethod {

    @Id
    @Column(name = "TH_CAPAYMENTMETHOD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

}

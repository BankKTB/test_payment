package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "C_CURRENCY")
@Data
public class Currency {

    @Id
    @Column(name = "C_CURRENCY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "DESCRIPTION")
    private String description;

}

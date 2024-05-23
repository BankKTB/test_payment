package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TH_BGPAYMENTCENTER")
@Data
public class PaymentCenter {

    @Id
    @Column(name = "TH_BGPAYMENTCENTER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ISACTIVE")
    private boolean isActive;

    @Column(name = "AREA")
    private String bgBudgetArea;

    @Column(name = "COMPANYCODE")
    private String companyCode;
}

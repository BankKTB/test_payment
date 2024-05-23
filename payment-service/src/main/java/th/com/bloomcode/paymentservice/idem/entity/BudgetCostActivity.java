package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TH_BGCOSTACTIVITY")
@Data
public class BudgetCostActivity {

    @Id
    @Column(name = "TH_BGCOSTACTIVITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COMPCODE")
    private String companyCode;
}

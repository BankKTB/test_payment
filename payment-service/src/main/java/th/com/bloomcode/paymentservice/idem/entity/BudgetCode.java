package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TH_BGBUDGETCODE")
@Data
public class BudgetCode {

    @Id
    @Column(name = "TH_BGBUDGETCODE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FISCALYEAR")
    private String fiscalYear;

    @Column(name = "BUDGETACCOUNT")
    private String budgetAccount;

    @Column(name = "FUNDSOURCE")
    private String fundSource;

    @Column(name = "GFMISBUDGETCODE")
    private String gfmisBudgetCode;


}

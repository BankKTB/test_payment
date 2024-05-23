package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TH_CASUBACCOUNT")
@Data
public class SubAccount {

    @Id
    @Column(name = "TH_CASUBACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CASUBACCOUNTCATEGORY")
    private String caSubAccountCategory;

    @Column(name = "SUBACCOUNTOWNER")
    private String subAccountOwner;

    @Column(name = "COMPANYCODE")
    private String companyCode;

    @Column(name = "AREA")
    private String area;
}

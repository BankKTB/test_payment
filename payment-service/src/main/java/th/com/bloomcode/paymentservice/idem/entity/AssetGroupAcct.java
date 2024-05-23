package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "A_ASSET_GROUP_ACCT")
@Data
public class AssetGroupAcct {

    @Id
    @Column(name = "A_ASSET_GROUP_ACCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String valueCode;

    @Column(name = "DEPRECIATIONAREA")
    private String depreciationArea;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DEPRECIATIONMETHOD")
    private String depreciationMethod;

    @Column(name = "USELIFEYEARS")
    private int usableLifeYears;

    @Column(name = "USELIFEMONTHS")
    private int usableLifeMonths;
}

package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TH_CAHOUSEBANK")
@Data
public class HouseBank {

    @Id
    @Column(name = "TH_CAHOUSEBANK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "COUNTRYCODE")
    private String countryCode;

    @Column(name = "COUNTRYNAME")
    private String countryNameEn;

    @Column(name = "BANKBRANCH")
    private String bankBranch;

    @Column(name = "DESCRIPTION")
    private String bankName;

    @Column(name = "SWIFTCODE")
    private String swiftCode;

    @Column(name = "ADDRESS1")
    private String address1;

    @Column(name = "ADDRESS2")
    private String address2;

    @Column(name = "ADDRESS3")
    private String address3;

    @Column(name = "ADDRESS4")
    private String address4;

    @Column(name = "ADDRESS5")
    private String address5;

}

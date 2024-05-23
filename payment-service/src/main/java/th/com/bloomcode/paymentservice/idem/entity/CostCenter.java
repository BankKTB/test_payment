package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TH_BGCOSTCENTER")
@Data
public class CostCenter implements Serializable {

    @Id
    @Column(name = "TH_BGCOSTCENTER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COMPANYCODE")
    private String companyCode;

    @Column(name = "PAYMENTCENTER")
    private String paymentCenter;

    @Column(name = "AREA")
    private String area;

}

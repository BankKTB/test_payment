package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TH_CAINCOME")
@Data
public class Income implements Serializable {

    @Id
    @Column(name = "TH_CAINCOME_ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SHORTDESCRIPTION")
    private String description;

    @Column(name = "FUNDSOURCEPATTERN")
    private String fundSourcePattern;

    @Column(name = "FROMACCOUNT")
    private String fromAccount;

    @Column(name = "TOACCOUNT")
    private String toAccount;
}

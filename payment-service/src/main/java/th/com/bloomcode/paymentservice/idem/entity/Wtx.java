package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.util.BooleanToStringConverter;

import javax.persistence.*;

@Entity
@Table(name = "TH_CAWITHHOLDINGTAX")
@Data
public class Wtx {

    @Id
    @Column(name = "TH_CAWITHHOLDINGTAX_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "WTXCODE")
    private String wtxCode;

    @Column(name = "GLACCOUNT")
    private String glAccount;


}

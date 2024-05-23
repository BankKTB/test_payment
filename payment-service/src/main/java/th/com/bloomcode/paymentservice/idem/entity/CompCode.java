package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.util.BooleanToStringConverter;

import javax.persistence.*;

@Entity
@Table(name = "TH_CACOMPCODE")
@Data
public class CompCode {

    @Id
    @Column(name = "TH_CACOMPCODE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "OLDCOMPCODE")
    private String oldCompCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MINISTRY")
    private String ministry;

    @Column(name = "ISACTIVE")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean active;
}

package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.util.BooleanToStringConverter;

import javax.persistence.*;

@Entity
@Table(name = "TH_BGFUNDSOURCE")
@Data
public class FundSource {

    @Id
    @Column(name = "TH_BGFUNDSOURCE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FUND_TYPE")
    private String fundType;

    @Column(name = "ISACTIVE")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean active;
}

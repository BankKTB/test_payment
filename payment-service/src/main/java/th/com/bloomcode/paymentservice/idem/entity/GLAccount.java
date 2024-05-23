package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.util.BooleanToStringConverter;

import javax.persistence.*;

@Entity
@Table(name = "C_ELEMENTVALUE")
@Data
public class GLAccount {
    @Id
    @Column(name = "C_ELEMENTVALUE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ISACTIVE")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean active;
}

package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.util.BooleanToStringConverter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "C_NONBUSINESSDAY")
@Data
public class NonBusinessDay {
    @Id
    @Column(name = "C_NONBUSINESSDAY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ISACTIVE")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean active;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DATE1")
    private Timestamp date;


}

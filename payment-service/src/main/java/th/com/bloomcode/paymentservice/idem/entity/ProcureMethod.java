package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TH_MM_PROCUREMETHOD")
@Data
public class ProcureMethod {

    @Id
    @Column(name = "TH_MM_PROCUREMETHOD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;
}

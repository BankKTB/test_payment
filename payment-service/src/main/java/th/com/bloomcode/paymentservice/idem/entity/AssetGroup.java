package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "A_ASSET_GROUP")
@Data
public class AssetGroup {

    @Id
    @Column(name = "A_ASSET_GROUP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VALUECODE")
    private String valueCode;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HELP")
    private String help;

}

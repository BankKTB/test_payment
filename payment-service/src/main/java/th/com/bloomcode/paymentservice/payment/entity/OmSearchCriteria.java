package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "OM_SEARCH_CRITERIA")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class OmSearchCriteria {

    @Column(name = "ID")
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OM_SEARCH_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "OM_SEARCH_CRITERIA_SEQ", allocationSize = 1, name = "OM_SEARCH_CRITERIA_SEQ")
    private Long id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "ROLE")
    private String role;
    @Column(name = "JSON_TEXT")
    private String jsonText;
    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CREATE_DATE")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "USER_ONLY")
    private boolean userOnly;

}

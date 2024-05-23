package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "TH_QUARTZ_CUSTOM_TRIGGERS")
@EntityListeners(AuditingEntityListener.class)
public class CustomTriggers implements Serializable {

  @Id
  @Column(name = "TH_QUARTZ_CUSTOM_TRIGGERS_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TH_QUARTZ_CUSTOM_TRIGGERS_SEQ")
  @SequenceGenerator(
      sequenceName = "TH_QUARTZ_CUSTOM_TRIGGERS_SEQ",
      allocationSize = 1,
      name = "TH_QUARTZ_CUSTOM_TRIGGERS_SEQ")
  private Long id;

  private String state;
  private String status;
  private Long triggerAtInMillis;
  private Long startAtInMillis;
  private Long endAtInMillis;
  private Long paymentAliasId;
  private Long paymentType;
  @CreatedDate private Timestamp created;
  @LastModifiedDate private Timestamp updated;
  private String createdBy;
  private String updatedBy;
  private Long delay;
  private Long duration;
  private Long parentId;
  private String jobType;
  private Timestamp jobDate;

  @ManyToOne()
  @JoinColumn(
          updatable = false,
          insertable = false,
          name = "paymentAliasId",
          referencedColumnName = "id")
  private PaymentAlias paymentAlias;
}

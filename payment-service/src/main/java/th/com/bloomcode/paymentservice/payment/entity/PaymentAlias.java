package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PAYMENT_ALIAS")
@Getter
@Setter
public class PaymentAlias implements Serializable {

  @Id
  //    @GeneratedValue(strategy = GenerationType.IDENTITY)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_ALIAS_SEQ")
  @SequenceGenerator(
      sequenceName = "PAYMENT_ALIAS_SEQ",
      allocationSize = 1,
      name = "PAYMENT_ALIAS_SEQ")
  private Long id;

  @NotNull(message = "Payment date is required")
  private Date paymentDate;

  @NotEmpty(message = "Payment name is required")
  private String paymentName;

  private String parameterStatus;
  private String proposalStatus;
  private String runStatus;
  private String documentStatus;
  private String generateStatus;
  private Long paymentCreated;
  private Long paymentPosted;
  private String jsonText;

  private Integer proposalTotalDocument = 0;
  private Integer proposalSuccessDocument = 0;
  private Date proposalScheduleDate;
  //	private Date proposalScheduleTime;

  private Integer runTotalDocument = 0;
  private Integer runSuccessDocument = 0;
  private Date runScheduleDate;
  //	private Date runScheduleTime;

  private Long proposalTriggersId;
  private Long paymentTriggersId;
  private Timestamp proposalStart;
  private Timestamp proposalEnd;
  private Timestamp runStart;
  private Timestamp runEnd;
  private Integer runReverseDocument = 0;
  private Timestamp idemEnd;
  private Integer idemReversePaymentReply = 0;
  private Integer idemCreatePaymentReply = 0;
  private String proposalJobStatus;
  private String runJobStatus;
  private Integer runRepairDocument = 0;
  private Integer runErrorDocument = 0;
  private Timestamp created;
  //  @OneToOne(mappedBy = "paymentAlias", cascade = CascadeType.ALL,
  //          fetch = FetchType.LAZY, optional = false)
  //  private PaymentParameterConfig paymentParameterConfig;
  //    @OneToMany(mappedBy = "paymentAlias", cascade = CascadeType.ALL)
  //    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
  //    private List<SmartFileLog> smartFileLogs;

  //    @OneToOne(mappedBy = "paymentAlias", cascade = CascadeType.ALL,
  //            fetch = FetchType.LAZY, optional = false)
  //    private ProposalLogHeader proposalLogHeader;
  @OneToMany(
      targetEntity = CustomTriggers.class,
      mappedBy = "paymentAlias",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY)
  @JsonIgnore
  private List<CustomTriggers> customTriggers = new ArrayList<>();
}

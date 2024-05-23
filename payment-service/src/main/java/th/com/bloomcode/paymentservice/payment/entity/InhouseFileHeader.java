package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "INHOUSE_FILE_HEADER")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InhouseFileHeader {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INHOUSE_FILE_HEADER_SEQ")
  @SequenceGenerator(sequenceName = "INHOUSE_FILE_HEADER_SEQ", allocationSize = 1, name = "INHOUSE_FILE_HEADER_SEQ")
  private Long id;

  @Column(name = "REC_TYPE")
  private String recType;

  @Column(name = "SEQ_NO")
  private String seqNo;

  @Column(name = "BANK_CODE")
  private String bankCode;

  @Column(name = "COMP_ACC_NO")
  private String compAccNo;

  @Column(name = "COMP_NAME")
  private String compName;

  @Column(name = "POST_DATE")
  private String postDate;

  @Column(name = "RUNNING")
  private String running;

  @Column(name = "BATCH")
  private String batch;

  @Column(name = "EFF_DATE")
  private String effDate;

  @Column(name = "FILLER")
  private String filler;

  @Column(name = "IS_PROPOSAL")
  private boolean proposal;

  @Column(name = "USER_TREF")
  private String userTRef;

  @OneToMany(mappedBy = "inhouseFileHeader")
  private List<InhouseFileDetail> inhouseFileDetails;

  @OneToOne(mappedBy = "inhouseFileHeader", cascade = CascadeType.ALL,
          fetch = FetchType.LAZY)
  private InhouseFileTrailer inhouseFileTrailer;

  @ManyToOne(fetch = FetchType.LAZY)
  private GenerateFileAlias generateFileAlias;

  @Override
  public String toString() {
    return recType + seqNo + bankCode + compAccNo + compName + postDate + getBatch() + effDate + filler;
  }
}

package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "INHOUSE_FILE_TRAILER")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InhouseFileTrailer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INHOUSE_FILE_TRAILER_SEQ")
  @SequenceGenerator(sequenceName = "INHOUSE_FILE_TRAILER_SEQ", allocationSize = 1, name = "INHOUSE_FILE_TRAILER_SEQ")
  private Long id;

  @Column(name = "FILE_TYPE")
  private String fileType;

  @Column(name = "PAYMENT_METHOD")
  private String paymentMethod;

  @Column(name = "NUM_DR")
  private int numDr;

  @Column(name = "AMT_DR")
  private BigDecimal amtDr;

  @Column(name = "NUM_CR")
  private int numCr;

  @Column(name = "AMT_CR")
  private BigDecimal amtCr;

  @Column(name = "REC_TYPE")
  private String recType;

  @Column(name = "SEQ_NO")
  private String seqNo;

  @Column(name = "BANK_CODE")
  private String bankCode;

  @Column(name = "COMP_ACC_NO")
  private String compAccNo;

  @Column(name = "NUMBER_DR")
  private String numberDr;

  @Column(name = "TOTAL_DR")
  private String totalDr;

  @Column(name = "NUMBER_CR")
  private String numberCr;

  @Column(name = "TOTAL_CR")
  private String totalCr;

  @Column(name = "FILLER")
  private String filler;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "INHOUSE_FILE_HEADER_ID")
  private InhouseFileHeader inhouseFileHeader;

  @Override
  public String toString() {
    System.out.println("InhouseFileTrailer{" +
            "id=" + id +
            ", fileType='" + fileType + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", numDr=" + numDr +
            ", amtDr=" + amtDr +
            ", numCr=" + numCr +
            ", amtCr=" + amtCr +
            ", recType='" + recType + '\'' +
            ", seqNo='" + seqNo + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", compAccNo='" + compAccNo + '\'' +
            ", numberDr='" + numberDr + '\'' +
            ", totalDr='" + totalDr + '\'' +
            ", numberCr='" + numberCr + '\'' +
            ", totalCr='" + totalCr + '\'' +
            ", filler='" + filler + '\'' +
            ", inhouseFileHeader=" + inhouseFileHeader +
            '}');
    return recType + seqNo + bankCode + compAccNo + numberDr + totalDr + numberCr + totalCr + filler;
  }
}
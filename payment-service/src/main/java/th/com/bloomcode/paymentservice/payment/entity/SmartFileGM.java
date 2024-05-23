package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SMART_FILE_GM")
@Data
public class SmartFileGM {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMART_FILE_GM_SEQ")
  @SequenceGenerator(sequenceName = "SMART_FILE_GM_SEQ", allocationSize = 1, name = "SMART_FILE_GM_SEQ")
  private Long id;

  @Column(name = "ACCOUNT")
  private String account;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "AMOUNT")
  private BigDecimal amount;

  @Column(name = "FEE")
  private BigDecimal fee;

  @Column(name = "TOTAL")
  private BigDecimal total;

  @Column(name = "TRANS_NO")
  private int transNo;
}

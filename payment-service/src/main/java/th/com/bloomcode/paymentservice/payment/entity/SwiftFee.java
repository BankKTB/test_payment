package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SWIFT_FEE")
@Data
public class SwiftFee {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SWIFT_FEE_SEQ")
    @SequenceGenerator(sequenceName = "SWIFT_FEE_SEQ", allocationSize = 1, name = "SWIFT_FEE_SEQ")
    private Long id;

    @Column(name = "BANK_KEY")
    private String bankKey;

    @Column(name = "BASE_AMOUNT")
    private BigDecimal baseAmount;

    @Column(name = "BASE_FEE")
    private BigDecimal baseFee;

    @Column(name = "VAR_AMOUNT")
    private BigDecimal varAmount;

    @Column(name = "VAR_FEE")
    private BigDecimal varFee;

    @Column(name = "MAX_FEE")
    private BigDecimal maxFee;

    @Column(name = "IS_SMART")
    private boolean smart;
}

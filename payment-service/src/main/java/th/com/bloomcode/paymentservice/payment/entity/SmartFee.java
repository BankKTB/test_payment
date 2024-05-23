package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "SMART_FEE")
@Data
public class SmartFee {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMART_FEE_SEQ")
    @SequenceGenerator(sequenceName = "SMART_FEE_SEQ", allocationSize = 1, name = "SMART_FEE_SEQ")
    private Long id;

    private String glAccount;

    @Min(value = 0, message = "ยอดเงินต้องมากกว่าหรือเท่ากับ 0")
    private BigDecimal amountMin;

    @Min(value = 0, message = "ยอดเงินต้องมากกว่าหรือเท่ากับ 0")
    private BigDecimal amountMax;

    @Min(value = 0, message = "ค่าธรรมเนียมต้องมากกว่าหรือเท่ากับ 0")
    private BigDecimal botFee;

    @Min(value = 0, message = "ค่าธรรมเนียมต้องมากกว่าหรือเท่ากับ 0")
    private BigDecimal bankFee;

    @Min(value = 0, message = "ค่าธรรมเนียมต้องมากกว่าหรือเท่ากับ 0")
    private BigDecimal samedayBotFee;

    @Min(value = 0, message = "ค่าธรรมเนียมต้องมากกว่าหรือเท่ากับ 0")
    private BigDecimal samedayBankFee;
}

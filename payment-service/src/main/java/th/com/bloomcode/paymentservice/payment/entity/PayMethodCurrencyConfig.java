package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PAY_METHOD_CURRENCY_CONFIG")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class PayMethodCurrencyConfig {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAY_METHOD_CURRENCY_CONFIG_SEQ")
    @SequenceGenerator(sequenceName = "PAY_METHOD_CURRENCY_CONFIG_SEQ", allocationSize = 1, name = "PAY_METHOD_CURRENCY_CONFIG_SEQ")
    private Long id;

    private Long payMethodConfigId;


    private String currency;
    private String currencyName;


//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(
//			  name = "paymentMethodConfigId",
//			referencedColumnName = "id", insertable = false, updatable = false)
//	private PaymentMethodConfig paymentMethodConfig;
}

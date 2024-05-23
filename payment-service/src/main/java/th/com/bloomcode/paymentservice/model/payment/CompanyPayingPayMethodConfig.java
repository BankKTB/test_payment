package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CompanyPayingPayMethodConfig extends BaseModel {
    public static final String TABLE_NAME = "COMPANY_PAYING_PAY_METHOD_CONFIG";

    public static final String COLUMN_NAME_COMPANY_PAYING_PAY_METHOD_CONFIG_ID = "COMPANY_PAYING_PAY_METHOD_CONFIG_ID";
    public static final String COLUMN_NAME_ALLOWED_BANK_ANOTHER_COUNTRY = "ALLOWED_BANK_ANOTHER_COUNTRY";
    public static final String COLUMN_NAME_ALLOWED_CURRENCY_ANOTHER_COUNTRY = "ALLOWED_CURRENCY_ANOTHER_COUNTRY";
    public static final String COLUMN_NAME_ALLOWED_PARTNER_ANOTHER_COUNTRY = "ALLOWED_PARTNER_ANOTHER_COUNTRY";
    public static final String COLUMN_NAME_ALLOWED_SINGLE_PAYMENT = "ALLOWED_SINGLE_PAYMENT";
    public static final String COLUMN_NAME_COMPANY_PAYING_ID = "COMPANY_PAYING_ID";
    public static final String COLUMN_NAME_MAXIMUM_AMOUNT = "MAXIMUM_AMOUNT";
    public static final String COLUMN_NAME_MINIMUM_AMOUNT = "MINIMUM_AMOUNT";
    public static final String COLUMN_NAME_PAY_METHOD = "PAY_METHOD";
    public static final String COLUMN_NAME_PAY_METHOD_NAME = "PAY_METHOD_NAME";

    private boolean allowedBankAnotherCountry;
    private boolean allowedCurrencyAnotherCountry;
    private boolean allowedPartnerAnotherCountry;
    private boolean allowedSinglePayment;
    private Long companyPayingId;
    private BigDecimal maximumAmount;
    private BigDecimal minimumAmount;
    private String payMethod;
    private String payMethodName;

    public CompanyPayingPayMethodConfig(Long id,
                                            boolean allowedBankAnotherCountry,
                                boolean allowedCurrencyAnotherCountry,
                                boolean allowedPartnerAnotherCountry,
                                boolean allowedSinglePayment,
                                Long companyPayingId,
                                BigDecimal maximumAmount,
                                BigDecimal minimumAmount,
                                String payMethod,
                                String payMethodName) {
        super(id);
        this.allowedBankAnotherCountry = allowedBankAnotherCountry;
        this.allowedCurrencyAnotherCountry = allowedCurrencyAnotherCountry;
        this.allowedPartnerAnotherCountry = allowedPartnerAnotherCountry;
        this.allowedSinglePayment = allowedSinglePayment;
        this.companyPayingId = companyPayingId;
        this.maximumAmount = maximumAmount;
        this.minimumAmount = minimumAmount;
        this.payMethod = payMethod;
        this.payMethodName = payMethodName;

    }
}

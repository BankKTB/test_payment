package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CompanyPaying extends BaseModel {
    public static final String TABLE_NAME = "COMPANY_PAYING";

    public static final String COLUMN_NAME_COMPANY_PAYING_ID = "COMPANY_PAYING_ID";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_COMPANY_NAME = "COMPANY_NAME";
    public static final String COLUMN_NAME_MINIMUM_AMOUNT_FOR_PAY = "MINIMUM_AMOUNT_FOR_PAY";

    private String companyCode;
    private String companyName;
    private BigDecimal minimumAmountForPay;

    public CompanyPaying(Long id, String companyCode, String companyName, BigDecimal minimumAmountForPay) {
        super(id);
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.minimumAmountForPay = minimumAmountForPay;
    }
}

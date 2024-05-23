package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class CompanyPayingBankConfig extends BaseModel {
    public static final String TABLE_NAME = "COMPANY_PAYING_BANK_CONFIG";

    public static final String COLUMN_NAME_COMPANY_PAYING_BANK_CONFIG_ID = "COMPANY_PAYING_BANK_CONFIG_ID";
    public static final String COLUMN_NAME_ACCOUNT_CODE = "ACCOUNT_CODE";
    public static final String COLUMN_NAME_COMPANY_PAYING_ID = "COMPANY_PAYING_ID";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_GL_ACCOUNT = "GL_ACCOUNT";
    public static final String COLUMN_NAME_HOUSE_BANK_KEY = "HOUSE_BANK_KEY";
    public static final String COLUMN_NAME_PAY_METHOD = "PAY_METHOD";

    private String accountCode;
    private Long companyPayingId;
    private CompanyPaying companyPaying;
    private String currency;
    private String glAccount;
    private String houseBankKey;
    private String payMethod;

    public CompanyPayingBankConfig(Long id, String accountCode, Long companyPayingId, CompanyPaying companyPaying, String currency, String glAccount, String houseBankKey, String payMethod) {
        super(id);
        this.accountCode = accountCode;
        this.companyPayingId = companyPayingId;
        this.companyPaying = companyPaying;
        this.currency = currency;
        this.glAccount = glAccount;
        this.houseBankKey = houseBankKey;
        this.payMethod = payMethod;

    }
}

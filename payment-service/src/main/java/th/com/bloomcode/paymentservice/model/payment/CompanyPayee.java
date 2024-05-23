package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class CompanyPayee extends BaseModel {
    public static final String TABLE_NAME = "COMPANY_PAYEE";

    public static final String COLUMN_NAME_COMPANY_PAYEE_ID = "COMPANY_PAYEE_ID";
    public static final String COLUMN_NAME_AMOUNT_DUE_DATE = "AMOUNT_DUE_DATE";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_COMPANY_NAME = "COMPANY_NAME";
    public static final String COLUMN_NAME_COMPANY_PAYEE_CODE = "COMPANY_PAYEE_CODE";
    public static final String COLUMN_NAME_COMPANY_PAYING_CODE = "COMPANY_PAYING_CODE";
    public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";

    private Integer amountDueDate;
    private String companyCode;
    private String companyName;
    private String companyPayeeCode;
    private String companyPayingCode;
    private String paymentMethod;

    public CompanyPayee(Long id, Integer amountDueDate, String companyCode, String companyName, String companyPayeeCode, String companyPayingCode, String paymentMethod) {
        super(id);
        this.amountDueDate = amountDueDate;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.companyPayeeCode = companyPayeeCode;
        this.companyPayingCode = companyPayingCode;
        this.paymentMethod = paymentMethod;
    }

}

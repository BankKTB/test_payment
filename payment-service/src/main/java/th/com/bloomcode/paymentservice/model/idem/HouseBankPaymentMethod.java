package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class HouseBankPaymentMethod extends BaseModel {
    public static final String TABLE_NAME = "TH_CAHOUSEBANKACCOUNT_PM";
    public static final String COLUMN_NAME_HOUSEBANKACCOUNT_PM_ID = "TH_CAHOUSEBANKACCOUNT_PM_ID";
    public static final String COLUMN_NAME_HOUSEBANK = "HOUSEBANK";
    public static final String COLUMN_NAME_BANKBRANCH = "BANKBRANCH";
    public static final String COLUMN_NAME_COUNTRYCODE = "COUNTRYCODE";
    public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENTMETHOD";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_ACCOUNTCODE = "ACCOUNTCODE";
    public static final String COLUMN_NAME_GLACCOUNT = "GLACCOUNT";
    public static final String COLUMN_NAME_BANKACCOUNTNO = "BANKACCOUNTNO";

    private String houseBank;
    private String bankBranch;
    private String countryCode;
    private String paymentMethod;
    private String currency;
    private String accountCode;
    private String glAccount;
    private String bankAccountCode;
    public HouseBankPaymentMethod(long id, String houseBank, String bankBranch, String countryCode, String paymentMethod, String currency,
                                  String accountCode, String glAccount, String bankAccountCode) {
        super(id);
        this.houseBank = houseBank;
        this.bankBranch = bankBranch;
        this.countryCode = countryCode;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
        this.accountCode = accountCode;
        this.glAccount = glAccount;
        this.bankAccountCode = bankAccountCode;
    }

    @Override
    public String toString() {
        return "HouseBankPaymentMethod{" +
                "houseBank='" + houseBank + '\'' +
                ", bankBranch='" + bankBranch + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", currency='" + currency + '\'' +
                ", accountCode='" + accountCode + '\'' +
                ", glAccount='" + glAccount + '\'' +
                ", bankAccountCode='" + bankAccountCode + '\'' +
                '}';
    }
}

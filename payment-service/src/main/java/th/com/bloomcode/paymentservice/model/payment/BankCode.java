package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class BankCode  extends BaseModel {
    public static final String TABLE_NAME = "TH_CABANK";

    public static final String COLUMN_NAME_TH_CABANK_ID = "TH_CABANK_ID";
    public static final String COLUMN_NAME_ACCOUNT_NO = "BANKACCOUNTNO";
    public static final String COLUMN_NAME_BANK_KEY = "VALUECODE";
    public static final String COLUMN_NAME_BANK_NAME = "NAME";
    public static final String COLUMN_NAME_BANK_SHORT_NAME = "SHORTNAME";
    public static final String COLUMN_NAME_INCST_CODE = "SWIFTCODE";
    public static final String COLUMN_NAME_IS_INHOUSE = "ISINHOUSEFORMAT";
    public static final String COLUMN_NAME_INHOUSE_NO = "INHOUSENO";
    public static final String COLUMN_NAME_PAY_ACCOUNT = "PAYMENTTYPE";

    private String accountNo;
    private String bankKey;
    private String bankName;
    private String bankShortName;
    private String incstCode;
    private boolean inHouse;
    private Long inHouseNo;
    private String payAccount;

    public BankCode(Long id, String accountNo, String bankKey, String bankName, String bankShortName, String incstCode, boolean inHouse, Long inHouseNo, String payAccount) {
        super(id);
        this.accountNo = accountNo;
        this.bankKey = bankKey;
        this.bankName = bankName;
        this.bankShortName = bankShortName;
        this.incstCode = incstCode;
        this.inHouse = inHouse;
        this.inHouseNo = inHouseNo;
        this.payAccount = payAccount;
    }

    @Override
    public String toString() {
        return "BankCode{" +
            "accountNo='" + accountNo + '\'' +
            ", bankKey='" + bankKey + '\'' +
            ", bankName='" + bankName + '\'' +
            ", bankShortName='" + bankShortName + '\'' +
            ", incstCode='" + incstCode + '\'' +
            ", inHouse=" + inHouse +
            ", inHouseNo=" + inHouseNo +
            ", payAccount='" + payAccount + '\'' +
            '}';
    }
}

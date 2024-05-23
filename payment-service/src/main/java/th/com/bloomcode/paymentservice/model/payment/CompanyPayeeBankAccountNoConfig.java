package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.model.idem.HouseBank;

@Getter
@Setter
@NoArgsConstructor
public class CompanyPayeeBankAccountNoConfig extends BaseModel {
    public static final String TABLE_NAME = "COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG";

    public static final String COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID = "ID";
    public static final String COLUMN_NAME_ACCOUNT_CODE = "ACCOUNT_CODE";
    public static final String COLUMN_NAME_ACCOUNT_DESCRIPTION = "ACCOUNT_DESCRIPTION";
    public static final String COLUMN_NAME_ADDRESS1 = "ADDRESS1";
    public static final String COLUMN_NAME_ADDRESS2 = "ADDRESS2";
    public static final String COLUMN_NAME_ADDRESS3 = "ADDRESS3";
    public static final String COLUMN_NAME_ADDRESS4 = "ADDRESS4";
    public static final String COLUMN_NAME_ADDRESS5 = "ADDRESS5";
    public static final String COLUMN_NAME_BANK_ACCOUNT_NO = "BANK_ACCOUNT_NO";
    public static final String COLUMN_NAME_BANK_ACCOUNT_NO_ETC = "BANK_ACCOUNT_NO_ETC";
    public static final String COLUMN_NAME_BANK_BRANCH = "BANK_BRANCH";
    public static final String COLUMN_NAME_BANK_NAME = "BANK_NAME";
    public static final String COLUMN_NAME_CITY = "CITY";
    public static final String COLUMN_NAME_COUNTRY = "COUNTRY";
    public static final String COLUMN_NAME_COUNTRY_NAME_EN = "COUNTRY_NAME_EN";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_GL_ACCOUNT = "GL_ACCOUNT";
    public static final String COLUMN_NAME_HOUSE_BANK_KEY = "HOUSE_BANK_KEY";
    public static final String COLUMN_NAME_HOUSE_BANK_KEY_ID = "HOUSE_BANK_KEY_ID";
    public static final String COLUMN_NAME_KEY_CONTROL = "KEY_CONTROL";
    public static final String COLUMN_NAME_SWIFT_CODE = "SWIFT_CODE";
    public static final String COLUMN_NAME_ZIP_CODE = "ZIP_CODE";

    private String accountCode;
    private String accountDescription;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String bankAccountNo;
    private String bankAccountNoEtc;
    private String bankBranch;
    private String bankName;
    private String city;
    private String country;
    private String countryNameEn;
    private String currency;
    private String glAccount;
    private String HouseBankKey;
    private Long HouseBankKeyId;
    private HouseBank houseBank;
    private String swiftCode;
    private String keyControl;
    private String zipCode;

    public CompanyPayeeBankAccountNoConfig(Long id, String accountCode,
    String accountDescription,
    String address1,
    String address2,
    String address3,
    String address4,
    String address5,
    String bankAccountNo,
    String bankAccountNoEtc,
    String bankBranch,
    String bankName,
    String city,
    String country,
    String countryNameEn,
    String currency,
    String glAccount,
    String HouseBankKey,
    Long HouseBankKeyId,
    HouseBank houseBank,
    String swiftCode,
    String keyControl,
    String zipCode) {
        super(id);
        this.accountCode = accountCode;
        this.accountDescription = accountDescription;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
        this.bankAccountNo = bankAccountNo;
        this.bankAccountNoEtc = bankAccountNoEtc;
        this.bankBranch = bankBranch;
        this.bankName = bankName;
        this.city = city;
        this.country = country;
        this.countryNameEn = countryNameEn;
        this.currency = currency;
        this.glAccount = glAccount;
        this.HouseBankKey = HouseBankKey;
        this.houseBank = houseBank;
        this.HouseBankKeyId = HouseBankKeyId;
        this.swiftCode = swiftCode;
        this.keyControl = keyControl;
        this.zipCode = zipCode;
    }
}

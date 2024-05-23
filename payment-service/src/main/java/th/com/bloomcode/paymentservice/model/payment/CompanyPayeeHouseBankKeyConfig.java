package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class CompanyPayeeHouseBankKeyConfig  extends BaseModel {
    public static final String TABLE_NAME = "COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG";

    public static final String COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID = "ID";
    public static final String COLUMN_NAME_ADDRESS1 = "ADDRESS1";
    public static final String COLUMN_NAME_ADDRESS2 = "ADDRESS2";
    public static final String COLUMN_NAME_ADDRESS3 = "ADDRESS3";
    public static final String COLUMN_NAME_ADDRESS4 = "ADDRESS4";
    public static final String COLUMN_NAME_ADDRESS5 = "ADDRESS5";
    public static final String COLUMN_NAME_BANK_BRANCH = "BANK_BRANCH";
    public static final String COLUMN_NAME_BANK_NAME = "BANK_NAME";
    public static final String COLUMN_NAME_CITY = "CITY";
    public static final String COLUMN_NAME_COMPANY_PAYEE_ID = "COMPANY_PAYEE_ID";
    public static final String COLUMN_NAME_COUNTRY = "COUNTRY";
    public static final String COLUMN_NAME_COUNTRY_NAME_EN = "COUNTRY_NAME_EN";
    public static final String COLUMN_NAME_HOUSE_BANK_KEY = "HOUSE_BANK_KEY";
    public static final String COLUMN_NAME_SWIFT_CODE = "SWIFT_CODE";
    public static final String COLUMN_NAME_ZIP_CODE = "ZIP_CODE";

    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String bankBranch;
    private String bankName;
    private String city;
    private Long companyPayeeId;
    private CompanyPayee companyPayee;
    private String country;
    private String countryNameEn;
    private String houseBankKey;
    private String swiftCode;
    private String zipCode;

    public CompanyPayeeHouseBankKeyConfig(Long id,
                                           String address1,
                                           String address2,
                                           String address3,
                                           String address4,
                                           String address5,
                                           String bankBranch,
                                           String bankName,
                                           String city,
                                           Long companyPayeeId,
                                          CompanyPayee companyPayee,
                                           String country,
                                           String countryNameEn,
                                           String houseBankKey,
                                           String swiftCode,
                                           String zipCode) {
        super(id);
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
        this.bankBranch = bankBranch;
        this.bankName = bankName;
        this.city = city;
        this.companyPayee = companyPayee;
        this.companyPayeeId = companyPayeeId;
        this.country = country;
        this.countryNameEn = countryNameEn;
        this.houseBankKey = houseBankKey;
        this.swiftCode = swiftCode;
        this.zipCode = zipCode;
    }

}

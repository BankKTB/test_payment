package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class HouseBank extends BaseModel {
    public static final String TABLE_NAME = "TH_CAHOUSEBANK";
    public static final String COLUMN_NAME_TH_CAHOUSEBANK_ID = "TH_CAHOUSEBANK_ID";
    public static final String COLUMN_NAME_VALUE_CODE = "VALUECODE";
    public static final String COLUMN_NAME_COUNTRYCODE = "COUNTRYCODE";
    public static final String COLUMN_NAME_COUNTRYNAME = "COUNTRYNAME";
    public static final String COLUMN_NAME_SWIFTCODE = "SWIFTCODE";
    public static final String COLUMN_NAME_ADDRESS1 = "ADDRESS1";
    public static final String COLUMN_NAME_ADDRESS2 = "ADDRESS2";
    public static final String COLUMN_NAME_ADDRESS3 = "ADDRESS3";
    public static final String COLUMN_NAME_ADDRESS4 = "ADDRESS4";
    public static final String COLUMN_NAME_ADDRESS5 = "ADDRESS5";

    private String valueCode;
    private String countryCode;
    private String countryName;
    private String swiftCode;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    public HouseBank(long id, String valueCode, String countryCode, String countryName, String swiftCode,
                                  String address1, String address2, String address3, String address4, String address5) {
        super(id);
        this.valueCode = valueCode;
        this.countryCode = countryCode;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.swiftCode = swiftCode;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
    }
}

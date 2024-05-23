package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class APUserChgBankAccNo extends BaseModel implements Serializable {
    public static final String TABLE_NAME = "TH_APUserChgBankAccNo";
    public static final String COLUMN_NAME_APUSERCHGBANKACCNO_ID = "TH_APUSERCHGBANKACCNO_ID";

    public static final String COLUMN_NAME_IS_ACTIVE = "ISACTIVE";
    public static final String COLUMN_NAME_USERWEBONLINE = "USERWEBONLINE";
    public static final String COLUMN_NAME_DOCTYPENAME = "DOCTYPENAME";
    public static final String COLUMN_NAME_VALID_FROM = "VALIDFROM";
    public static final String COLUMN_NAME_VALID_TO = "VALIDTO";


    private String isActive;
    private String userWebOnline;
    private String docTypeName;
    private Timestamp validFrom;
    private Timestamp validTo;


    public APUserChgBankAccNo(Long id, String isActive, String userWebOnline, String docTypeName, Timestamp validFrom, Timestamp validTo) {
        super(id);
        this.isActive = isActive;
        this.userWebOnline = userWebOnline;
        this.docTypeName = docTypeName;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
}

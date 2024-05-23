package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PeriodControl extends BaseModel implements Serializable {
    public static final String TABLE_NAME = "TH_CAPERIODCONTROL";
    public static final String COLUMN_NAME_TH_CAPERIODCONTROL_ID = "TH_CAPERIODCONTROL_ID";
    public static final String COLUMN_NAME_PERIODSTATUS = "PERIODSTATUS";
    public static final String COLUMN_NAME_POSTINGKEY_ACCOUNTTYPE = "POSTINGKEY_ACCOUNTTYPE";
    public static final String COLUMN_NAME_PERIODNO = "PERIODNO";

    private String periodStatus;
    private String postingKeyAccountType;
    private String periodNo;

    public PeriodControl(Long id, String periodStatus, String postingKeyAccountType, String periodNo) {
        super(id);
        this.periodStatus = periodStatus;
        this.postingKeyAccountType = postingKeyAccountType;
        this.periodNo = periodNo;
    }

    @Override
    public String toString() {
        return "PeriodControl{" +
                "periodStatus='" + periodStatus + '\'' +
                ", postingKeyAccountType='" + postingKeyAccountType + '\'' +
                ", periodNo='" + periodNo + '\'' +
                '}';
    }
}

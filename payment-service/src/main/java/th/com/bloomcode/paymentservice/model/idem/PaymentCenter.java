package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCenter extends BaseModel implements Serializable {
    public static final String TABLE_NAME = "TH_BGPAYMENTCENTER";
    public static final String COLUMN_NAME_TH_BGPAYMENTCENTER_ID = "TH_BGPAYMENTCENTER_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_NAME_ISACTIVE = "ISACTIVE";
    public static final String COLUMN_NAME_TH_BGBUDGETAREA_ID = "budgetAreaId";
    public static final String COLUMN_NAME_TH_CACOMPCODE_ID = "companyCodeId";
    public static final String COLUMN_NAME_AREA = "AREA";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    private String valueCode;
    private String name;
    private String description;
    private String isActive;
    private Long budgetAreaId;
    private Long companyCodeId;
    private String area;
    private String compCode;

    public PaymentCenter(long id, String valueCode, String name, String description, String isActive, long budgetAreaId, long companyCodeId, String area,  String compCode) {
        super(id);
        this.valueCode = valueCode;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.budgetAreaId = budgetAreaId;
        this.companyCodeId = companyCodeId;
        this.area = area;
        this.compCode = compCode;
    }




}

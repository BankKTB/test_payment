package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CostCenter extends BaseModel {
    public static final String TABLE_NAME = "TH_BGCOSTCENTER";
    public static final String COLUMN_NAME_TH_BGCOSTCENTER_ID = "TH_BGCOSTCENTER_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_NAME_COMPANYCODE = "COMPANYCODE";
    public static final String COLUMN_NAME_PAYMENTCENTER = "PAYMENTCENTER";

}

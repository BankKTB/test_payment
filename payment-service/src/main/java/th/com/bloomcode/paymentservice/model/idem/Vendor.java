package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Vendor extends BaseModel implements Serializable {
    public static final String TABLE_NAME = "C_BPARTNER";
    public static final String COLUMN_NAME_C_BPARTNER_ID = "C_BPARTNER_ID";
    public static final String COLUMN_NAME_VALUE = "VALUE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_TAX_ID = "TAXID";
    public static final String COLUMN_NAME_VENDOR_GROUP = "VENDORGROUP";
    public static final String COLUMN_NAME_CONFIRM_STATUS = "CONFIRMSTATUS";
    public static final String COLUMN_NAME_VENDOR_STATUS = "VENDORSTATUS";
    public static final String COLUMN_NAME_COMP_CODE = "COMPCODE";
    public static final String COLUMN_NAME_IS_ACTIVE = "ISACTIVE";

    private Long id;
    private String taxId;
    private String valueCode;
    private String name;
    private String vendorGroup;
    private String confirmStatus;
    private String vendorStatus;
    private String companyCode;
    private boolean active;

    public Vendor(long id, String valueCode, String name, String taxId, String vendorGroup,  String confirmStatus, String vendorStatus, String companyCode, String active) {
        this.id = id;
        this.valueCode = valueCode;
        this.name = name;
        this.taxId = taxId;
        this.vendorGroup = vendorGroup;
        this.confirmStatus = confirmStatus;
        this.vendorStatus = vendorStatus;
        this.companyCode = companyCode;
        this.active = "Y".equalsIgnoreCase(active);
    }


}

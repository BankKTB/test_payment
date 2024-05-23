package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class CompanyPayingMinimalAmount extends BaseModel {
    public static final String TABLE_NAME = "TH_APMinAmtControl";

    public static final String COLUMN_NAME_TH_APMINAMTCONTROL_ID = "TH_APMINAMTCONTROL_ID";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_MINIMAL_AMOUNT = "MINIMAL_AMOUNT";


    private String companyCode;
    private BigDecimal miniMalAmount;

    public CompanyPayingMinimalAmount(Long id, String companyCode, BigDecimal miniMalAmount) {
        super(id);
        this.companyCode = companyCode;
        this.miniMalAmount = miniMalAmount;
    }
}

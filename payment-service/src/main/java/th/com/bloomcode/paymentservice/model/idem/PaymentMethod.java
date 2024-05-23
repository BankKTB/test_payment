package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethod extends BaseModel implements Serializable {
    public static final String TABLE_NAME = "TH_CAPAYMENTMETHOD";
    public static final String COLUMN_NAME_C_PAYMENT_METHOD_ID = "TH_CAPAYMENTMETHOD_ID";
    public static final String COLUMN_NAME_VALUE_CODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    private String valueCode;
    private String name;
    public PaymentMethod(long id, String valueCode, String name) {
        super(id);
        this.valueCode = valueCode;
        this.name = name;
    }
}

package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PaymentArrange extends BaseModel {
    public static final String TABLE_NAME = "TH_PAYMENT_ARRANGE";

    public static final String COLUMN_NAME_TH_PAYMENT_ARRANGE_ID = "TH_PAYMENT_ARRANGE_ID";
    public static final String COLUMN_NAME_ARRANGE_CODE = "ARRANGE_CODE";
    public static final String COLUMN_NAME_ARRANGE_NAME = "ARRANGE_NAME";
    public static final String COLUMN_NAME_ARRANGE_DESCRIPTION = "ARRANGE_DESCRIPTION";
    public static final String COLUMN_NAME_ARRANGE_JSON_TEXT = "ARRANGE_JSON_TEXT";
    public static final String COLUMN_NAME_ARRANGE_DEFAULT = "ARRANGE_DEFAULT";

    @NotNull
    @NotBlank
    private String arrangeCode;
    private String arrangeName;
    private String arrangeDescription;
    private String arrangeJsonText;
    private boolean arrangeDefault;

    public PaymentArrange(
            Long id
            , String arrangeCode
            , String arrangeName
            , String arrangeDescription
            , String arrangeJsonText
            , boolean active
            , boolean arrangeDefault
            , Timestamp created
            , String createdBy
            , Timestamp updated
            , String updatedBy
    ) {
        super(id, created, createdBy, updated, updatedBy, active);
        this.arrangeCode = arrangeCode;
        this.arrangeName = arrangeName;
        this.arrangeDescription = arrangeDescription;
        this.arrangeJsonText = arrangeJsonText;
        this.arrangeDefault = arrangeDefault;
    }

}

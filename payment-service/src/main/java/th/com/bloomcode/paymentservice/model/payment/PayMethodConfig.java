package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PayMethodConfig extends BaseModel {

    public static final String TABLE_NAME = "PAY_METHOD_CONFIG";

    public static final String COLUMN_NAME_PAY_METHOD_CONFIG_ID = "ID";
    public static final String COLUMN_NAME_IS_ALLOWED_PERSONNEL_PAYMENT = "IS_ALLOWED_PERSONNEL_PAYMENT";
    public static final String COLUMN_NAME_IS_BANK_DETAIL = "IS_BANK_DETAIL";
    public static final String COLUMN_NAME_COUNTRY = "COUNTRY";
    public static final String COLUMN_NAME_COUNTRY_NAME_EN = "COUNTRY_NAME_EN";
    public static final String COLUMN_NAME_DOCUMENT_TYPE_FOR_PAYMENT = "DOCUMENT_TYPE_FOR_PAYMENT";
    public static final String COLUMN_NAME_IS_PAY_BY = "IS_PAY_BY";
    public static final String COLUMN_NAME_PAY_METHOD = "PAY_METHOD";
    public static final String COLUMN_NAME_PAY_METHOD_NAME = "PAY_METHOD_NAME";

    private boolean allowedPersonnelPayment;
    private boolean bankDetail;
    private String country;
    private String countryNameEn;
    private String documentTypeForPayment;
    private boolean payBy;
    private String payMethod;
    private String payMethodName;

    public PayMethodConfig(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy, boolean allowedPersonnelPayment, boolean bankDetail, String country, String countryNameEn, String documentTypeForPayment, boolean payBy, String payMethod, String payMethodName) {
        super(id, created, createdBy, updated, updatedBy);
        this.allowedPersonnelPayment = allowedPersonnelPayment;
        this.bankDetail = bankDetail;
        this.country = country;
        this.countryNameEn = countryNameEn;
        this.documentTypeForPayment = documentTypeForPayment;
        this.payBy = payBy;
        this.payMethod = payMethod;
        this.payMethodName = payMethodName;
    }
}

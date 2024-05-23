package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class BankBranch extends BaseModel {
    public static final String TABLE_NAME = "C_BANK";
    public static final String COLUMN_NAME_C_BANK_ID = "C_BANK_ID";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_NAME_BANK_CODE = "BANK_CODE";
    private Long id;
    private String name;
    private String description;
    private String bankCode;

    public BankBranch(long id, String name, String description, String bankCode) {
        super(id);
        this.name = name;
        this.name = description;
        this.bankCode = bankCode;
    }

}

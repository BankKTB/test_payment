package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class DocumentType extends BaseModel {
    public static final String TABLE_NAME = "C_DOCTYPE";
    public static final String COLUMN_NAME_ID = "C_DOCTYPE_ID";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    private Long id;
    private String name;
    private String description;
    public DocumentType(long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }
}

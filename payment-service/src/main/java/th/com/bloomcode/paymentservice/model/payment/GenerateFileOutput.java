package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GenerateFileOutput extends BaseModel {

    public static final String TABLE_NAME = "GENERATE_FILE_OUTPUT";

    public static final String COLUMN_NAME_GENERATE_FILE_OUTPUT_ID = "ID";
    public static final String COLUMN_NAME_GENERATE_FILE_ALIAS_ID = "GENERATE_FILE_ALIAS_ID";
    public static final String COLUMN_NAME_JSON_TEXT = "JSON_TEXT";

    public GenerateFileOutput(Long id, Long generateFileAliasId, String jsonText) {
        super(id);
        this.generateFileAliasId = generateFileAliasId;
        this.jsonText = jsonText;
    }

    private Long generateFileAliasId;
    private String jsonText;
}

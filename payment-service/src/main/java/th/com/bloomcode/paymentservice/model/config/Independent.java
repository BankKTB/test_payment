package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Independent {

    private String fieldName;
    private String conditionField;
    private boolean optionExclude;
    private String dataType;
    private String dbName;
    private String tableName;
    private List<Condition> condition;


    @Override
    public String toString() {
        return "Independent{" +
                "fieldName='" + fieldName + '\'' +
                ", conditionField='" + conditionField + '\'' +
                ", optionExclude=" + optionExclude +
                ", dataType='" + dataType + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", condition=" + condition +
                '}';
    }
}

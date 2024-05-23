package th.com.bloomcode.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.adapter.JsonDateSerializer;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class BaseModel {

    public static final String ID = "_ID";
    public static final String SEQ = "_SEQ";

    public static final String COLUMN_NAME_CREATED = "CREATED";
    public static final String COLUMN_NAME_CREATED_BY = "CREATED_BY";
    public static final String COLUMN_NAME_UPDATED = "UPDATED";
    public static final String COLUMN_NAME_UPDATED_BY = "UPDATED_BY";
    public static final String COLUMN_NAME_ACTIVE = "ACTIVE";

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp created = new Timestamp(System.currentTimeMillis());
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updated = new Timestamp(System.currentTimeMillis());
    private String updatedBy;
    private boolean active;

    public BaseModel(Long id) {
        this.id = id;
    }

    public BaseModel(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy) {
        this.id = id;
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public BaseModel(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy, boolean active) {
        this.id = id;
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
        this.active = active;
    }
}

package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "SELECT_GROUP_DOCUMENT")
public class TestCreateDataBaseUnBlockDocumentLog {

    @Id
    private Long id;
    private Timestamp created;
    private String createdBy;
    private Timestamp updated;
    private String updatedBy;



    private String fiscalYear;
    private String defineName;
    private String jsonText;
}

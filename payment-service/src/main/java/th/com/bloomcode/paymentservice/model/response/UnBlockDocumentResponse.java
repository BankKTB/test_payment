package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class UnBlockDocumentResponse {

  List<UnBlockDocument> documentForDisplay;
  List<UnBlockDocument> documentForCheckRelation;
}

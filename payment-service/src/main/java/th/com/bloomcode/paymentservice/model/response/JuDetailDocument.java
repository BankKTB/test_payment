package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JuDetailDocument {
    private List<JuHeadDocumentResponse> listJuHead;
    private Integer summaryFile;
    private Integer summaryDocument;
    private BigDecimal summaryAmount;

}

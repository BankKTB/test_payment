package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.ProposalDocument;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProposalDocumentRequest {

    private List<ProposalDocument> proposalDocumentList;

    private WSWebInfo webInfo;

}
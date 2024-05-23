package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;

import java.util.List;

public interface PrepareRunDocumentService {

  List<PrepareRealRunDocument> findProposalDocument(Long paymentAliasId, boolean isProposal);

  List<PrepareRunDocument> findProposalDoc(Long paymentAliasId, boolean isProposal);

  PrepareRunDocument repairDocument(Long paymentAliasId, boolean isProposal);

  List<PrepareRunDocument> findChildProposalDoc(APPaymentLine apPaymentLine, boolean isProposal,Long paymentAliasId );

  List<PrepareRunDocument> findProposalDocForGen(Long paymentAliasId, boolean isProposal);

  List<PrepareRunDocument> findProposalDocForReGen(Long paymentAliasId, String fileName, boolean isProposal);
  List<PrepareRunDocument> findChild(Long paymentAliasId, String compCode, String documentNo, String fiscalYear);
}

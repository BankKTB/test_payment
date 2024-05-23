package th.com.bloomcode.paymentservice.repository.payment;

import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;

import java.util.List;

public interface PrepareRunDocumentRepository {

    List<PrepareRealRunDocument> findProposalDocument(Long paymentAliasId, boolean isProposal);

    List<PrepareRunDocument> selectDocument(Long paymentAliasId, boolean isProposal);

    List<PrepareRunDocument> findRepairDocument(Long paymentProcess, boolean isProposal);

    List<PrepareRunDocument> selectK3orKXDocument(APPaymentLine apPaymentLine, boolean isProposal,Long paymentAliasId);

    List<PrepareRunDocument> selectDocumentForGen(Long paymentAliasId, boolean isProposal);

    List<PrepareRunDocument> selectDocumentForReGen(Long paymentAliasId, String fileName, boolean isProposal);

    List<PrepareRunDocument> findChild(Long paymentAliasId, String compCode, String documentNo, String fiscalYear);
}

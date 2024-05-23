package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;

import java.util.Date;
import java.util.List;

public interface PrepareProposalDocumentRepository extends CrudRepository<PrepareProposalDocument, Long> {

    List<PrepareProposalDocument> findUnBlockDocumentCanPayByParameter(ParameterConfig parameterConfig, String username);

    List<PrepareProposalDocument> findDocumentK3OrKX(String parentDocumentNo, String parentFiscalYear,String originalCompanyCode, String username);

    Long getNextSeries(boolean isProposal);

    void updateNextSeries(Long lastNumber, boolean isProposal);

    List<PrepareSelectProposalDocument> findUnBlockDocumentCanPayByParameterNew(ParameterConfig parameterConfig, String username, Date runDate);

    void  newLockProposalDocument(List<PrepareSelectProposalDocument> prepareSelectProposalDocumentList, Long paymentAliasId);
}

package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.repository.payment.PrepareRunDocumentRepository;
import th.com.bloomcode.paymentservice.service.payment.PrepareRunDocumentService;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;

import java.util.List;

@Service
public class PrepareRunDocumentServiceImpl implements PrepareRunDocumentService {

    @Autowired
    private PrepareRunDocumentRepository prepareRunDocumentRepository;

    @Override
    public List<PrepareRealRunDocument> findProposalDocument(Long paymentAliasId, boolean isProposal) {
        return prepareRunDocumentRepository.findProposalDocument(paymentAliasId, isProposal);
    }

    @Override
    public List<PrepareRunDocument> findProposalDoc(Long paymentAliasId, boolean isProposal) {
        return prepareRunDocumentRepository.selectDocument(paymentAliasId, isProposal);
    }

    @Override
    public PrepareRunDocument repairDocument(Long paymentProcessId, boolean isProposal) {
        List<PrepareRunDocument> prepareRunDocument = prepareRunDocumentRepository.findRepairDocument(paymentProcessId, isProposal);
        if (prepareRunDocument.size() > 0) {
            return prepareRunDocument.get(0);
        } else {
            return null;
        }
    }

    public List<PrepareRunDocument> findChildProposalDoc(APPaymentLine apPaymentLine, boolean isProposal,Long paymentAliasId) {
        return prepareRunDocumentRepository.selectK3orKXDocument(apPaymentLine, isProposal, paymentAliasId);
    }

    @Override
    public List<PrepareRunDocument> findProposalDocForGen(Long paymentAliasId, boolean isProposal) {
        return prepareRunDocumentRepository.selectDocumentForGen(paymentAliasId, isProposal);
    }

    @Override
    public List<PrepareRunDocument> findProposalDocForReGen(Long paymentAliasId, String fileName, boolean isProposal) {
        return prepareRunDocumentRepository.selectDocumentForReGen(paymentAliasId, fileName, isProposal);
    }

    @Override
    public List<PrepareRunDocument> findChild(Long paymentAliasId, String compCode, String documentNo, String fiscalYear) {
        return prepareRunDocumentRepository.findChild(paymentAliasId, compCode, documentNo, fiscalYear);
    }
}

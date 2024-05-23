package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.request.ProposalDocumentRequest;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PrepareProposalDocumentService {

    List<PrepareProposalDocument> findUnBlockDocumentCanPayByParameter(ParameterConfig parameterConfig, String username);

//    ResponseEntity<Result<PaymentAlias>> proposal(Long id, WSWebInfo webInfo);
//
//    ResponseEntity<Result<PaymentAlias>> realRun(Long id, WSWebInfo webInfo);

    ResponseEntity<Result<List<PaymentReport>>> findProposalDocumentForBlock(HttpServletRequest httpServletRequest, Long id, WSWebInfo webInfo);

    void repairDocument(HttpServletRequest httpServletRequest, Long id, WSWebInfo webInfo);

    void blockDocument(ProposalDocumentRequest request);

    ResponseEntity<Result<PaymentAlias>> proposalJob(PaymentAlias paymentAlias, WSWebInfo webInfo);

    ResponseEntity<Result<PaymentAlias>> realRunJob(PaymentAlias paymentAlias, WSWebInfo webInfo);
}
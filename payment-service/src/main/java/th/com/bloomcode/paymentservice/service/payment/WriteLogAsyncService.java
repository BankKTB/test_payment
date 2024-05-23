package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.util.List;

public interface WriteLogAsyncService {
  void writeLogPrepareProposalDocumentNew(PaymentAlias paymentAlias, ParameterConfig parameterConfig, List<PrepareSelectProposalDocument> prepareSelectProposalDocuments, WSWebInfo webInfo);
  void writeLogPaymentRealRunNew(PaymentAlias paymentAlias, List<PrepareRealRunDocument> prepareRealRunDocumentList, WSWebInfo webInfo);

  void writeLogPrepareProposalDocumentJob(PaymentAlias paymentAlias, ParameterConfig parameterConfig, List<PrepareProposalDocument> prepareProposalDocuments, WSWebInfo webInfo);
  void writeLogPaymentRealRunJob(PaymentAlias paymentAlias, List<PrepareRunDocument> prepareRunDocumentList, WSWebInfo webInfo);
}

package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.util.List;

public interface PaymentAsyncService {
  void createProposalNew( PaymentAlias paymentAlias, WSWebInfo webInfo);
  void createRealRunNew(List<PrepareRealRunDocument> prepareRealRunDocumentList, PaymentAlias paymentAlias, WSWebInfo webInfo);

//  void createRealRun(List<PrepareRunDocument> prepareRunDocumentList, PaymentAlias paymentAlias, WSWebInfo webInfo);
  void createProposalJob(PaymentAlias paymentAlias, WSWebInfo webInfo);
  void createRealRunJob(List<PrepareRunDocument> prepareRunDocumentList, PaymentAlias paymentAlias, WSWebInfo webInfo);
}

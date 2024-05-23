package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.*;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;
import th.com.bloomcode.paymentservice.model.response.*;
import java.util.List;

public interface ReturnService {
    ResponseEntity<Result<ReturnLogResultResponse>> getFileReturn(ReturnGetFile request);
    ResponseEntity<Result<List<ReturnProposalLogResponse>>> getPropLogReturn(ReturnSearchProposalLog request);
    Result changeFileStatusReturn(List<ChangeFileStatusReturnRequest> request);
//    ResponseEntity<Result<List<ReturnReversePaymentResponse>>> getPropLogReverseDocPayment(ReturnSearchProposalLog request);
    Result reversePayment(List<ReversePaymentReturnRequest> request);
    ResponseEntity<Result<List<ReturnReverseInvoiceResponse>>> getPropLogReverseDocInvoice(ReturnSearchProposalLog request);
    Result reverseInvoice(List<ReverseInvoiceReturnRequest> request);

     void autoStep4ByMessageQueue(ProposalReturnLog proposalReturnLog);
}

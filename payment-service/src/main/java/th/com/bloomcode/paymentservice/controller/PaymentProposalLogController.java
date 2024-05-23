package th.com.bloomcode.paymentservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentProposalLog;
import th.com.bloomcode.paymentservice.model.payment.PaymentRealRunLog;
import th.com.bloomcode.paymentservice.service.payment.PaymentProposalLogService;
import th.com.bloomcode.paymentservice.service.payment.PaymentRealRunLogService;

@RestController
@RequestMapping(path = "/api/paymentProposalLog")
public class PaymentProposalLogController {


    private final PaymentProposalLogService paymentProposalLogService;
    private final PaymentRealRunLogService paymentRealRunLogService;

    public PaymentProposalLogController(PaymentProposalLogService paymentProposalLogService, PaymentRealRunLogService paymentRealRunLogService) {
        this.paymentProposalLogService = paymentProposalLogService;
        this.paymentRealRunLogService = paymentRealRunLogService;
    }

    @RequestMapping(path = "/search/{paymentAliasId}/{type}/{page}/{size}", method = RequestMethod.GET)
    public ResponseEntity<Result<Page<PaymentProposalLog>>> search(
            @PathVariable("paymentAliasId") Long paymentAliasId,
            @PathVariable("type") String type,
            @PathVariable("page") int page,
            @PathVariable("size") int size) {
        if (type.equalsIgnoreCase("1")) {
            return paymentProposalLogService.findAllByPaymentAliasIdAndProposal(paymentAliasId, true, page, size);
        } else {
            return paymentProposalLogService.findAllByPaymentAliasIdAndProposal(paymentAliasId, false, page, size);
        }
    }

    @RequestMapping(path = "/searchRealRun/{paymentAliasId}/{type}/{page}/{size}", method = RequestMethod.GET)
    public ResponseEntity<Result<Page<PaymentRealRunLog>>> searchRealRun(
            @PathVariable("paymentAliasId") Long paymentAliasId,
            @PathVariable("type") String type,
            @PathVariable("page") int page,
            @PathVariable("size") int size
    ) {
        if (type.equalsIgnoreCase("1")) {
            return paymentRealRunLogService.findAllByPaymentAliasId(paymentAliasId, true, page, size);
        } else {
            return paymentRealRunLogService.findAllByPaymentAliasId(paymentAliasId, false, page, size);
        }
    }

}

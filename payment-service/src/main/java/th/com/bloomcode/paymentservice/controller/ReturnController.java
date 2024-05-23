package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.*;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;
import th.com.bloomcode.paymentservice.model.response.ReturnLogResultResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnProposalLogResponse;
import th.com.bloomcode.paymentservice.service.payment.ProposalReturnLogService;
import th.com.bloomcode.paymentservice.service.payment.ReturnService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/return")
@Validated
public class ReturnController {

    private final ReturnService returnService;

    private final ProposalReturnLogService proposalReturnLogService;

    @Autowired
    public ReturnController(ReturnService returnService, ProposalReturnLogService proposalReturnLogService) {
        this.returnService = returnService;
        this.proposalReturnLogService = proposalReturnLogService;
    }
    // step 1
    @RequestMapping(path = "/autoUpdateFile", method = RequestMethod.POST)
    public ResponseEntity<Result<ReturnLogResultResponse>> getFileReturn(@RequestBody ReturnGetFile request) throws Exception {
        return this.returnService.getFileReturn(request);
    }
    // Search step2
    @RequestMapping(path = "/getPropLogReturn", method = RequestMethod.POST)
    public ResponseEntity<Result<List<ReturnProposalLogResponse>>> getPropLogReturn(@RequestBody ReturnSearchProposalLog request) throws Exception {
        return this.returnService.getPropLogReturn(request);
    }
    // update step2
    @RequestMapping(path = "/saveStatus", method = RequestMethod.POST)
    public Result changeFileStatusReturn(@RequestBody List<ChangeFileStatusReturnRequest> request) {
        return this.returnService.changeFileStatusReturn(request);
    }

    @RequestMapping(path = "/getPropLogReverseDocPayment", method = RequestMethod.POST)
    public ResponseEntity<Result<List<ProposalReturnLog>>> getPropLogReverseDocPayment(@RequestBody ReturnSearchProposalLog request) throws Exception {
        return this.proposalReturnLogService.getPropLogReverseDocPayment(request);
    }

    @RequestMapping(path = "/reverse/payment", method = RequestMethod.POST)
    public Result reverseReturnPayment(@RequestBody List<ReversePaymentReturnRequest> request) {
        return this.returnService.reversePayment(request);
    }

    @RequestMapping(path = "/getPropLogReverseDocInvoice", method = RequestMethod.POST)
    public ResponseEntity<Result<List<ProposalReturnLog>>> getPropLogReverseDocInvoice(@RequestBody ReturnSearchProposalLog request) throws Exception {
        return this.proposalReturnLogService.getPropLogReverseDocInvoice(request);
    }

    @RequestMapping(path = "/reverse/invoice", method = RequestMethod.POST)
    public Result reverseReturnInvoice(@RequestBody List<ReverseInvoiceReturnRequest> request) {
        return this.returnService.reverseInvoice(request);
    }
}

package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.dto.SummaryFromTR1;
import th.com.bloomcode.paymentservice.model.request.SummaryTR1Request;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogTR1Service;

import java.util.List;

@RestController
@RequestMapping(path = "/api/proposalLogTR1")
public class PaymentProposalLogTR1Controller {

    @Autowired
    private ProposalLogTR1Service proposalLogTR1Service;


    @RequestMapping(path = "/getSummaryTR1", method = RequestMethod.POST)
    public ResponseEntity<Result<List<SummaryFromTR1>>> findErrorReport(@RequestBody SummaryTR1Request request)  {
        return proposalLogTR1Service.getSummaryReportFromTR1(request);
    }
}

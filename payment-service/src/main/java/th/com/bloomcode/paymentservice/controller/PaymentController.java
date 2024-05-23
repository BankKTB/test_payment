package th.com.bloomcode.paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.ReversePaymentReport;
import th.com.bloomcode.paymentservice.model.request.*;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.service.payment.impl.GLLineServiceImpl;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.APUpdateLineVendorRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PrepareProposalDocumentService prepareProposalDocumentService;
    private final PaymentReverseService paymentReverseService;
    private final PaymentReportService paymentReportService;
    private final GLLineService glLineService;
    private final ReturnReverseDocumentService returnReverseDocumentService;
    private final PaymentAsyncService paymentAsyncService;
    private final ProposalLogService proposalLogService;


//    @RequestMapping(path = "/proposal/run/{id}", method = RequestMethod.POST)
//    public void proposal(@RequestBody WSWebInfo webInfo, HttpServletRequest httpServletRequest,
//                                                         @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id
//    ) {
//         paymentAsyncService.createProposalNew(id, webInfo);
//    }
////
//    @RequestMapping(path = "/real/run/{id}", method = RequestMethod.POST)
//    public void realRun(@RequestBody WSWebInfo webInfo,
//                                                        HttpServletRequest httpServletRequest,
//                                                        @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) {
//
//         paymentAsyncService.createRealRunJobNew(id, webInfo);
//    }

    @RequestMapping(path = "/repair/document/{paymentProcessId}", method = RequestMethod.POST)
    public void repairDocument(@RequestBody WSWebInfo webInfo,
                               HttpServletRequest httpServletRequest,
                               @PathVariable("paymentProcessId") @Min(value = 1, message = "Id must be greater than or equal to 1") Long paymentProcessId
    ) {
        prepareProposalDocumentService.repairDocument(httpServletRequest, paymentProcessId, webInfo);
    }


    //

    @RequestMapping(path = "/proposal/document/{id}", method = RequestMethod.POST)
    public ResponseEntity<Result<List<PaymentReport>>> findProposalDocumentForBlock(@RequestBody WSWebInfo webInfo,
                                                                                    HttpServletRequest httpServletRequest,
                                                                                    @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) {

        return prepareProposalDocumentService.findProposalDocumentForBlock(httpServletRequest, id, webInfo);
    }


    @RequestMapping(path = "/block/document", method = RequestMethod.POST)
    public void blockDocument(@RequestBody ProposalDocumentRequest request) {
        prepareProposalDocumentService.blockDocument(request);
    }


    @RequestMapping(path = "/mass/changeDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<List<MassChangeDocumentRequest>>> massChangeDocument(@RequestBody List<MassChangeDocumentRequest> request) {
        return this.paymentReverseService.massChangeDocument(request);
    }
    @RequestMapping(path = "/mass/pullMassChangeDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<List<MassChangeDocument>>> pullMassChangeDocument(@RequestBody List<MassChangeDocumentRequest> request) {
        return this.paymentReverseService.pullMassChangeDocument(request);
    }
    @RequestMapping(path = "/mass/pullMassChangeDocument/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<MassChangeDocument>>> pullMassChangeDocument( @PathVariable("uuid")  String uuid) {
        return this.paymentReverseService.pullMassChangeDocument(uuid);
    }


    @RequestMapping(path = "/mass/reverseDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<List<ReverseDocumentRequest>>> reversePaymentDocument(@RequestBody List<ReverseDocumentRequest> request) {
        return this.paymentReverseService.massReverseDocument(request);
    }

    @RequestMapping(path = "/mass/pullMassReverseDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<List<ReverseDocument>>> pullMassReverseDocument(@RequestBody List<ReverseDocumentRequest> request) {
        return this.paymentReverseService.pullMassReverseDocument(request);
    }
    @RequestMapping(path = "/mass/pullMassReverseDocument/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<ReverseDocument>>> pullMassReverseDocument( @PathVariable("uuid")  String uuid) {
        return this.paymentReverseService.pullMassReverseDocument(uuid);
    }

    @RequestMapping(path = "/reverse/paymentDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<ReverseDocumentRequest>> reversePaymentDocument(@RequestBody ReverseDocumentRequest request) {
        return this.paymentReverseService.reversePaymentDocument(request);
    }

    @RequestMapping(path = "/reverse/invoiceDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<ReverseDocumentRequest>> reverseInvoiceDocument(@RequestBody ReverseDocumentRequest request) {
        return this.paymentReverseService.reverseInvoiceDocument(request);
    }

    @RequestMapping(path = "/reversePaymentAll", method = RequestMethod.POST)
    public ResponseEntity<Result<List<PaymentReport>>> reversePaymentAll(HttpServletRequest httpServletRequest,
                                                                         @RequestBody ReversePaymentDocumentAllRequest reversePaymentDocumentAllRequest
    ) {
        return this.paymentReverseService.reversePaymentAll(httpServletRequest, reversePaymentDocumentAllRequest);
    }

    @RequestMapping(path = "/pullReversePaymentDocument/{paymentAliasId}", method = RequestMethod.POST)
    public ResponseEntity<Result<ReversePaymentReport>> pullReversePaymentDocument(HttpServletRequest httpServletRequest,
                                                                                   @PathVariable("paymentAliasId") @Min(value = 1, message = "Id must be greater than or equal to 1") Long paymentAliasId,
                                                                                   @RequestBody WSWebInfo webInfo
    ) {
        return this.paymentReverseService.pullReversePaymentDocument(httpServletRequest, paymentAliasId, webInfo);
    }

    @RequestMapping(path = "/changeDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> changeDocument(HttpServletRequest httpServletRequest,
                               @RequestBody ChangeDocumentRequest changeDocumentRequest
    ) {
      return    this.glLineService.changeDocument(httpServletRequest, changeDocumentRequest);
    }

    @RequestMapping(path = "/mass/pullMassStep4ReverseDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<List<ReturnReverseDocument>>> pullMassStep3ReverseDocument(@RequestBody List<ReverseDocumentRequest> request) {
        return this.returnReverseDocumentService.pullMassStep4ReverseDocument(request);
    }

    @RequestMapping(path = "validateMarkComplete", method = RequestMethod.POST)
    public ResponseEntity<Result<ProposalLog>> validateMarkComplete(@RequestBody DocumentRequest request) {
        return this.proposalLogService.findByPaymentDocumentAndCompCodeAndPaymentFiscalYear(request.getPaymentDocNo(), request.getCompCode(), request.getFiscalYear());
    }
}

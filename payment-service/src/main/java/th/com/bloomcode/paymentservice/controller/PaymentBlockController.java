package th.com.bloomcode.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.SearchPaymentBlockRequest;
import th.com.bloomcode.paymentservice.model.payment.OmDisplayColumnTable;
import th.com.bloomcode.paymentservice.model.payment.OmSearchCriteria;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.payment.UnblockDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.*;
import th.com.bloomcode.paymentservice.model.response.OmReportResponse;
import th.com.bloomcode.paymentservice.model.response.UnBlockDocumentMQResponse;
import th.com.bloomcode.paymentservice.model.response.UnBlockDocumentResponse;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.service.payment.impl.GLLineServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/paymentBlock")
public class PaymentBlockController {

    private final GLLineServiceImpl glLineService;
    private final UnBlockDocumentService unBlockDocumentService;
    private final UnBlockDocumentLogService unBlockDocumentLogService;
    private final UnblockDocumentDetailLogService unblockDocumentDetailLogService;
    private final UnBlockDocumentMQService unBlockDocumentMQService;
    private final OmDisplayColumnTableService omDisplayColumnTableService;
    private final OmSearchCriteriaService omSearchCriteriaService;
    public PaymentBlockController(GLLineServiceImpl glLineService, UnBlockDocumentService unBlockDocumentService, UnBlockDocumentLogService unBlockDocumentLogService, UnblockDocumentDetailLogService unblockDocumentDetailLogService, UnBlockDocumentMQService unBlockDocumentMQService, OmDisplayColumnTableService omDisplayColumnTableService, OmSearchCriteriaService omSearchCriteriaService) {
        this.glLineService = glLineService;
        this.unBlockDocumentService = unBlockDocumentService;
        this.unBlockDocumentLogService = unBlockDocumentLogService;
        this.unblockDocumentDetailLogService = unblockDocumentDetailLogService;
        this.unBlockDocumentMQService = unBlockDocumentMQService;
        this.omDisplayColumnTableService = omDisplayColumnTableService;
        this.omSearchCriteriaService = omSearchCriteriaService;
    }

    @RequestMapping(path = "changePaymentBlock", method = RequestMethod.POST)
    public ResponseEntity<Result<List<UnBlockChangeDocumentRequest>>> changePaymentBlock(@RequestBody List<UnBlockChangeDocumentRequest> request) {
        return this.unBlockDocumentService.changePaymentBlock(request);
    }
    @RequestMapping(path = "pullChangePaymentBlock", method = RequestMethod.POST)
    public ResponseEntity<Result<List<UnBlockDocumentMQResponse>>> pullChangePaymentBlock(@RequestBody List<UnBlockChangeDocumentRequest> request) {
        return this.unBlockDocumentMQService.findMQLogByCondition(request);
    }

    @RequestMapping(path = "pullChangePaymentBlock/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<UnBlockDocumentMQResponse>>> pullChangePaymentBlockUUID(@PathVariable("uuid") String uuid) {
        return this.unBlockDocumentMQService.findMQLogByCondition(uuid);
    }

    @RequestMapping(path = "search", method = RequestMethod.POST)
    public ResponseEntity<Result<UnBlockDocumentResponse>> findUnBlockByCondition(HttpServletRequest httpServletRequest, @RequestBody SearchPaymentBlockRequest request) {
        return this.unBlockDocumentService.findUnBlockByCondition(httpServletRequest, request);
    }

    @RequestMapping(path = "findParent", method = RequestMethod.POST)
    public ResponseEntity<Result<List<UnBlockDocument>>> findUnBlockParentByCondition(@RequestBody List<PaymentBlockListDocumentRequest> request) {
        return this.unBlockDocumentService.findUnBlockParentByCondition(request);
    }

    @RequestMapping(path = "log", method = RequestMethod.POST)
    public ResponseEntity<Result<List<UnBlockDocumentLog>>> findByCondition(@RequestBody SearchUnBlockDocumentLogRequest searchUnBlockDocumentLogRequest) {
        return this.unBlockDocumentLogService.findLogByCondition(searchUnBlockDocumentLogRequest);
    }

    @RequestMapping(path = "report", method = RequestMethod.POST)
    public ResponseEntity<Result<OmReportResponse>> findReportUnblockByCondition(@RequestBody SearchUnBlockDocumentLogRequest request) {
        return this.unBlockDocumentLogService.findReportUnblockByCondition(request);
    }

    @RequestMapping(path = "save/search/criteria", method = RequestMethod.POST)
    public ResponseEntity<Result<OmSearchCriteria>> save(@Valid @RequestBody OmSearchCriteriaRequest request) {
        return omSearchCriteriaService.save(request);
    }

    @RequestMapping(path = "update/search/criteria", method = RequestMethod.PUT)
    public ResponseEntity<Result<OmSearchCriteria>> update(@Valid @RequestBody OmSearchCriteriaRequest request) {
        return omSearchCriteriaService.update(request);
    }

    @RequestMapping(path = "search/criteria/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<OmSearchCriteria>> criteria(@PathVariable("id") Long id
    ) {
        return omSearchCriteriaService.getOneById(id);
    }

    @RequestMapping(path = "search/criteriaAll/{role}/{user}/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<OmSearchCriteria>>> criteriaAll(@PathVariable("role") String role, @PathVariable("user") String user, @PathVariable("value") String value
    ) {
        return omSearchCriteriaService.getAllByRole(role, user, value);
    }

    @RequestMapping(path = "delete/criteria/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<OmSearchCriteria>> deleteColumnById(@PathVariable("id") Long id
    ) {
        return omSearchCriteriaService.deleteById(id);
    }


    @RequestMapping(path = "save/column", method = RequestMethod.POST)
    public ResponseEntity<Result<OmDisplayColumnTable>> save(@Valid @RequestBody OmDisplayColumnTableRequest request) {
        return omDisplayColumnTableService.save(request);
    }

    @RequestMapping(path = "update/column", method = RequestMethod.PUT)
    public ResponseEntity<Result<OmDisplayColumnTable>> update(@Valid @RequestBody OmDisplayColumnTableRequest request) {
        return omDisplayColumnTableService.update(request);
    }

    @RequestMapping(path = "delete/column/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<OmDisplayColumnTable>> deleteById(@PathVariable("id") Long id
    ) {
        return omDisplayColumnTableService.deleteById(id);
    }

    @RequestMapping(path = "search/columnAll/{role}/{user}/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<OmDisplayColumnTable>>> columnAll(@PathVariable("role") String role, @PathVariable("user") String user, @PathVariable("value") String value
    ) {
        return omDisplayColumnTableService.getAllByRole(role, user, value);
    }

    @RequestMapping(path = "search/column/{role}/{name}", method = RequestMethod.GET)
    public ResponseEntity<Result<OmDisplayColumnTable>> getOneByNameAndRole(@PathVariable("role") String role,
                                                                            @PathVariable("name") String name
    ) {
        return omDisplayColumnTableService.getOneByRoleAndName(role, name);
    }




    @RequestMapping(path = "findLogDetail", method = RequestMethod.POST)
    public ResponseEntity<Result<List<UnblockDocumentDetailLog>>>  findLogDetail(@RequestBody UnblockDocumentLogDetailRequest request) {
        return unblockDocumentDetailLogService.findLogDetail(request);
    }


}

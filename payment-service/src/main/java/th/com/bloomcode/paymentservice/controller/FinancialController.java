package th.com.bloomcode.paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.webservice.client.FinancialXMLService;
import th.com.bloomcode.paymentservice.webservice.model.request.*;
import th.com.bloomcode.paymentservice.webservice.model.response.*;

@RestController
@RequestMapping(path = "/api/financial")
public class FinancialController {

    private final FinancialXMLService financialXMLService;

    @Autowired
    public FinancialController(FinancialXMLService financialXMLService) {
        this.financialXMLService = financialXMLService;
    }

    @RequestMapping(path = "searchDetail", method = RequestMethod.POST)
    public ResponseEntity<Result<FISearchDetailResponse>> searchDetail(@RequestBody FISearchDetailRequest request) {
        return this.financialXMLService.fiSearchDetail(request);
    }
    @RequestMapping(path = "searchAutoDoc", method = RequestMethod.POST)
    public ResponseEntity<Result<FIResponse>> searchAutoDoc(@RequestBody FIResultRequest request) throws JsonProcessingException {
        return this.financialXMLService.searchAutoDoc(request);
    }
    @RequestMapping(path = "search", method = RequestMethod.POST)
    public ResponseEntity<Result<FISearchResponse2>> searchDetail(@RequestBody FISearchRequest2 request) {
        return this.financialXMLService.search(request);
    }


    @RequestMapping(path = "paymentBlockDetail", method = RequestMethod.POST)
    public ResponseEntity<Result<FICPbkDetailResponse>> paymentBlockDetail(@RequestBody FICPbkDetailRequest request) throws JsonProcessingException {
        return this.financialXMLService.paymentBlockDetail(request);
    }

    @RequestMapping(path = "updateLineVendor", method = RequestMethod.POST)
    public ResponseEntity<Result<APUpdateLineVendorResponse>> updateLineVendor(@RequestBody APUpdateLineVendorRequest request) throws JsonProcessingException {
        return this.financialXMLService.updateLineVendor(request);
    }

    @RequestMapping(path = "validateUpdateLineVendor", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> validateUpdateLineVendor(@RequestBody APUpdateLineVendorRequest request) throws JsonProcessingException {
        return this.financialXMLService.validateUpdateLineVendor(request);
    }
    @RequestMapping(path = "checkOriginalDocument", method = RequestMethod.POST)
    public ResponseEntity<Result<FICheckOriginalDocResponse>> checkOriginalDocument(@RequestBody FICheckOriginalDocRequest request) throws JsonProcessingException {
        return this.financialXMLService.checkOriginalDocument(request);
    }
}

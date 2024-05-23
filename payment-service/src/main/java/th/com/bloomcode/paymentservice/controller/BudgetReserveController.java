package th.com.bloomcode.paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.request.BRConsumptionRequest;
import th.com.bloomcode.paymentservice.model.request.BRSearchDetailRequest;

import th.com.bloomcode.paymentservice.model.response.BRConsumptionResponse;
import th.com.bloomcode.paymentservice.model.response.BRSearchDetailResponse;
import th.com.bloomcode.paymentservice.webservice.client.BudgetReserveXMLService;


@RestController
@RequestMapping(path = "/api/budgetReserve")
public class BudgetReserveController {
    private final BudgetReserveXMLService budgetReserveXMLService;

    @Autowired
    public BudgetReserveController(BudgetReserveXMLService budgetReserveXMLService) {
        this.budgetReserveXMLService = budgetReserveXMLService;
    }


    @RequestMapping(path = "searchDetail", method = RequestMethod.POST)
    public ResponseEntity<Result<BRSearchDetailResponse>> searchDetail(
            @RequestBody BRSearchDetailRequest request)
            throws JsonProcessingException {
        return this.budgetReserveXMLService.searchDetail(request);
    }

    @RequestMapping(path = "consumption", method = RequestMethod.POST)
    public ResponseEntity<Result<BRConsumptionResponse>> consumption(
            @RequestBody BRConsumptionRequest request)
            throws JsonProcessingException {
        return this.budgetReserveXMLService.consumption(request);
    }




}

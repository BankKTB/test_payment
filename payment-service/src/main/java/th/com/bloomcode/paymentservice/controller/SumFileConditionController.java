package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.entity.SumFileCondition;
import th.com.bloomcode.paymentservice.service.SumFileConditionService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/sumFileCondition")
@Validated
public class SumFileConditionController {

    @Autowired
    private SumFileConditionService sumFileConditionService;

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<SumFileCondition>> saveSumFileCondition(@RequestBody SumFileCondition request) throws Exception {
        return this.sumFileConditionService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<SumFileCondition>> updateSumFileCondition(@RequestBody SumFileCondition request,
                                                                   @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.sumFileConditionService.update(request);
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<SumFileCondition>>> getAllSumFileCondition() throws Exception {
        return this.sumFileConditionService.findAllConfig();
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<SumFileCondition>> deleteSumFileCondition(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.sumFileConditionService.delete(id);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<SumFileCondition>> findById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.sumFileConditionService.findById(id);
    }

}

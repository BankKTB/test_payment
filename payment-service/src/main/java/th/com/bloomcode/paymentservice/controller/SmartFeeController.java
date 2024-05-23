package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.entity.SmartFee;
import th.com.bloomcode.paymentservice.service.SmartFeeService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/smartFee")
@Validated
public class SmartFeeController {

    @Autowired
    private SmartFeeService smartFeeService;

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<SmartFee>> saveSmartFee(@RequestBody @Valid SmartFee request) throws Exception {
        return this.smartFeeService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<SmartFee>> updateSmartFee(@RequestBody @Valid SmartFee request,
                                                                   @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.smartFeeService.update(request);
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<SmartFee>>> getAllSmartFee() throws Exception {
        return this.smartFeeService.findAllConfig();
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<SmartFee>> deleteSmartFee(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.smartFeeService.delete(id);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<SmartFee>> findById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.smartFeeService.findById(id);
    }

}

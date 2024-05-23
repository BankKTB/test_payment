package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFee;
import th.com.bloomcode.paymentservice.service.SwiftFeeService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/swiftFee")
@Validated
public class SwiftFeeController {

    @Autowired
    private SwiftFeeService swiftFeeService;

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<SwiftFee>> saveSwiftFee(@RequestBody SwiftFee request) throws Exception {
        return this.swiftFeeService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<SwiftFee>> updateSwiftFee(@RequestBody SwiftFee request,
                                                                   @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.swiftFeeService.update(request);
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<SwiftFee>>> getAllSwiftFee() throws Exception {
        return this.swiftFeeService.findAllConfig();
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<SwiftFee>> deleteSwiftFee(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.swiftFeeService.delete(id);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<SwiftFee>> findById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.swiftFeeService.findById(id);
    }

}

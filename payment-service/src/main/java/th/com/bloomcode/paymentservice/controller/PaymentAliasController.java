package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.config.Parameter;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.util.TimestampUtil;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/api/paymentAlias")
@Validated
public class PaymentAliasController {

    @Autowired
    private PaymentAliasService paymentAliasService;

    @RequestMapping(path = "/search/{paymentDate}/{paymentName}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentAlias>> search(
            @PathVariable("paymentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull(message = "Payment date is required") String paymentDate,
            @PathVariable("paymentName") @NotBlank(message = "Payment name is required") String paymentName) {
        return paymentAliasService.searchByPaymentDateAndPaymentName(TimestampUtil.stringToTimestamp(paymentDate), paymentName);
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<PaymentAlias>> save(@Valid @RequestBody PaymentAlias request) {
        return paymentAliasService.create(request);

    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<PaymentAlias>> save(@Valid @RequestBody PaymentAlias request,
                                                     @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) {
        return paymentAliasService.update(request);

    }

    @RequestMapping(path = "/deleteParameter/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<PaymentAlias>> deleteParameter(@Valid @RequestBody PaymentAlias request,
                                                     @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) {
        return paymentAliasService.updateParameter(request);

    }

    @RequestMapping(path = "/findByCondition/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentAliasResponse>>> findByCondition(@PathVariable("value") String value) throws Exception {
        return paymentAliasService.findByCondition(value);
    }
    @RequestMapping(path = "/findJobByCondition/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentAliasResponse>>> findJobByCondition(@PathVariable("value") String value) throws Exception {
        return paymentAliasService.findSearchJobByCondition(value);
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<PaymentAlias>> delete(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) {
        return paymentAliasService.deleteById(id);

    }

    @RequestMapping(path = "/validate", method = RequestMethod.POST)
    public ResponseEntity<Result<List<String>>> validate(@RequestBody Parameter request) {
        return paymentAliasService.validateParameter(request);

    }

//    @RequestMapping(path = "/deletePaymentDocumentAll/{id}", method = RequestMethod.DELETE)
//    public void deletePaymentDocumentAll(
//            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) {
//        paymentAliasService.deletePaymentDocumentAll(id);
//    }


    @RequestMapping(path = "/update/schedule/{id}/{type}", method = RequestMethod.PUT)
    public ResponseEntity<Result<PaymentAlias>> updateSchedule(@Valid @RequestBody PaymentAlias request,
                                                               @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id
            , @PathVariable("type") String type) {
        return paymentAliasService.updateSchedule(request, type);

    }


}

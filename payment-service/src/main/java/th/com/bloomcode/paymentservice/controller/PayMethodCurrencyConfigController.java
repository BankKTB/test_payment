package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.entity.PayMethodCurrencyConfig;
import th.com.bloomcode.paymentservice.service.PayMethodCurrencyConfigService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/payMethodCurrencyConfig")
public class PayMethodCurrencyConfigController {

//    @Autowired
//    private PayMethodCurrencyConfigService payMethodCurrencyConfigService;
//
//    @RequestMapping(path = "/save", method = RequestMethod.POST)
//    public ResponseEntity<Result<PayMethodCurrencyConfig>> save(@RequestBody PayMethodCurrencyConfig request)
//            throws Exception {
//        return this.payMethodCurrencyConfigService.save(request);
//    }
//
//    @RequestMapping(path = "/getAllByPayMethodConfigId/{payMethodConfigId}", method = RequestMethod.GET)
//    public ResponseEntity<Result<List<PayMethodCurrencyConfig>>> getAllCompanyPayingBankConfig(
//            @PathVariable("payMethodConfigId") @Min(value = 1, message = "Id must be greater than or equal to 1") Long payMethodConfigId)
//            throws Exception {
//        return this.payMethodCurrencyConfigService.findAllByPayMethodConfigId(payMethodConfigId);
//    }
//
//    @RequestMapping(path = "/deleteById/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Result<PayMethodCurrencyConfig>> deleteById(
//            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
//            throws Exception {
//        return this.payMethodCurrencyConfigService.deleteById(id);
//    }

}

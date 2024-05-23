//package th.com.bloomcode.paymentservice.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
//import th.com.bloomcode.paymentservice.model.request.PayMethodConfigRequest;
//import th.com.bloomcode.paymentservice.service.payment.PayMethodConfigService;
//
//import javax.validation.constraints.Min;
//import java.util.List;
//
//@RestController
//@RequestMapping(path = "/api/payMethodConfig")
//public class PayMethodConfigController {
//
//    @Autowired
//    private PayMethodConfigService payMethodConfigService;
//
//    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
//    public ResponseEntity<Result<List<PayMethodConfig>>> getAllPayMethod() throws Exception {
//        return this.payMethodConfigService.findAll();
//    }
//
//    @RequestMapping(path = "/save", method = RequestMethod.POST)
//    public ResponseEntity<Result<PayMethodConfig>> save(@RequestBody PayMethodConfig request) throws Exception {
//        return this.payMethodConfigService.save(request);
//    }
//
//    @RequestMapping(path = "/copy", method = RequestMethod.POST)
//    public ResponseEntity<Result<PayMethodConfig>> copy(@RequestBody PayMethodConfigRequest request) throws Exception {
//        return this.payMethodConfigService.copy(request);
//    }
//
//    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Result<PayMethodConfig>> updateCompanyPayee(@RequestBody PayMethodConfig request,
//                                                                      @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
//            throws Exception {
//        return this.payMethodConfigService.update(request);
//    }
//
//    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
//    public ResponseEntity<Result<PayMethodConfig>> findById(@PathVariable("id") Long id) throws Exception {
//        return this.payMethodConfigService.findById(id);
//
//    }
//
//
//    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Result<PayMethodConfig>> deleteCompanyPayee(
//            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
//            throws Exception {
//        return this.payMethodConfigService.delete(id);
//    }
//
//
//}

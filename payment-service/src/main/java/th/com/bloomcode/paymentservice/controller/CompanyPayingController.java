package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPaying;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayingService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/companyPaying")
@Validated
public class CompanyPayingController {

    @Autowired
    private CompanyPayingService companyPayingService;

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<CompanyPaying>> saveCompanyPaying(@RequestBody CompanyPaying request)
            throws Exception {
        return this.companyPayingService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<CompanyPaying>> updateCompanyPaying(@RequestBody CompanyPaying request,
                                                                     @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayingService.update(request);
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CompanyPaying>>> getAllCompanyPaying() throws Exception {
        return this.companyPayingService.findAll();
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<CompanyPaying>> deleteCompanyPaying(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayingService.delete(id);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<CompanyPaying>> findById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayingService.findById(id);
    }

}

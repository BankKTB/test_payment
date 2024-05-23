package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayee;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayeeService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/companyPayee")
@Validated
public class CompanyPayeeController {

    @Autowired
    private CompanyPayeeService companyPayeeService;

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<CompanyPayee>> saveCompanyPayee(@RequestBody CompanyPayee request) throws Exception {
        return this.companyPayeeService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<CompanyPayee>> updateCompanyPayee(@RequestBody CompanyPayee request,
                                                                   @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayeeService.update(request);
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CompanyPayee>>> getAllCompanyPayee() throws Exception {
        return this.companyPayeeService.findAll();
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<CompanyPayee>> deleteCompanyPayee(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayeeService.delete(id);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<CompanyPayee>> findById(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayeeService.findById(id);
    }

}

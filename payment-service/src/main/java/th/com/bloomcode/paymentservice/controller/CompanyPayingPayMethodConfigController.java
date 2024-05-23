package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingPayMethodConfig;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayingPayMethodConfigService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/companyPayingPayMethodConfig")
public class CompanyPayingPayMethodConfigController {

    @Autowired
    private CompanyPayingPayMethodConfigService companyPayingPayMethodConfigService;

    @RequestMapping(path = "/getAll/{companyPayingId}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CompanyPayingPayMethodConfig>>> getAllPayMethod(
            @PathVariable("companyPayingId") @Min(value = 1, message = "Id must be greater than or equal to 1") Long companyPayingId)
            throws Exception {
        return this.companyPayingPayMethodConfigService.findAllByCompanyPayingId(companyPayingId);
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> save(@RequestBody CompanyPayingPayMethodConfig request)
            throws Exception {
        return this.companyPayingPayMethodConfigService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> updateCompanyPayee(
            @RequestBody CompanyPayingPayMethodConfig request,
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayingPayMethodConfigService.update(request);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> findById(@PathVariable("id") Long id) throws Exception {
        return this.companyPayingPayMethodConfigService.findById(id);

    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> deleteCompanyPayee(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayingPayMethodConfigService.delete(id);
    }

}

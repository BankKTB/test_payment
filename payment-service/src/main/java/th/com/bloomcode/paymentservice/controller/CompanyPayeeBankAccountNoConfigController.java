package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeBankAccountNoConfig;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayeeBankAccountNoConfigService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/companyPayeeBankAccountNoConfig")
public class CompanyPayeeBankAccountNoConfigController {

    private final CompanyPayeeBankAccountNoConfigService companyPayeeBankAccountNoConfigService;

    @Autowired
    public CompanyPayeeBankAccountNoConfigController(CompanyPayeeBankAccountNoConfigService companyPayeeBankAccountNoConfigService) {
        this.companyPayeeBankAccountNoConfigService = companyPayeeBankAccountNoConfigService;
    }

    @RequestMapping(path = "/getAll/{houseBankKeyId}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CompanyPayeeBankAccountNoConfig>>> findAllByHouseBankKeyId(
            @PathVariable("houseBankKeyId") Long houseBankKeyId) throws Exception {
        return this.companyPayeeBankAccountNoConfigService.findAllByHouseBankKeyId(houseBankKeyId.toString());
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> save(
            @RequestBody CompanyPayeeBankAccountNoConfig request) throws Exception {
        return this.companyPayeeBankAccountNoConfigService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> updateCompanyPayee(
            @RequestBody CompanyPayeeBankAccountNoConfig request,
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayeeBankAccountNoConfigService.update(request);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> findById(@PathVariable("id") Long id)
            throws Exception {
        return this.companyPayeeBankAccountNoConfigService.findById(id);

    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> deleteCompanyPayee(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayeeBankAccountNoConfigService.delete(id);
    }

}

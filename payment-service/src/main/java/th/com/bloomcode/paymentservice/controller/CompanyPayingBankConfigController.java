package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingBankConfig;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayingBankConfigService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/companyPayingBankConfig")
@Validated
public class CompanyPayingBankConfigController {

    @Autowired
    private CompanyPayingBankConfigService companyPayingBankConfigService;

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<CompanyPayingBankConfig>> saveCompanyPayingBankConfig(
            @RequestBody CompanyPayingBankConfig request) throws Exception {
        return this.companyPayingBankConfigService.save(request);
    }

    @RequestMapping(path = "/getAllByCompanyPayingId/{companyPayingId}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CompanyPayingBankConfig>>> getAllCompanyPayingBankConfig(
            @PathVariable("companyPayingId") @Min(value = 1, message = "Id must be greater than or equal to 1") Long companyPayingId)
            throws Exception {
        return this.companyPayingBankConfigService.findAllByCompanyPayingId(companyPayingId);
    }

    @RequestMapping(path = "/deleteById/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<CompanyPayingBankConfig>> deleteCompanyPayingBankConfig(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayingBankConfigService.deleteById(id);
    }

}

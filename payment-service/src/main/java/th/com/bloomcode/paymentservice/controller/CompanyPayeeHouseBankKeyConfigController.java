package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeHouseBankKeyConfig;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayeeBankAccountNoConfigService;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayeeHouseBankKeyConfigService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/companyPayeeHouseBankKeyConfig")
public class CompanyPayeeHouseBankKeyConfigController {

    private final CompanyPayeeHouseBankKeyConfigService companyPayeeHouseBankKeyConfigService;

    private final CompanyPayeeBankAccountNoConfigService companyPayeeBankAccountNoConfigService;

    @Autowired
    public CompanyPayeeHouseBankKeyConfigController(CompanyPayeeHouseBankKeyConfigService companyPayeeHouseBankKeyConfigService, CompanyPayeeBankAccountNoConfigService companyPayeeBankAccountNoConfigService) {
        this.companyPayeeHouseBankKeyConfigService = companyPayeeHouseBankKeyConfigService;
        this.companyPayeeBankAccountNoConfigService = companyPayeeBankAccountNoConfigService;
    }


    @RequestMapping(path = "/getAll/{companyPayeeId}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CompanyPayeeHouseBankKeyConfig>>> findAllByCompanyPayeeId(
            @PathVariable("companyPayeeId") Long companyPayeeId) throws Exception {
        return this.companyPayeeHouseBankKeyConfigService.findAllByCompanyPayeeId(companyPayeeId);
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> save(
            @RequestBody CompanyPayeeHouseBankKeyConfig request) throws Exception {
        return this.companyPayeeHouseBankKeyConfigService.save(request);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> updateCompanyPayee(
            @RequestBody CompanyPayeeHouseBankKeyConfig request,
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        return this.companyPayeeHouseBankKeyConfigService.update(request);
    }

    @RequestMapping(path = "/findBy/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> findById(@PathVariable("id") Long id) throws Exception {


        return this.companyPayeeHouseBankKeyConfigService.findById(id);

    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> deleteCompanyPayee(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id)
            throws Exception {
        this.companyPayeeBankAccountNoConfigService.deleteAllByHouseBankKeyId(id);
        return this.companyPayeeHouseBankKeyConfigService.delete(id);
    }

}

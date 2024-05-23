package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.entity.PaymentIndependentConfig;
import th.com.bloomcode.paymentservice.service.PaymentIndependentConfigService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(path = "/api/paymentIndependentConfig")
public class PaymentIndependentConfigController {

    @Autowired
    private PaymentIndependentConfigService paymentIndependentConfigService;

    @RequestMapping(path = "/searchStandard", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentIndependentConfig>>> searchStandard() {
        return this.paymentIndependentConfigService.findOnlyStandard();
    }

    @RequestMapping(path = "/searchGroup/{groupName}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentIndependentConfig>>> searchGroup(@PathVariable("groupName") @NotEmpty(message = "Group name is required") String groupName) {
        return this.paymentIndependentConfigService.findByGroupName(groupName);
    }
}

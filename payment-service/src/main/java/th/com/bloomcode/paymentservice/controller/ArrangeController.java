package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentArrange;
import th.com.bloomcode.paymentservice.service.payment.PaymentArrangeService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/arrange")
public class ArrangeController {
    private final PaymentArrangeService paymentArrangeService;

    @Autowired
    public ArrangeController(PaymentArrangeService paymentArrangeService) {
        this.paymentArrangeService = paymentArrangeService;
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<PaymentArrange>> save(HttpServletRequest httpServletRequest, @Valid @RequestBody PaymentArrange paymentArrange)
            throws Exception {
        return paymentArrangeService.save(httpServletRequest, paymentArrange);
    }

    @RequestMapping(path = "/findByArrangeCode/{arrangeCode}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentArrange>>> findAllByArrangeCode(HttpServletRequest httpServletRequest, @PathVariable("arrangeCode") String arrangeCode)
            throws Exception {
        return paymentArrangeService.findAllByArrangeCode(httpServletRequest, arrangeCode);
    }

    @RequestMapping(path = "/findByArrangeCodeDefaultArrange/{arrangeCode}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentArrange>>> findByArrangeCodeDefaultArrange(HttpServletRequest httpServletRequest, @PathVariable("arrangeCode") String arrangeCode)
            throws Exception {
        return paymentArrangeService.findByArrangeCodeDefaultArrange(httpServletRequest, arrangeCode);
    }

    @RequestMapping(path = "/findByArrangeCodeAndArrangeName/{arrangeCode}/{arrangeName}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentArrange>> findOneByArrangeCodeAndArrangeName(HttpServletRequest httpServletRequest, @PathVariable("arrangeCode") String arrangeCode, @PathVariable("arrangeName") String arrangeName)
            throws Exception {
        return paymentArrangeService.findOneByArrangeCodeAndArrangeName(httpServletRequest, arrangeCode, arrangeName);
    }

    @RequestMapping(path = "/findByArrange/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentArrange>> findOneById(HttpServletRequest httpServletRequest, @PathVariable("id") Long id)
            throws Exception {
        return paymentArrangeService.findOneById(httpServletRequest, id);
    }

    @RequestMapping(path = "/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<PaymentArrange>> edit(HttpServletRequest httpServletRequest, @RequestBody PaymentArrange paymentArrange, @PathVariable("id") Long id)
            throws Exception {
        return paymentArrangeService.edit(httpServletRequest, paymentArrange, id);
    }

    @RequestMapping(path = "/editDefaultArrange/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentArrange>> editDefaultArrange(HttpServletRequest httpServletRequest, @PathVariable("id") Long id)
            throws Exception {
        return paymentArrangeService.editDefaultArrange(httpServletRequest, id);
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Result> delete(HttpServletRequest httpServletRequest, @PathVariable("id") Long id)
            throws Exception {
        return paymentArrangeService.delete(httpServletRequest, id);
    }

}

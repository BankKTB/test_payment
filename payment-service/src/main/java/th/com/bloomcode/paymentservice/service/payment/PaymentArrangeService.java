package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentArrange;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PaymentArrangeService {
    ResponseEntity<Result<PaymentArrange>> save(HttpServletRequest httpServletRequest, PaymentArrange request);
    ResponseEntity<Result<List<PaymentArrange>>> findAllByArrangeCode(HttpServletRequest httpServletRequest, String arrangeCode);
    ResponseEntity<Result<List<PaymentArrange>>> findByArrangeCodeDefaultArrange(HttpServletRequest httpServletRequest, String arrangeCode);
    ResponseEntity<Result<PaymentArrange>> findOneByArrangeCodeAndArrangeName(HttpServletRequest httpServletRequest, String arrangeCode, String arrangeName);
    ResponseEntity<Result<PaymentArrange>> findOneById(HttpServletRequest httpServletRequest, Long id);
    ResponseEntity<Result<PaymentArrange>> edit(HttpServletRequest httpServletRequest, PaymentArrange request, Long id);
    ResponseEntity<Result<PaymentArrange>> editDefaultArrange(HttpServletRequest httpServletRequest, Long id);
    ResponseEntity<Result> delete(HttpServletRequest httpServletRequest, Long id);
}


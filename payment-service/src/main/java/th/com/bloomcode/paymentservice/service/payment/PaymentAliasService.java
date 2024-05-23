package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.config.Parameter;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface PaymentAliasService {
    PaymentAlias save(PaymentAlias paymentAlias);

    ResponseEntity<Result<PaymentAlias>> create(PaymentAlias paymentAlias);

    ResponseEntity<Result<PaymentAlias>> update(PaymentAlias paymentAlias);

    ResponseEntity<Result<PaymentAlias>> updateParameter(PaymentAlias paymentAlias);

    ResponseEntity<Result<PaymentAlias>> searchByPaymentDateAndPaymentName(Timestamp paymentDate, String paymentName);

    ResponseEntity<Result<List<PaymentAliasResponse>>> findByCondition(String value);

    ResponseEntity<Result<PaymentAlias>> deleteById(Long id);

    ResponseEntity<Result<List<String>>> validateParameter(Parameter parameter);


    void saveAll(List<PaymentAlias> paymentAliases);

    List<PaymentAlias> findAll();

    PaymentAlias findOneById(Long id);

    ResponseEntity<Result<PaymentAlias>> updateSchedule(PaymentAlias request, String type);

    ResponseEntity<Result<List<PaymentAliasResponse>>> findCreateJobByCondition(String value);
    ResponseEntity<Result<PaymentAliasResponse>> findCreateJobByCondition(Date paymentDate, String paymentName);

    ResponseEntity<Result<List<PaymentAliasResponse>>> findSearchJobByCondition(String value);
    PaymentAlias findOneByPaymentDateAndPaymentName(Timestamp paymentDate, String paymentName);
}

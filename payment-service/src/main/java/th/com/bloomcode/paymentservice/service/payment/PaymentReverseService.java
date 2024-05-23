package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.config.Parameter;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.ReversePaymentReport;
import th.com.bloomcode.paymentservice.model.request.MassChangeDocumentRequest;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;
import th.com.bloomcode.paymentservice.model.request.ReversePaymentDocumentAllRequest;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.FIPostRequest;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface PaymentReverseService {


    ResponseEntity<Result<List<MassChangeDocumentRequest>>> massChangeDocument(List<MassChangeDocumentRequest> request);

    ResponseEntity<Result<List<ReverseDocumentRequest>>> massReverseDocument(List<ReverseDocumentRequest> request);

    ResponseEntity<Result<List<ReverseDocument>>> pullMassReverseDocument(List<ReverseDocumentRequest> request);

    ResponseEntity<Result<List<ReverseDocument>>> pullMassReverseDocument(String uuid);

    ResponseEntity<Result<List<MassChangeDocument>>> pullMassChangeDocument(List<MassChangeDocumentRequest> request);

    ResponseEntity<Result<List<MassChangeDocument>>> pullMassChangeDocument(String uuid);

    ResponseEntity<Result<ReverseDocumentRequest>> reversePaymentDocument(ReverseDocumentRequest request);

    ResponseEntity<Result<ReverseDocumentRequest>> reverseInvoiceDocument(ReverseDocumentRequest request);

    void sendReverse(FIMessage message, String compCode) throws Exception;

    ResponseEntity<Result<List<PaymentReport>>> reversePaymentAll(HttpServletRequest httpServletRequest, ReversePaymentDocumentAllRequest reversePaymentDocumentAllRequest);

    ResponseEntity<Result<ReversePaymentReport>> pullReversePaymentDocument(HttpServletRequest httpServletRequest, Long paymentAliasId, WSWebInfo webInfo);
}


package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentDetailLog;
import th.com.bloomcode.paymentservice.service.payment.PaymentRunErrorDocumentDetailLogService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/paymentRunError")
public class PaymentRunErrorDocumentLogController {

    @Autowired
    private PaymentRunErrorDocumentDetailLogService paymentRunErrorDocumentDetailLogService;


    @RequestMapping(path = "/findErrorDetailByInvoice/{invoiceCompanyCode}/{invoiceDocumentNo}/{invoiceFiscalYear}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentRunErrorDocumentDetailLog>>> findAll(@PathVariable("invoiceCompanyCode") String invoiceCompanyCode
            , @PathVariable("invoiceDocumentNo") String invoiceDocumentNo, @PathVariable("invoiceFiscalYear") String invoiceFiscalYear) throws Exception {

        return paymentRunErrorDocumentDetailLogService.findErrorDetailByInvoice(invoiceCompanyCode, invoiceDocumentNo, invoiceFiscalYear);

    }


    @RequestMapping(path = "/findErrorDetailByPaymentAliasId/{paymentAliasId}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentRunErrorDocumentDetailLog>>> findAll(@PathVariable("paymentAliasId") Long paymentAliasId
            ) throws Exception {

        return paymentRunErrorDocumentDetailLogService.findErrorDetailByPaymentAliasId(paymentAliasId);

    }

}

package th.com.bloomcode.paymentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.VendorReport;
import th.com.bloomcode.paymentservice.model.payment.dto.DuplicatePaymentReportResponse;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReportPaging;
import th.com.bloomcode.paymentservice.model.request.DuplicatePaymentReport;
import th.com.bloomcode.paymentservice.model.response.*;
import th.com.bloomcode.paymentservice.service.payment.PaymentReportService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/paymentReport")
public class PaymentReportController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaymentReportService paymentReportService;

    @RequestMapping(path = "/vendor/{id}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<VendorReport>>> findVendorReport(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
            @PathVariable("type") Long type) throws Exception {
        return paymentReportService.findVendorReport(id, type);
    }

    @RequestMapping(path = "/search/{paymentAliasId}/{vendor}/{bankAccount}/{type}/{page}/{size}", method = RequestMethod.GET)
    public ResponseEntity<Result<Page<PaymentReportPaging>>> search(
            @PathVariable("paymentAliasId") Long paymentAliasId,
            @PathVariable("type") String type,
            @PathVariable("vendor") String vendor,
            @PathVariable("bankAccount") String bankAccount,
            @PathVariable("page") int page,
            @PathVariable("size") int size) throws Exception {
        if (type.equalsIgnoreCase("0")) {
            return paymentReportService.findVendorReportDetail(paymentAliasId, true,vendor,bankAccount ,page, size);
        } else {
            return paymentReportService.findVendorReportDetail(paymentAliasId, false,vendor,bankAccount , page, size);
        }

    }

//    @RequestMapping(path = "/document/{id}/{vendor}/{type}", method = RequestMethod.GET)
//    public ResponseEntity<Result<PaymentVendorReportResponse>> findDocumentReport(
//            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
//            @PathVariable("vendor") String vendor,
//            @PathVariable("type") Long type) throws Exception {
//        return paymentReportService.findVendorReport(id, type);
//    }

    @RequestMapping(path = "/compCode/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<CompanyReportResponse>> findCompCode(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) throws Exception {
        return paymentReportService.findCompanyReport(id);
    }

    @RequestMapping(path = "/error/{id}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentErrorReportResponse>>> findErrorReport(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
            @PathVariable("type") Long type) throws Exception {
        return paymentReportService.findErrorReport(id, type);
    }

    @RequestMapping(path = "/areaWithPaymentMethod/{id}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentAreaReportResponse>> findAreaWithPaymentMethodReport(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
            @PathVariable("type") Long type) throws Exception {
        return paymentReportService.findAreaReport(id, type);
    }

    @RequestMapping(path = "/countryWithPaymentMethod/{id}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentCountryReportResponse>> findCountryWithPaymentMethodReport(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
            @PathVariable("type") Long type) throws Exception {
        return paymentReportService.findCountryReport(id, type);

    }

    @RequestMapping(path = "/currencyWithPaymentMethod/{id}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentCurrencyReportResponse>> findCurrencyWithPaymentMethodReport(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
            @PathVariable("type") Long type) throws Exception {
        return paymentReportService.findCurrencyReport(id, type);
    }

    @RequestMapping(path = "/paymentMethod/{id}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentPaymentMethodReportResponse>> findPaymentReport(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
            @PathVariable("type") Long type) throws Exception {
        return paymentReportService.findPaymentMethodReport(id, type);
    }

    @RequestMapping(path = "/bank/{id}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentBankReportResponse>> findBankReport(
            @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id,
            @PathVariable("type") Long type) throws Exception {
        return paymentReportService.findBankReport(id, type);
    }

    @RequestMapping(path = "/duplicatePayment", method = RequestMethod.POST)
    public ResponseEntity<Result<List<DuplicatePaymentReportResponse>>> duplicatePaymentReport(@RequestBody DuplicatePaymentReport request) {
        return paymentReportService.findAllDuplicatePaymentReport(request);
    }
}

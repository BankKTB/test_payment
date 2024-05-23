package th.com.bloomcode.paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.request.ReportJsonRequest;
import th.com.bloomcode.paymentservice.webservice.client.PurchaseOrderXMLService;
import th.com.bloomcode.paymentservice.webservice.model.POChangeHistoryRequest;
import th.com.bloomcode.paymentservice.webservice.model.POChangeHistoryResponse;
import th.com.bloomcode.paymentservice.webservice.model.POHistoryRequest;
import th.com.bloomcode.paymentservice.webservice.model.POHistoryResponse;
import th.com.bloomcode.paymentservice.webservice.model.request.ReqPOSearchDetail;
import th.com.bloomcode.paymentservice.webservice.model.response.ResSearchDetail;

import java.io.ByteArrayInputStream;
import java.util.Calendar;


@RestController
@RequestMapping(path = "/api/purchasingOrder")
public class PurchasingOrderController {

    private final PurchaseOrderXMLService purchaseOrderXMLService;

    public static byte[] asByteArray(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return bts;
    }

    @Autowired
    public PurchasingOrderController(PurchaseOrderXMLService purchaseOrderXMLService) {
        this.purchaseOrderXMLService = purchaseOrderXMLService;
    }


    @RequestMapping(path = "searchDetail", method = RequestMethod.POST)
    public ResponseEntity<Result<ResSearchDetail>> searchDetail(
            @RequestBody ReqPOSearchDetail request)
            throws JsonProcessingException {
        return this.purchaseOrderXMLService.poSearchDetail(request);
    }

    @RequestMapping(path = "history", method = RequestMethod.POST)
    public ResponseEntity<Result<POHistoryResponse>> history(
            @RequestBody POHistoryRequest request)
            throws JsonProcessingException {
        return this.purchaseOrderXMLService.history(request);
    }

    @RequestMapping(path = "changeHistory", method = RequestMethod.POST)
    public ResponseEntity<Result<POChangeHistoryResponse>> changeHistory(
            @RequestBody POChangeHistoryRequest request)
            throws JsonProcessingException {
        return this.purchaseOrderXMLService.changeHistory(request);
    }

    @RequestMapping(
            path = "po",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity po(
            @RequestBody ReportJsonRequest request) {
        String hex = this.purchaseOrderXMLService.reportPO(request);

        ByteArrayInputStream bis = new ByteArrayInputStream(asByteArray(hex));

        Calendar calendar = Calendar.getInstance();
        long timeMilli = calendar.getTimeInMillis();
        String fileName;
        if (request.getFormat().equalsIgnoreCase("pdf")) {
            fileName = timeMilli + ".pdf";
        } else if (request.getFormat().equalsIgnoreCase("xls")) {
            fileName = timeMilli + ".xls";
        } else {
            fileName = timeMilli + ".doc";
        }

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }


}

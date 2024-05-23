package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.model.response.JuDetailDocument;
import th.com.bloomcode.paymentservice.model.response.JuHeadDocumentResponse;
import th.com.bloomcode.paymentservice.service.GenerateDocumentJuService;
import th.com.bloomcode.paymentservice.model.Raw;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/genJu")
public class GenerateJUController {

    private final GenerateDocumentJuService generateDocumentJuService;

    @Autowired
    public GenerateJUController(GenerateDocumentJuService generateDocumentJuService) {
        this.generateDocumentJuService = generateDocumentJuService;
    }

    @RequestMapping(path = "/document", method = RequestMethod.POST)
    public ResponseEntity<Result<List<JuHeadDocumentResponse>>> generateFileJu(@RequestBody GenerateJuRequest request) throws Exception {
        return this.generateDocumentJuService.generateFileJu(request);
    }

    @RequestMapping(path = "/document/exportPdf", method = RequestMethod.POST)
    public ResponseEntity<Result<Raw>> generateFileJuExportPdf(@RequestBody GenerateJuRequest request) {
        return generateDocumentJuService.generateFileJuExportPdf(request);
    }

    @RequestMapping(path = "/document/exportExcel", method = RequestMethod.POST)
    public ResponseEntity<Result<Raw>> generateFileJuExportExcel(@RequestBody GenerateJuRequest request) {
        return generateDocumentJuService.generateFileJuExportExcel(request);
    }

    @RequestMapping(path = "/getJuDocument/detail", method = RequestMethod.POST)
    public ResponseEntity<Result<JuDetailDocument>> getJUDocumentDetail(@RequestBody GenerateJuRequest request) throws Exception {
        return this.generateDocumentJuService.getJUDocumentDetail(request);
    }

    @RequestMapping(path = "/getJuDocument/detail/exportPdf", method = RequestMethod.POST)
    public ResponseEntity<Result<Raw>> getJUDocumentDetailExportPdf(@RequestBody GenerateJuRequest request) {
        return generateDocumentJuService.getJUDocumentDetailExportPdf(request);
    }

    @RequestMapping(path = "/getJuDocument/detail/exportExcel", method = RequestMethod.POST)
    public ResponseEntity<Result<Raw>> getJUDocumentDetailExportExcel(@RequestBody GenerateJuRequest request) {
        return generateDocumentJuService.getJUDocumentDetailExportExcel(request);
    }
}

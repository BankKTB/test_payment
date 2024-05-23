package th.com.bloomcode.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.model.response.JuHeadDocumentResponse;
import th.com.bloomcode.paymentservice.model.response.SelectGroupDocumentPreviewResponse;
import th.com.bloomcode.paymentservice.model.response.SelectGroupDocumentResponse;
import th.com.bloomcode.paymentservice.service.payment.SelectGroupDocumentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/api/selectGroupDocument")
public class SelectGroupDocumentController {

    private final SelectGroupDocumentService selectGroupDocumentService;

    public SelectGroupDocumentController(SelectGroupDocumentService selectGroupDocumentService) {
        this.selectGroupDocumentService = selectGroupDocumentService;
    }


    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<SelectGroupDocumentResponse>> save(@Valid @RequestBody SelectGroupDocument request) {
        return selectGroupDocumentService.create(request);

    }

    @RequestMapping(path = "/preview", method = RequestMethod.POST)
    public ResponseEntity<Result<SelectGroupDocumentPreviewResponse>> preview(HttpServletRequest httpServletRequest, @Valid @RequestBody SelectGroupDocument request) {
        return selectGroupDocumentService.preview(httpServletRequest, request);

    }

//    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Result<PaymentAlias>> save(@Valid @RequestBody PaymentAlias request,
//                                                     @PathVariable("id") @Min(value = 1, message = "Id must be greater than or equal to 1") Long id) {
//        return selectGroupDocumentService.update(request);
//
//    }


}

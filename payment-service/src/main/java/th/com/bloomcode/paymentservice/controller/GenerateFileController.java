package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.GenerateFile;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.GenerateFileAliasResponse;
import th.com.bloomcode.paymentservice.model.request.AuthRegen;
import th.com.bloomcode.paymentservice.model.request.GenerateFileAliasRequest;
import th.com.bloomcode.paymentservice.service.GenerateFileService;
import th.com.bloomcode.paymentservice.service.idem.CABOBUserService;
import th.com.bloomcode.paymentservice.service.payment.GenerateFileAliasService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/genFile")
public class GenerateFileController {

    private final GenerateFileAliasService generateFileAliasService;
    private final GenerateFileService generateFileService;
    private final CABOBUserService cabobUserService;

    @Autowired
    public GenerateFileController(GenerateFileAliasService generateFileAliasService, GenerateFileService generateFileService, CABOBUserService cabobUserService) {
        this.generateFileAliasService = generateFileAliasService;
        this.generateFileService = generateFileService;
        this.cabobUserService = cabobUserService;
    }

    @RequestMapping(path = "/search/{generateFileDate}/{generateFileName}", method = RequestMethod.GET)
    public ResponseEntity<Result<GenerateFileAliasResponse>> search(
            @PathVariable("generateFileDate") @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull(message = "GenerateFile date is required") Date generateFileDate,
            @PathVariable("generateFileName") @NotBlank(message = "GenerateFile name is required") String generateFileName) {
        return generateFileAliasService.searchByGenerateFileDateAndGenerateFileName(generateFileDate, generateFileName);
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<Result<GenerateFileAlias>> save(@Valid @RequestBody GenerateFileAliasRequest request) {
        return generateFileAliasService.save(request);

    }

    @RequestMapping(path = "/getAll/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<GenerateFileAliasResponse>>> findByCondition(@PathVariable("value") String value) throws Exception {
        return this.generateFileAliasService.findByCondition(value);
    }

    @RequestMapping(path = "/getReturn/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<GenerateFileAliasResponse>>> findByReturn(@PathVariable("value") String value) throws Exception {
        return this.generateFileAliasService.findByReturn(value);
    }

//    @RequestMapping(path = "/getByValue/{value}", method = RequestMethod.GET)
//    public ResponseEntity<Result<List<GenerateFileAliasResponse>>> getAreaCodeByValue(@PathVariable("value") String value)
//            throws Exception {
//        return this.generateFileAliasService.findByValueCodeContaining(value);
//    }

//    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Result> deleteGenFile(@PathVariable("id") Long id) throws Exception {
//        return this.generateFileAliasService.deleteGenFile(id);
//    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> genFile(@PathVariable("id") Long id, @RequestBody GenerateFileAliasRequest request) throws Exception {
        return this.generateFileService.generateFile(id, request, false);
    }

    @RequestMapping(path = "/pac/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> genFilePac(@PathVariable("id") Long id, @RequestBody GenerateFileAliasRequest request) throws Exception {
        return this.generateFileService.generateFile(id, request, true);
    }

    @RequestMapping(path = "report/{id}", method = RequestMethod.GET)
    public ResponseEntity<Result<GenerateFile>> report(@PathVariable("id") Long id) throws Exception {
        return this.generateFileService.report(id);
//        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "regen", method = RequestMethod.POST)
    public ResponseEntity<Result<Boolean>> regen(@RequestBody AuthRegen request) throws Exception {
        return this.cabobUserService.existByUsernameAndPassword(request.getUsername(), request.getPassword());
    }

}

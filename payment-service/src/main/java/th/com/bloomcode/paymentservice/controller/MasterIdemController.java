package th.com.bloomcode.paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.webservice.client.MasterXMLService;
import th.com.bloomcode.paymentservice.webservice.model.request.*;
import th.com.bloomcode.paymentservice.webservice.model.response.*;

@RestController
@RequestMapping(path = "/api/masterIdem")
@RequiredArgsConstructor
public class MasterIdemController {

    private final MasterXMLService masterXMLService;

    @RequestMapping(path = "searchHoliday", method = RequestMethod.POST)
    public ResponseEntity<Result<PYHolidaySearchResponse>> searchDetail(@RequestBody PYHolidaySearchRequest request) {
        return this.masterXMLService.searchHoliday(request);
    }

    @RequestMapping(path = "createHoliday", method = RequestMethod.POST)
    public ResponseEntity<Result<PYHolidayCreateResponse>> createHoliday(@RequestBody PYHolidayCreateRequest request) throws JsonProcessingException {
        return this.masterXMLService.createHoliday(request);
    }

    @RequestMapping(path = "updateHoliday", method = RequestMethod.POST)
    public ResponseEntity<Result<PYHolidayCreateResponse>> updateHoliday(@RequestBody PYHolidayCreateRequest request) throws JsonProcessingException {
        return this.masterXMLService.updateHoliday(request);
    }
}

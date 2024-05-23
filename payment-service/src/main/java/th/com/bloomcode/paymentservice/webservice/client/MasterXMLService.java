package th.com.bloomcode.paymentservice.webservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.ParamValues;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.RunProcessRequest;
import org.idempiere.webservice.client.response.RunProcessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.config.ConfigIdempiere;
import th.com.bloomcode.paymentservice.webservice.model.request.PYHolidayCreateRequest;
import th.com.bloomcode.paymentservice.webservice.model.request.PYHolidaySearchRequest;
import th.com.bloomcode.paymentservice.webservice.model.response.PYHolidayCreateResponse;
import th.com.bloomcode.paymentservice.webservice.model.response.PYHolidaySearchResponse;

import java.util.Objects;

@Slf4j
@Service
public class MasterXMLService {
    public ResponseEntity<Result<PYHolidaySearchResponse>> searchHoliday(PYHolidaySearchRequest request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("PY_HOLIDAY_SEARCH");

        CAClient client = Context.getUrl("99999");
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(PYHolidaySearchRequest.class, request);
            log.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);


        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<PYHolidaySearchResponse> result = new Result<>();
        result.setXml(xml);
        PYHolidaySearchResponse json = new PYHolidaySearchResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            log.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            log.info("xml : {}", connection.getXmlRequest());
            log.info("response error : {}", response.getErrorMessage());
            log.info("response loginfo: {}", response.getLogInfo());
            log.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    PYHolidaySearchResponse xmlResponse = XMLUtil.xmlUnmarshall(PYHolidaySearchResponse.class, response.getErrorMessage());
                    result.setMessage("Web Service Validate Error");
                    result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.setData(Objects.requireNonNullElse(xmlResponse, json));
                    return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
                } catch (Exception e) {
                    if ("Unknown XML error".equalsIgnoreCase(e.getMessage())) {
                        result.setMessage(response.getErrorMessage());
                        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        result.setData(null);
                        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                PYHolidaySearchResponse xmlResponse = XMLUtil.xmlUnmarshall(PYHolidaySearchResponse.class, response.getSummary());
                if (null != xmlResponse) {
                    result.setMessage("");
                    result.setStatus(HttpStatus.OK.value());
                    result.setData(xmlResponse);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setMessage("Web Service Error:");
                    result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    result.setData(null);
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("ไม่สามารถเชื่อมต่อกับเซอร์วิสได้");
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Result<PYHolidayCreateResponse>> createHoliday(PYHolidayCreateRequest request) throws JsonProcessingException {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("PY_HOLIDAY_CREATE");

        CAClient client = Context.getUrl("99999");
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(PYHolidayCreateRequest.class, request);
            log.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);


        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<PYHolidayCreateResponse> result = new Result<>();
        result.setXml(xml);
        PYHolidayCreateResponse json = new PYHolidayCreateResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            log.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            log.info("xml : {}", connection.getXmlRequest());
            log.info("response error : {}", response.getErrorMessage());
            log.info("response loginfo: {}", response.getLogInfo());
            log.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    PYHolidayCreateResponse xmlResponse = XMLUtil.xmlUnmarshall(PYHolidayCreateResponse.class, response.getErrorMessage());
                    result.setMessage("Web Service Validate Error");
                    result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.setData(Objects.requireNonNullElse(xmlResponse, json));
                    return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
                } catch (Exception e) {
                    if ("Unknown XML error".equalsIgnoreCase(e.getMessage())) {
                        result.setMessage(response.getErrorMessage());
                        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        result.setData(null);
                        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                PYHolidayCreateResponse xmlResponse = XMLUtil.xmlUnmarshall(PYHolidayCreateResponse.class, response.getSummary());
                if (null != xmlResponse) {
                    result.setMessage("");
                    result.setStatus(HttpStatus.OK.value());
                    result.setData(xmlResponse);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setMessage("Web Service Error:");
                    result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    result.setData(null);
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("ไม่สามารถเชื่อมต่อกับเซอร์วิสได้");
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Result<PYHolidayCreateResponse>> updateHoliday(PYHolidayCreateRequest request) throws JsonProcessingException {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("PY_HOLIDAY_CHANGE");

        CAClient client = Context.getUrl("99999");
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(PYHolidayCreateRequest.class, request);
            log.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);


        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<PYHolidayCreateResponse> result = new Result<>();
        result.setXml(xml);
        PYHolidayCreateResponse json = new PYHolidayCreateResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            log.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            log.info("xml : {}", connection.getXmlRequest());
            log.info("response error : {}", response.getErrorMessage());
            log.info("response loginfo: {}", response.getLogInfo());
            log.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    PYHolidayCreateResponse xmlResponse = XMLUtil.xmlUnmarshall(PYHolidayCreateResponse.class, response.getErrorMessage());
                    result.setMessage("Web Service Validate Error");
                    result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.setData(Objects.requireNonNullElse(xmlResponse, json));
                    return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
                } catch (Exception e) {
                    if ("Unknown XML error".equalsIgnoreCase(e.getMessage())) {
                        result.setMessage(response.getErrorMessage());
                        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        result.setData(null);
                        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                PYHolidayCreateResponse xmlResponse = XMLUtil.xmlUnmarshall(PYHolidayCreateResponse.class, response.getSummary());
                if (null != xmlResponse) {
                    result.setMessage("");
                    result.setStatus(HttpStatus.OK.value());
                    result.setData(xmlResponse);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setMessage("Web Service Error:");
                    result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    result.setData(null);
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("ไม่สามารถเชื่อมต่อกับเซอร์วิสได้");
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

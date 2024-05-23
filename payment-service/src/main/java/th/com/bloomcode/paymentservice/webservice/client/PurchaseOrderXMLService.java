package th.com.bloomcode.paymentservice.webservice.client;

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
import th.com.bloomcode.paymentservice.model.request.ReportJsonRequest;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.config.ConfigIdempiere;
import th.com.bloomcode.paymentservice.webservice.model.POChangeHistoryRequest;
import th.com.bloomcode.paymentservice.webservice.model.POChangeHistoryResponse;
import th.com.bloomcode.paymentservice.webservice.model.POHistoryRequest;
import th.com.bloomcode.paymentservice.webservice.model.POHistoryResponse;
import th.com.bloomcode.paymentservice.webservice.model.request.ReqPOSearchDetail;
import th.com.bloomcode.paymentservice.webservice.model.response.ResSearchDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PurchaseOrderXMLService {

    private static final List<String> listPoDocNo = new ArrayList<String>();
    private static final List<Object> listError = new ArrayList<Object>();
    private final ObjectMapper mapper = new ObjectMapper();

    public ResponseEntity<Result<ResSearchDetail>> poSearchDetail(ReqPOSearchDetail request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("YBAPI_PO_SEARCH_DETAIL");

        CAClient client = Context.getUrl(request.getCompanyCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(ReqPOSearchDetail.class, request);
            log.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);

        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<ResSearchDetail> result = new Result<>();
        result.setXml(xml);
        ResSearchDetail json = new ResSearchDetail();
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
                    ResSearchDetail xmlResponse = XMLUtil.xmlUnmarshall(ResSearchDetail.class, response.getErrorMessage());
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
                ResSearchDetail xmlResponse = XMLUtil.xmlUnmarshall(ResSearchDetail.class, response.getSummary());
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

    public ResponseEntity<Result<POHistoryResponse>> history(POHistoryRequest request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("PO_HISTORY");

        CAClient client = Context.getUrl(request.getCompanyCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(POHistoryRequest.class, request);
            log.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);

        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<POHistoryResponse> result = new Result<>();
        result.setXml(xml);
        POHistoryResponse json = new POHistoryResponse();
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
                    POHistoryResponse xmlResponse = XMLUtil.xmlUnmarshall(POHistoryResponse.class, response.getErrorMessage());
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
                POHistoryResponse xmlResponse = XMLUtil.xmlUnmarshall(POHistoryResponse.class, response.getSummary());
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

    public ResponseEntity<Result<POChangeHistoryResponse>> changeHistory(POChangeHistoryRequest request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("PO_CHANGE_HISTORY");

        CAClient client = Context.getUrl(request.getCompanyCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(POChangeHistoryRequest.class, request);
            log.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);

        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<POChangeHistoryResponse> result = new Result<>();
        result.setXml(xml);
        POChangeHistoryResponse json = new POChangeHistoryResponse();
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
                    POChangeHistoryResponse xmlResponse = XMLUtil.xmlUnmarshall(POChangeHistoryResponse.class, response.getErrorMessage());
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
                POChangeHistoryResponse xmlResponse = XMLUtil.xmlUnmarshall(POChangeHistoryResponse.class, response.getSummary());
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

    public String reportPO(ReportJsonRequest request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("PO_REPORT");
        CAClient client = Context.getUrl(request.getCompCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        ParamValues paramValues = new ParamValues();
        paramValues.addField("PO_DOC_NO", request.getPoDocNo());
        paramValues.addField(
                "COMP_CODE", request.getCompCode());
        paramValues.addField("REPORT_TYPE", request.getReportType());
        paramValues.addField("EXPORT_FORMAT", request.getFormat());
        ws.setParamValues(paramValues);

        WebServiceConnection connection = ConfigIdempiere.getConnection(client);
        try {
            RunProcessResponse response = connection.sendRequest(ws);
            connection.writeRequest(System.out);
            System.out.println();
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                //                result.setStatus(Result.F);
                //                result.setData(json);
            } else {
                log.debug("report : {}", response.getSummary());
                return response.getSummary();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}

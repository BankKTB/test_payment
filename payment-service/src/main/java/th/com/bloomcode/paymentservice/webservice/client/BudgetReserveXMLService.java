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
import th.com.bloomcode.paymentservice.model.request.BRConsumptionRequest;
import th.com.bloomcode.paymentservice.model.request.BRSearchDetailRequest;
import th.com.bloomcode.paymentservice.model.response.BRConsumptionResponse;
import th.com.bloomcode.paymentservice.model.response.BRSearchDetailResponse;
import th.com.bloomcode.paymentservice.util.Util;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.config.ConfigIdempiere;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BudgetReserveXMLService {

  private final ObjectMapper mapper = new ObjectMapper();

  public ResponseEntity<Result<BRSearchDetailResponse>> searchDetail(
      BRSearchDetailRequest request) {
    RunProcessRequest ws = new RunProcessRequest();
    ws.setWebServiceType("BR_SEARCH_DETAIL");
    CAClient client = Context.getUrl(request.getCompanyCode());
    ws.setLogin(ConfigIdempiere.getLogin(client));
    String xml = "";
    try {
      xml = XMLUtil.xmlMarshall(BRSearchDetailRequest.class, request);
      log.info("xml : {}", xml);
    } catch (Exception e) {
      e.printStackTrace();
    }
    ParamValues paramValues = new ParamValues();
    paramValues.addField("XMLMessage", xml);
    ws.setParamValues(paramValues);
    WebServiceConnection connection = ConfigIdempiere.getConnection(client);
    Result<BRSearchDetailResponse> result = new Result<>();
    result.setXml(xml);
    BRSearchDetailResponse json = new BRSearchDetailResponse();
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
          BRSearchDetailResponse xmlResponse =
              XMLUtil.xmlUnmarshall(BRSearchDetailResponse.class, response.getErrorMessage());
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
        BRSearchDetailResponse xmlResponse =
            XMLUtil.xmlUnmarshall(BRSearchDetailResponse.class, response.getSummary());
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

  public ResponseEntity<Result<BRConsumptionResponse>> consumption(BRConsumptionRequest request) {
    RunProcessRequest ws = new RunProcessRequest();
    ws.setWebServiceType("BR_CONSUMPTION");
    CAClient client = Context.getUrl(request.getCompanyCode());
    ws.setLogin(ConfigIdempiere.getLogin(client));
    String xml = "";
    try {
      xml = XMLUtil.xmlMarshall(BRConsumptionRequest.class, request);
      log.info("xml : {}", xml);
    } catch (Exception e) {
      e.printStackTrace();
    }
    ParamValues paramValues = new ParamValues();
    paramValues.addField("XMLMessage", xml);
    ws.setParamValues(paramValues);
    WebServiceConnection connection = ConfigIdempiere.getConnection(client);
    Result<BRConsumptionResponse> result = new Result<>();
    result.setXml(xml);
    BRConsumptionResponse json = new BRConsumptionResponse();
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
          BRConsumptionResponse xmlResponse =
              XMLUtil.xmlUnmarshall(BRConsumptionResponse.class, response.getErrorMessage());
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
        BRConsumptionResponse xmlResponse =
            XMLUtil.xmlUnmarshall(BRConsumptionResponse.class, response.getSummary());
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

package th.com.bloomcode.paymentservice.webservice.client;

import lombok.extern.slf4j.Slf4j;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.RunProcessRequest;
import org.idempiere.webservice.client.response.RunProcessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.util.XMLUtil;

@Slf4j
public class MetadataXMLService<T> {

  public ResponseEntity<Result<T>> sendAndReceive(
      Result<T> result, RunProcessRequest ws, WebServiceConnection connection, Class<T> clazz) {
    try {
      RunProcessResponse runProcessResponse = connection.sendRequest(ws);
      log.debug("xml : {}", connection.getXmlRequest());
      log.debug("response error : {}", runProcessResponse.getErrorMessage());
      log.debug("response loginfo: {}", runProcessResponse.getLogInfo());
      log.debug("response summary: {}", runProcessResponse.getSummary());
      T response;
      if (runProcessResponse.getStatus() == Enums.WebServiceResponseStatus.Error) {
        try {
          response = XMLUtil.xmlUnmarshall(clazz, runProcessResponse.getErrorMessage());
          result.setMessage("Web Service Validate Error");
          result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
          result.setData(response);
          return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch (IndexOutOfBoundsException e) {
          if ("Index 0 out of bounds for length 0".equalsIgnoreCase(e.getMessage())) {
            result.setMessage(runProcessResponse.getErrorMessage());
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
          }
          return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
          if ("Unknown XML error".equalsIgnoreCase(e.getMessage())) {
            result.setMessage(runProcessResponse.getErrorMessage());
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
          }
          return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
      } else {
        response = XMLUtil.xmlUnmarshall(clazz, runProcessResponse.getSummary());
        if (null != response) {
          result.setMessage("");
          result.setStatus(HttpStatus.OK.value());
          result.setData(response);
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

  public T sendAndReceive(RunProcessRequest ws, WebServiceConnection connection, Class<T> clazz) {
    try {
      RunProcessResponse runProcessResponse = connection.sendRequest(ws);
      log.debug("xml : {}", connection.getXmlRequest());
      log.debug("response error : {}", runProcessResponse.getErrorMessage());
      log.debug("response loginfo: {}", runProcessResponse.getLogInfo());
      log.debug("response summary: {}", runProcessResponse.getSummary());
      T response;
      if (runProcessResponse.getStatus() == Enums.WebServiceResponseStatus.Error) {
        try {
          response = XMLUtil.xmlUnmarshall(clazz, runProcessResponse.getErrorMessage());
          return response;
        } catch (Exception e) {
          if ("Unknown XML error".equalsIgnoreCase(e.getMessage())) {
            return null;
          }
          return null;
        }
      } else {
        response = XMLUtil.xmlUnmarshall(clazz, runProcessResponse.getSummary());
        return response;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

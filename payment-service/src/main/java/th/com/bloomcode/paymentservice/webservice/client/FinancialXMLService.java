package th.com.bloomcode.paymentservice.webservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.ParamValues;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.RunProcessRequest;
import org.idempiere.webservice.client.response.RunProcessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.service.payment.GLHeadService;
import th.com.bloomcode.paymentservice.service.payment.InsertDocumentFromChangeDocumentService;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.util.TimestampUtil;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.config.ConfigIdempiere;
import th.com.bloomcode.paymentservice.webservice.model.request.*;
import th.com.bloomcode.paymentservice.webservice.model.response.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Service
public class FinancialXMLService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final InsertDocumentFromChangeDocumentService insertDocumentFromChangeDocumentService;
    private final GLHeadService glHeadService;
    private final PaymentAliasService paymentAliasService;

    public FinancialXMLService(InsertDocumentFromChangeDocumentService insertDocumentFromChangeDocumentService, GLHeadService glHeadService, PaymentAliasService paymentAliasService) {
        this.insertDocumentFromChangeDocumentService = insertDocumentFromChangeDocumentService;
        this.glHeadService = glHeadService;
        this.paymentAliasService = paymentAliasService;
    }

    public ResponseEntity<Result<FISearchDetailResponse>> fiSearchDetail(FISearchDetailRequest request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("FI_DOCUMENT_DETAIL_SEARCH2");

        CAClient client = Context.getUrl(request.getCompCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(FISearchDetailRequest.class, request);
            logger.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);

        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<FISearchDetailResponse> result = new Result<>();
        result.setXml(xml);
        FISearchDetailResponse json = new FISearchDetailResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            logger.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            logger.info("xml : {}", connection.getXmlRequest());
            logger.info("response error : {}", response.getErrorMessage());
            logger.info("response loginfo: {}", response.getLogInfo());
            logger.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    FISearchDetailResponse xmlResponse = XMLUtil.xmlUnmarshall(FISearchDetailResponse.class, response.getErrorMessage());
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
                FISearchDetailResponse xmlResponse = XMLUtil.xmlUnmarshall(FISearchDetailResponse.class, response.getSummary());
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

    public ResponseEntity<Result<FIResponse>> searchAutoDoc(FIResultRequest request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("FI_RESULT");

        CAClient client = Context.getUrl(request.getCompCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(FIResultRequest.class, request);
            logger.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);


        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<FIResponse> result = new Result<>();
        result.setXml(xml);
        FIResponse json = new FIResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            logger.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            logger.info("xml : {}", connection.getXmlRequest());
            logger.info("response error : {}", response.getErrorMessage());
            logger.info("response loginfo: {}", response.getLogInfo());
            logger.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    FIResponse xmlResponse = XMLUtil.xmlUnmarshall(FIResponse.class, response.getErrorMessage());
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
                FIResponse xmlResponse = XMLUtil.xmlUnmarshall(FIResponse.class, response.getSummary());
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

    public ResponseEntity<Result<FICPbkDetailResponse>> paymentBlockDetail(FICPbkDetailRequest request) throws JsonProcessingException {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("FI_CHANGE_PAYMENT_BLOCK_DETAIL");

        CAClient client = Context.getUrl(request.getCompCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(FICPbkDetailRequest.class, request);
            logger.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);


        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<FICPbkDetailResponse> result = new Result<>();
        result.setXml(xml);
        FICPbkDetailResponse json = new FICPbkDetailResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            logger.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            logger.info("xml : {}", connection.getXmlRequest());
            logger.info("response error : {}", response.getErrorMessage());
            logger.info("response loginfo: {}", response.getLogInfo());
            logger.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    FICPbkDetailResponse xmlResponse = XMLUtil.xmlUnmarshall(FICPbkDetailResponse.class, response.getErrorMessage());
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
                FICPbkDetailResponse xmlResponse = XMLUtil.xmlUnmarshall(FICPbkDetailResponse.class, response.getSummary());
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

    public ResponseEntity<Result<APUpdateLineVendorResponse>> updateLineVendor(APUpdateLineVendorRequest request) throws JsonProcessingException {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("AP_UPDATE_LINE_VENDOR");

        CAClient client = Context.getUrl(request.getCompCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(APUpdateLineVendorRequest.class, request);
            logger.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);


        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<APUpdateLineVendorResponse> result = new Result<>();
        result.setXml(xml);
        APUpdateLineVendorResponse json = new APUpdateLineVendorResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            logger.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            logger.info("xml : {}", connection.getXmlRequest());
            logger.info("response error : {}", response.getErrorMessage());
            logger.info("response loginfo: {}", response.getLogInfo());
            logger.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    APUpdateLineVendorResponse xmlResponse = XMLUtil.xmlUnmarshall(APUpdateLineVendorResponse.class, response.getErrorMessage());
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
                APUpdateLineVendorResponse xmlResponse = XMLUtil.xmlUnmarshall(APUpdateLineVendorResponse.class, response.getSummary());
                if (null != xmlResponse) {
                    insertDocumentFromChangeDocumentService.insertDocument(xmlResponse.getFiDoc(), jwt.getSub(), new Timestamp(System.currentTimeMillis()));
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


    public ResponseEntity<Result<String>> validateUpdateLineVendor(APUpdateLineVendorRequest request) throws JsonProcessingException {
        Result<String> result = new Result<>();
        result.setTimestamp(new Date());
        try {

                GLHead glHead = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(request.getCompCode(), request.getAccountDocNo(), request.getFiscalYear());

                String textError =null;
                if(glHead == null ){
                    textError=null;
                }else if (glHead != null && (glHead.getPaymentId() == null || glHead.getPaymentId() ==0)){
                    textError=null;
                }else{
                    PaymentAlias paymentAlias = paymentAliasService.findOneById(glHead.getPaymentId());
                    textError="ไม่สามารถเปลี่ยนแปลงรายการเนื่องจากอยู่ใน "+ paymentAlias.getPaymentName() +" "+ TimestampUtil.dateThai( TimestampUtil.timestampToString(paymentAlias.getPaymentDate()));
                }
            result.setData(textError);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<FICheckOriginalDocResponse>> checkOriginalDocument(FICheckOriginalDocRequest request) throws JsonProcessingException {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("FI_CHECK_ORIGINAL_DOC");
        CAClient client = null;
        System.out.println(request);
        if(request.getLines().size()>0){
            client = Context.getUrl(request.getLines().get(0).getCompCode());
        }


        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(FICheckOriginalDocRequest.class, request);
            logger.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);


        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<FICheckOriginalDocResponse> result = new Result<>();
        result.setXml(xml);
        FICheckOriginalDocResponse json = new FICheckOriginalDocResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            logger.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            logger.info("xml : {}", connection.getXmlRequest());
            logger.info("response error : {}", response.getErrorMessage());
            logger.info("response loginfo: {}", response.getLogInfo());
            logger.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    FICheckOriginalDocResponse xmlResponse = XMLUtil.xmlUnmarshall(FICheckOriginalDocResponse.class, response.getErrorMessage());
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
                FICheckOriginalDocResponse xmlResponse = XMLUtil.xmlUnmarshall(FICheckOriginalDocResponse.class, response.getSummary());
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


    public ResponseEntity<Result<FISearchResponse2>> search(FISearchRequest2 request) {
        RunProcessRequest ws = new RunProcessRequest();
        ws.setWebServiceType("FI_DOCUMENT_SEARCH2");

        CAClient client = Context.getUrl(request.getCompCode());
        ws.setLogin(ConfigIdempiere.getLogin(client));

        String xml = "";
        try {
            xml = XMLUtil.xmlMarshall(FISearchRequest2.class, request);
            logger.info("xml : {}", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ParamValues paramValues = new ParamValues();
        paramValues.addField("XMLMessage", xml);
        ws.setParamValues(paramValues);

        WebServiceConnection connection = ConfigIdempiere.getConnection(client);

        Result<FISearchResponse2> result = new Result<>();
        result.setXml(xml);
        FISearchResponse2 json = new FISearchResponse2();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            logger.info("ResultingJSONstring : {}", jsonString);
            result.setJsonObject(jsonString);

            RunProcessResponse response = connection.sendRequest(ws);
            logger.info("xml : {}", connection.getXmlRequest());
            logger.info("response error : {}", response.getErrorMessage());
            logger.info("response loginfo: {}", response.getLogInfo());
            logger.info("response summary: {}", response.getSummary());
            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                try {
                    FISearchResponse2 xmlResponse = XMLUtil.xmlUnmarshall(FISearchResponse2.class, response.getErrorMessage());
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
                FISearchResponse2 xmlResponse = XMLUtil.xmlUnmarshall(FISearchResponse2.class, response.getSummary());
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

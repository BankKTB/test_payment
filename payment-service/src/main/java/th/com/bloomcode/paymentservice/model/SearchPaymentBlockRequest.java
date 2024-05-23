package th.com.bloomcode.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import th.com.bloomcode.paymentservice.model.common.*;
import th.com.bloomcode.paymentservice.webservice.adapter.JsonDateSerializer;

import java.sql.Timestamp;
import java.util.List;

@Data
public class SearchPaymentBlockRequest {

    private String compCodeFrom;
    private String compCodeTo;
    private String fiAreaFrom;
    private String fiAreaTo;
    private String fiscalYear;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateAcctFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateAcctTo;
    private String bPartnerFrom;
    private String bPartnerTo;
    private String paymentCenterFrom;
    private String paymentCenterTo;
    private String docTypeFrom;
    private String docTypeTo;
    private String paymentMethodFrom;
    private String paymentMethodTo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateDocFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateDocTo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateCreatedFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateCreatedTo;
    private String specialGlFrom;
    private String specialGlTo;
    private List<CompanyCodeCondition> listCompanyCode;
    private List<BaseRange> listDocType;
    private List<BaseRange> listPaymentMethod;
    private List<BaseRange> vendor;
    private List<BaseRange> paymentCenter;
    private List<BaseRange> specialType;

    private List<BaseRange> listPostDate;
    private List<BaseRange> listDocumentDate;
    private List<BaseRange> listDocumentCreateDate;

    private List<BaseRange> listFiArea;
    private List<BaseRange> listDocumentNo;
    private List<BaseRange> listHeaderReference;

}

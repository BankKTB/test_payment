package th.com.bloomcode.paymentservice.webservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BRHeader {
  @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
  private String docType = null;

  @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
  private String companyCode = null;

  @XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
  private String fiscalYear = null;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = StandardXMLTagName.DATE_DOC)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp dateDoc = null;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = StandardXMLTagName.DATE_ACCT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp dateAcct = null;

  @XmlElement(name = "CURRENCY")
  private String currency = null;

  @XmlElement(name = "HEAD_TEXT")
  private String headerText = null;

  @XmlElement(name = "REFERENCE_DOC")
  private String referenceDoc = null;

  @XmlElement(name = "REFERENCE2")
  private String reference2 = null;

  @XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
  private String paymentCenter = null;

  @XmlElement(name = StandardXMLTagName.BR_DOCUMENT_NO)
  private String reservationDocumentNo = null;

  @XmlElement(name = StandardXMLTagName.REASON_NO)
  private String reasonNo = null;

  @XmlElement(name = "REASON_DESCRIPTION")
  private String reasonDescription = null;

  @XmlElement(name = StandardXMLTagName.PAYMENT_CENTER_NAME)
  private String paymentCenterName = null;

  @XmlElement(name = StandardXMLTagName.USER_WEBONLINE)
  private String userWebOnline = null;

  @XmlElement(name = StandardXMLTagName.BR_RECORD_ID)
  private int brRecordID = 0;

  @XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS)
  private String docStatus = null;

  @XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS_NAME)
  private String docStatusName = null;

  @XmlElement(name = StandardXMLTagName.CANCEL_REASON)
  private String cancelReason = null;

  @XmlElement(name = StandardXMLTagName.COMPANY_CODE_NAME)
  private String companyCodeName = null;

  @XmlElement(name = StandardXMLTagName.OPEN_AMOUNT)
  private BigDecimal remainAmount = BigDecimal.ZERO;

  @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE_NAME)
  private String docTypeName = null;

  @XmlElement(name = StandardXMLTagName.REASON_NAME)
  private String reasonName = null;

}

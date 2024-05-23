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
public class BRSearchResult extends BudgetDimensionBaseXML {
  @XmlElement(name = StandardXMLTagName.BR_DOCUMENT_NO)
  private String brDocumentNo = null;

  @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
  private String companyCode = null;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = StandardXMLTagName.DATE_DOC)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp dateDoc = null;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = StandardXMLTagName.DATE_ACCT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp dateAcct = null;

  @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
  private String docType = null;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = "CREATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp dateCreated = null;

  @XmlElement(name = StandardXMLTagName.DESCRIPTION)
  private String description = null;

  @XmlElement(name = StandardXMLTagName.STATUS)
  private String status = null;

  @XmlElement(name = "STATUS_NAME")
  private String statusName = null;

  @XmlElement(name = StandardXMLTagName.AMOUNT)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount = BigDecimal.ZERO;

  @XmlElement(name = StandardXMLTagName.OPEN_AMOUNT)
  private BigDecimal openAmount = BigDecimal.ZERO;

  @XmlElement(name = StandardXMLTagName.BR_RECORD_ID)
  private int brRecordID = 0;

  @XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
  private String paymentCenter = null;

  @XmlElement(name = StandardXMLTagName.PAYMENT_CENTER_NAME)
  private String paymentCenterName = null;

  @XmlElement(name = "HEADER_TEXT")
  private String headerText = null;

  @XmlElement(name = StandardXMLTagName.COMPANY_CODE_NAME)
  private String companyCodeName = null;
}

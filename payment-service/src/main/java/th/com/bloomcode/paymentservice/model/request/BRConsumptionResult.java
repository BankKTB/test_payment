package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BRConsumptionResult {
  @XmlElement(name = StandardXMLTagName.CONSUME_TYPE)
  private String consumeType = null;

  @XmlElement(name = StandardXMLTagName.CONSUME_STATUS)
  private String consumeStatus = null;

  @XmlElement(name = StandardXMLTagName.DOCUMENT_NO)
  private String documentNo = null;

  @XmlElement(name = "LINE")
  private int lineNo = 0;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = StandardXMLTagName.POSTING_DATE)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp postingDate = null;

  @XmlElement(name = StandardXMLTagName.DESCRIPTION)
  private String description = null;

  @XmlElement(name = StandardXMLTagName.AMOUNT)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount = BigDecimal.ZERO;
}

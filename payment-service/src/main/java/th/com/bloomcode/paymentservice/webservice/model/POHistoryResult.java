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
public class POHistoryResult {

  @XmlElement(name = StandardXMLTagName.HISTORY_NAME)
  private String historyName;

  @XmlElement(name = StandardXMLTagName.DOCUMENT_NO)
  private String docNo;

  @XmlElement(name = StandardXMLTagName.LINE)
  private int line;

  @XmlElement(name = StandardXMLTagName.DATE_ACCT)
  @XmlJavaTypeAdapter(TimestampAdapter.class)
  private Timestamp dateAcct;

  @XmlElement(name = StandardXMLTagName.QUANTITY)
  private BigDecimal qty = BigDecimal.ZERO;

  @XmlElement(name = StandardXMLTagName.UOM)
  private String uom;

  @XmlElement(name = StandardXMLTagName.AMOUNT)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount = BigDecimal.ZERO;

  @XmlElement(name = StandardXMLTagName.CURRENCY)
  private String currency;

  @XmlElement(name = StandardXMLTagName.PO_REFERENCE)
  private String poReference;
}

package th.com.bloomcode.paymentservice.webservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.DateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class POChangeHistoryResult {

  @XmlElement(name = "LINE")
  private Integer line = null;

  @XmlElement(name = "TYPE")
  private String objectType = null;

  @XmlElement(name = "FIELD")
  private String fieldName = null;

  @XmlElement(name = "EVENT_TYPE")
  private String eventType = null;

  @XmlElement(name = "OLD_VALUE")
  private String oldValue = null;

  @XmlElement(name = "NEW_VALUE")
  private String newValue = null;

  @XmlElement(name = "UPDATE_USER")
  private String userName = null;

  @XmlJavaTypeAdapter(DateTimeAdapter.class)
  @XmlElement(name = "UPDATE_DATE")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  private Timestamp changeDate = null;
}

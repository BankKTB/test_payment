package th.com.bloomcode.paymentservice.webservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BRContract {

  @XmlElement(name = "ITEM_NO")
  private int line = 0;

  @XmlElement(name = "LOCALNAME")
  private String localName = null;

  @XmlElement(name = "CONTRACT_NO")
  private String contractNo = null;

  @XmlElement(name = "LC_NO")
  private String lcNo = null;

  @XmlElement(name = "DESCRIPTION")
  private String description = null;

  @XmlElement(name = "VENDOR")
  private String vendor = null;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = "SIGNDATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp signDate = null;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = "DUEDATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp dueDate = null;
}

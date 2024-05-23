package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class AbstractReqService {

  @XmlElement(name = "FLAG")
  private Integer flag;

  @XmlElement(name = "FORMID")
  private String formID;

  /** Idempiere : new field to keep web online user code */
  @XmlElement(name = "USERWEBONLINE")
  private String webUserCode;

  @XmlElement(name = "COMP_CODE")
  private String companyCode;

  @XmlElement(name = "WEBINFO")
  private WSWebInfo webInfo;
}

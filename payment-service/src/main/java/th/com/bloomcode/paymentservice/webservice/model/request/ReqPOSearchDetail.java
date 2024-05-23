package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.AbstractReqService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "REQUEST")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReqPOSearchDetail extends AbstractReqService {
  @XmlElement(name = "DOC_NO")
  private String docNo;

  @XmlElement(name = "DOC_TYPE")
  private String doctype;

  @XmlElement(name = "SEARCH_ADVANCE")
  private String searchAdvance = "N";
}

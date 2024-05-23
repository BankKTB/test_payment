package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractResService {
  @XmlElement(name = "COMP_CODE")
  private String companyCode;

  @XmlElementWrapper(name = StandardXMLTagName.ERROR_LIST)
  @XmlElement(name = StandardXMLTagName.ERROR_ENTRY)
  private List<BaseErrorLine> msgLines = new ArrayList<>();
}

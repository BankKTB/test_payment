package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.request.BRConsumptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BRConsumptionGroupResult {
  @XmlElementWrapper(name = "BRS")
  @XmlElement(name = "LINE")
  private List<BRConsumptionResult> brs = new ArrayList<>();

  @XmlElementWrapper(name = "POS")
  @XmlElement(name = "LINE")
  private List<BRConsumptionResult> pos = new ArrayList<>();

  @XmlElementWrapper(name = "ACS")
  @XmlElement(name = "LINE")
  private List<BRConsumptionResult> acs = new ArrayList<>();

  @XmlElementWrapper(name = "JVS")
  @XmlElement(name = "LINE")
  private List<BRConsumptionResult> jvs = new ArrayList<>();
}

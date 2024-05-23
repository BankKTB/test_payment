package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "HEADER")
@XmlAccessorType(XmlAccessType.FIELD)
public class POHeaderEntitySearch extends POHeaderEntityBase {
  /** Vendor Code */
  @XmlElement(name = "VENDOR_NAME")
  private String vendorCodeName;

  /** Company code = AD_Org TODO:? */
  @XmlElement(name = "COMP_CODE_NAME")
  private String companyCodeName;

  /** ProcureMethod */
  @XmlElement(name = "REF_1_NAME")
  private String procureMethodName;

  /** Payment Center */
  @XmlElement(name = "PAYMENT_CENTER_NAME")
  private String paymentCenterCodeName;

  @XmlElement(name = "FI_AREA_NAME")
  private String fiAreaName;
}

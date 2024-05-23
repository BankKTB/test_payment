package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSRequestBaseXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class BRConsumptionRequest extends WSRequestBaseXML {

  @XmlElement(name = StandardXMLTagName.BR_DOCUMENT_NO)
  private String reservationDocumentNo = null;

  @XmlElement(name = StandardXMLTagName.BR_RECORD_ID)
  private int reservationRecordID = 0;

  @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
  private String companyCode = null;
}

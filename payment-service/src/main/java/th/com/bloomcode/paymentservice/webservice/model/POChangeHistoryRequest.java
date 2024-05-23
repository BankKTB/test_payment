package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class POChangeHistoryRequest extends WSRequestBaseXML {
  @XmlElement(name = StandardXMLTagName.PO_DOCUMENT_NO)
  private String poDocumentNo = null;

  @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
  private String companyCode = null;
}

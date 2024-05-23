package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.FICreateResponseBase;
import th.com.bloomcode.paymentservice.webservice.model.FIHeader;
import th.com.bloomcode.paymentservice.webservice.model.FILine;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class FISearchDetailResponse {
	@XmlElement(name = StandardXMLTagName.HEADER)
	private FIHeader header;
	
	@XmlElementWrapper(name = StandardXMLTagName.ITEM_LIST)
	@XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
	private List<FILine> lines = new ArrayList<>();
	
	@XmlElementWrapper(name = StandardXMLTagName.AUTO_DOCUMENT_LIST)
	@XmlElement(name = StandardXMLTagName.AUTO_DOCUMENT_ENTRY)
	private List<FICreateResponseBase> autoDocs = new ArrayList<>();

	@XmlElementWrapper(name = StandardXMLTagName.INVOICE_DOCUMENT_LIST)
	@XmlElement(name = StandardXMLTagName.INVOICE_DOCUMENT_ENTRY)
	private List<FICreateResponseBase> invDocs = new ArrayList<>();

}

package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSResponseBaseXML;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class FICPbkDetailResponse extends WSResponseBaseXML {
	
	@XmlElementWrapper(name = StandardXMLTagName.FOLLOWING_LIST)
	@XmlElement(name = StandardXMLTagName.FOLLOWING_ENTRY)
	private List<FICPbkDoc> followingDoc = new ArrayList<>();
	
	@XmlElementWrapper(name = StandardXMLTagName.CHANGE_LOG_LIST)
	@XmlElement(name = StandardXMLTagName.CHANGE_LOG_ENTRY)
	private List<FICPbkChangeLog> changeLog = new ArrayList<>();
}

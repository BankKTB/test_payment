package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WSResponseBaseXML extends BaseMessage {
	
	public List<BaseErrorLine> getErrors() {
		return getMsgLines();
	}
	
}

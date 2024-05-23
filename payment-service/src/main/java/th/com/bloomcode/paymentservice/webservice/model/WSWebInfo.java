package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WSWebInfo {

	@XmlElement(name = StandardXMLTagName.IPADDRESS)
	private String ipAddress = "";
	@XmlElement(name = StandardXMLTagName.USER_WEBONLINE)
	private String userWebOnline = "";
	@XmlElement(name = StandardXMLTagName.FI_AREA)
	private String fiArea = "";
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = "";
	@XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
	private String paymentCenter = "";
	@XmlElement(name = StandardXMLTagName.AUTH_PAYMENT_CENTER)
	private List<String> authPaymentCenter = new ArrayList<>();
	@XmlElement(name = StandardXMLTagName.AUTH_COST_CENTER)
	private List<String> authCostCenter = new ArrayList<>();
	@XmlElement(name = StandardXMLTagName.AUTH_FI_AREA)
	private List<String> authFIArea = new ArrayList<>();
	@XmlElement(name = StandardXMLTagName.AUTH_COMP_CODE)
	private List<String> authCompanyCode = new ArrayList<>();
}

package th.com.bloomcode.paymentservice.webservice.adapter;

import java.util.Base64;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Base64Adapter extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String v) throws Exception {
		byte[] decodedBytes = Base64.getDecoder().decode(v);
		String decoded = new String(decodedBytes, "UTF-8");
		return decoded;
	}

	@Override
	public String marshal(String v) throws Exception {
		return Base64.getEncoder().encodeToString(v.getBytes("UTF-8"));
	}

}

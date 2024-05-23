package th.com.bloomcode.paymentservice.webservice.adapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Timestamp> {
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public Timestamp unmarshal(String v) throws Exception {
		if (v == null)
			return null;
		
		Date d = df.parse(v);
		return new Timestamp(d.getTime());
	}
	
	@Override
	public String marshal(Timestamp v) throws Exception {
		if (v == null)
			return null;
		
		Date d = new Date(v.getTime());
		return df.format(d);
	}
	
}

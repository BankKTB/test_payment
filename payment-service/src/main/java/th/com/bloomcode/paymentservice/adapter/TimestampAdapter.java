package th.com.bloomcode.paymentservice.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampAdapter extends XmlAdapter<String, Timestamp> {
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
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

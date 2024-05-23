package th.com.bloomcode.paymentservice.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Timestamp> {
    public static final String FORMAT_DATETIME_DISPLAY = "yyyy-MM-dd";
    private SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATETIME_DISPLAY);

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

        LocalDateTime date = new Date(v.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        DateTimeFormatter patternFormatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME_DISPLAY);

        return patternFormatter.format(date);
    }

}
package th.com.bloomcode.paymentservice.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimestampUtil {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat yyyymmdd = new SimpleDateFormat("YYYYMMdd");
    private static SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Timestamp stringToTimestamp(String date) {
        if (null == date || "".equalsIgnoreCase(date))
            return null;

        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return new Timestamp(d.getTime());
    }

    public static Timestamp stringToTimestampYYYYMMdd(String date) {
        if (null == date || "".equalsIgnoreCase(date))
            return null;

        Date d = null;
        try {
            d = yyyymmdd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return new Timestamp(d.getTime());
    }

    public static String timestampToString(Timestamp date) {
        if (date == null)
            return null;

        Date d = new Date(date.getTime());
        return df.format(d);
    }

    public static String timestampWithTimeToString(Timestamp date) {
        if (date == null)
            return null;

        Date d = new Date(date.getTime());
        return dft.format(d);
    }

    public static String dateThai(String strDate) {
        String Months[] = {
                "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน",
                "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม",
                "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        int year = 0, month = 0, day = 0;
        try {
            Date date = df.parse(strDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DATE);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return String.format("%s %s %s", day, Months[month], year + 543);
    }

    public static String firstDayOfWeek(String date, String type) {
        if (null == date || "".equalsIgnoreCase(date))
            return null;
        Date d = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = df.parse(date);
            cal.setTime(d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if ("yyyyMMdd".equals(type)) {
            return df.format(cal.getTime());
        } else {
            return yyyymmdd.format(cal.getTime());
        }
    }

    public static String lastDayOfWeek(String date, String type) {
        if (null == date || "".equalsIgnoreCase(date))
            return null;
        Date d = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = df.parse(date);
            cal.setTime(d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if ("yyyyMMdd".equals(type)) {
            return df.format(cal.getTime());
        } else {
            return yyyymmdd.format(cal.getTime());
        }
    }

    public static String firstDayOfMonth(String date, String type) {
        if (null == date || "".equalsIgnoreCase(date))
            return null;
        Date d = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = df.parse(date);
            cal.setTime(d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            cal.set(Calendar.DAY_OF_MONTH, cal.getFirstDayOfWeek());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if ("yyyyMMdd".equals(type)) {
            return df.format(cal.getTime());
        } else {
            return yyyymmdd.format(cal.getTime());
        }

    }

    public static String lastDayOfMonth(String date, String type) {
        if (null == date || "".equalsIgnoreCase(date))
            return null;
        Date d = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = df.parse(date);
            cal.setTime(d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            cal.set(Calendar.DAY_OF_MONTH, cal.getFirstDayOfWeek());
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if ("yyyyMMdd".equals(type)) {
            return df.format(cal.getTime());
        } else {
            return yyyymmdd.format(cal.getTime());
        }
    }

    public static String toDay(String date, String type) {
        if (null == date || "".equalsIgnoreCase(date))
            return null;
        Date d = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = df.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if ("yyyyMMdd".equals(type)) {
            return df.format(cal.getTime());
        } else {
            return yyyymmdd.format(cal.getTime());
        }
    }
}

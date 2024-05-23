package th.com.bloomcode.paymentservice.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class Util {

  private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

  private static final SimpleDateFormat dfForIndependent = new SimpleDateFormat("yyyyMMdd");

  private static final SimpleDateFormat dfForNewIndependent = new SimpleDateFormat("ddMMyyyy");

  private static final SimpleDateFormat dfPattern_ddMMyyyy = new SimpleDateFormat("ddMMyyyy");

  private static final SimpleDateFormat dfPatternReturnLog_ddMMyyyy =
      new SimpleDateFormat("dd.MM.yyyy");

  private static final SimpleDateFormat dfPatternWithScore_yyyyMMdd =
      new SimpleDateFormat("yyyy-MM-dd");

  private static final SimpleDateFormat dfGenerateSwiftFileStructure =
      new SimpleDateFormat("yyMMdd");

  private static final SimpleDateFormat dfGenerateGIROFileStructure =
      new SimpleDateFormat("ddMMyy");

  private static final SimpleDateFormat dfPattern_yyMMdd = new SimpleDateFormat("yyMMdd");

  private static final SimpleDateFormat dfPattern_ddMMyy = new SimpleDateFormat("ddMMyy");

  private static final SimpleDateFormat dfPattern_ddMMyyyyWithScore =
      new SimpleDateFormat("dd-MM-yyyy");

  private static final SimpleDateFormat dfReturn = new SimpleDateFormat("ddMMyyyy");

  public static boolean isEmpty(String str) {
    return isEmpty(str, false);
  }

  public static boolean isEmpty(Timestamp date) {
    return null == date;
  }

  public static boolean isEmpty(Long value) {
    return null == value;
  }

  public static boolean isEmpty(String str, boolean trimWhitespaces) {
    if (null == str) return true;
    if (trimWhitespaces) return str.trim().length() == 0;
    else return str.length() == 0;
  }

  public static boolean isEmpty(List<?> list) {
    return null == list;
  }

  public static Timestamp stringToTimestamp(String date) {
    if (null == date || "".equalsIgnoreCase(date)) return null;
    Date d = null;
    try {
      d = df.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
    return new Timestamp(d.getTime());
  }

  public static Timestamp stringSmartToTimestamp(String date) {
    if (null == date || "".equalsIgnoreCase(date)) return null;
    Date d = null;
    try {
      d = dfPattern_ddMMyyyy.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
    return new Timestamp(d.getTime());
  }

  public static String timestampToString(Timestamp date) {
    if (date == null) return null;

    Date d = new Date(date.getTime());
    return df.format(d);
  }

  public static String timestampToStringReturn(Timestamp date) {
    if (date == null) return null;

    Date d = new Date(date.getTime());
    return dfReturn.format(d);
  }

  public static Date stringToDate(String date) {
    if (null == date || "".equalsIgnoreCase(date)) return null;
    Date d = null;
    try {
      d = df.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
    return d;
  }

  public static BigDecimal getBigDecimal(Object value) {
    BigDecimal ret = BigDecimal.ZERO;
    if (null != value) {
      if (value instanceof BigDecimal) {
        ret = (BigDecimal) value;
      } else if (value instanceof String) {
        ret = new BigDecimal((String) value);
      } else if (value instanceof BigInteger) {
        ret = new BigDecimal((BigInteger) value);
      } else if (value instanceof Number) {
        ret = BigDecimal.valueOf(((Number) value).doubleValue());
      } else {
        throw new ClassCastException(
            "Not possible to coerce ["
                + value
                + "] from class "
                + value.getClass()
                + " into a BigDecimal.");
      }
    }
    return ret;
  }

  public static Timestamp stringToTimestampForIndependent(String date) {
    if (null == date || "".equalsIgnoreCase(date)) return null;
    Date d = null;
    try {
      int year = Integer.parseInt(date.substring(4)) - 543;
      String monthDay = date.substring(0, 4);
      String newDateString = monthDay + year;
      d = dfForNewIndependent.parse(newDateString);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
    return new Timestamp(d.getTime());
  }

  public static String timestampToStringForIndependent(Timestamp date) {
    if (date == null) return null;

    Date d = new Date(date.getTime());
    return dfForIndependent.format(d);
  }

  public static Date stringToDateForIndependent(String date) {
    if (null == date || "".equalsIgnoreCase(date)) return null;
    Date d = null;
    try {
      d = dfForIndependent.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
    return d;
  }

  public static String dateToStringPattern_ddMMyyyy(Date date) {
    if (null == date) return null;

    return dfPattern_ddMMyyyy.format(date);
  }

  public static String timestampToStringPattern_ddMMyyyy(Timestamp date) {
    if (null == date) return null;

    Date d = new Date(date.getTime());
    return dfPattern_ddMMyyyy.format(d);
  }

  public static String dateToStringPatternWithScore_yyyyMMdd(Date date) {
    if (null == date) return null;

    return dfPatternWithScore_yyyyMMdd.format(date);
  }

  public static String timestampToStringPatternWithScore_yyyyMMdd(Timestamp date) {
    if (null == date) return null;

    Date d = new Date(date.getTime());
    return dfPatternWithScore_yyyyMMdd.format(d);
  }

  public static String dateToStringGenerateSwiftFileStructure(Date date) {
    if (null == date) return null;

    return dfGenerateSwiftFileStructure.format(date);
  }

  public static String dateToStringGenerateGIROFileStructure(Date date) {
    if (null == date) return null;

    return dfGenerateGIROFileStructure.format(date);
  }

  public static String dateToStringReturnLog(Date date) {
    if (null == date) return null;

    return dfPatternReturnLog_ddMMyyyy.format(date);
  }

  public static String dateToStringPattern_yyMMdd(Date date) {
    if (null == date) return null;

    return dfPattern_yyMMdd.format(date);
  }

  public static String timestampToStringPattern_yyMMdd(Timestamp date) {
    if (null == date) return null;

    Date d = new Date(date.getTime());
    return dfPattern_yyMMdd.format(d);
  }

  public static String dateToStringPattern_ddMMyy(Date date) {
    if (null == date) return null;

    return dfPattern_ddMMyy.format(date);
  }

  public static String timestampToStringPattern_ddMMyy(Timestamp date) {
    if (null == date) return null;

    Date d = new Date(date.getTime());
    return dfPattern_ddMMyy.format(d);
  }

  public static String dateToStringPattern_ddMMyyyyWithScore(Date date) {
    if (null == date) return null;

    return dfPattern_ddMMyyyyWithScore.format(date);
  }

  public static Timestamp addDays(Timestamp date, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, days);
    return new Timestamp(cal.getTime().getTime());
  }

  public static String starToPerCent(String value) {
    if (value.contains("*")) {
      value = value.replace("*", "%");
    }
    return value;
  }

  public static String removeStarOrPercent(String value) {
    value = value.replace("*", "").replace("%", "");
    return value;
  }

  public static String replaceNull(String value) {
    return !isEmpty(value) ? value : "";
  }
}

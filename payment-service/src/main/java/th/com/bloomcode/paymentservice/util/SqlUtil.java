package th.com.bloomcode.paymentservice.util;

import org.springframework.jdbc.core.JdbcTemplate;
import th.com.bloomcode.paymentservice.model.common.BaseDateRange;
import th.com.bloomcode.paymentservice.model.common.BaseRange;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SqlUtil {

    public static String whereClauseRange(Timestamp from, Timestamp to, String column, Map<String, Object> params) {
        if (null != from && null != to) {
            params.put(column + "From", from);
            params.put(column + "To", Util.addDays(to, 1));
            return " AND (" + column + " BETWEEN TRUNC(:" + column + "From,'dd') AND TRUNC(:" + column + "To,'dd'))";
        } else if (null != from) {
            params.put(column + "From", from);
            params.put(column + "To", Util.addDays(from, 1));
            return " AND (" + column + " BETWEEN TRUNC(:" + column + "From,'dd') AND TRUNC(:" + column + "To,'dd'))";
        }

        return "";
    }

    public static String whereClauseRange(Timestamp from, Timestamp to, String column, List<Object> params) {
        if (null != from && null != to) {
            params.add(from);
            params.add(Util.addDays(to, 1));
            return " AND (" + column + " BETWEEN TRUNC(?) AND TRUNC(?))";
        } else if (null != from) {
            params.add(from);
            params.add(Util.addDays(from, 1));
            return " AND (" + column + " BETWEEN TRUNC(?) AND TRUNC(?))";
        }
        return "";
    }

    public static String whereClauseRangeNotAddDay(Timestamp from, Timestamp to, String column, List<Object> params) {
        if (null != from && null != to) {
            params.add(from);
            params.add(to);
            return " AND (" + column + " BETWEEN TRUNC(?) AND TRUNC(?))";
        } else if (null != from) {
            params.add(from);
            return " AND (" + column + "=TRUNC(?))";
        }
        return "";
    }

    public static String whereClause(Timestamp to, String column, Map<String, Object> params) {
        if (null != to) {
            params.put(column + "to", to);
            return " AND (" + column + "<=TRUNC(:" + column + "to,'dd'))";
        }

        return "";
    }

    public static String whereClause(Timestamp to, String column, List<Object> params) {
        if (null != to) {
            params.add(to);
            return " AND (" + column + "<=TRUNC(?,'dd'))";
        }

        return "";
    }

    public static String whereClauseLess(Timestamp to, String column, List<Object> params) {
        if (null != to) {
            params.add(to);
            return " AND (" + column + "<TRUNC(?,'dd'))";
        }

        return "";
    }

    public static String whereClauseEqual(Timestamp to, String column, Map<String, Object> params) {
        if (null != to) {
            params.put(column + "to", to);
            return " AND (" + column + "=TRUNC(:" + column + "to,'dd'))";
        }

        return "";
    }

    public static String whereClauseEqual(Timestamp to, String column, List<Object> params) {
        if (null != to) {
            params.add(to);
            return " AND (" + column + "=TRUNC(?))";
        }
        return "";
    }

    public static String whereClauseEqual(Date to, String column, List<Object> params) {
        if (null != to) {
            params.add(to);
            return " AND (" + column + "=TRUNC(?))";
        }
        return "";
    }

    public static String whereClauseRange(String from, String to, String column, Map<String, Object> params) {
        if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
            params.put(column + "From", from.trim());
            params.put(column + "To", to.trim());
            return " AND (UPPER(" + column + ") BETWEEN UPPER(:" + column + "From) AND UPPER(:" + column + "To))";
        } else if (!Util.isEmpty(from)) {
            params.put(column + "From", from.trim());
            return " AND (UPPER(" + column + ") LIKE UPPER(:" + column + "From))";
        }

        return "";
    }

    public static String whereClauseRange(String from, String to, String column, List<Object> params) {
        if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
            params.add(from.trim());
            params.add(to.trim());
            return " AND (UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
        } else if (!Util.isEmpty(from)) {
            params.add(from.trim());
            return " AND (UPPER(" + column + ") LIKE UPPER(?))";
        }
        return "";
    }


    public static String whereClauseRangeOr(String from, String to, String column, Map<String, Object> params,
                                            int index, int sequence) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.put(column + "From" + index, from.trim());
                        params.put(column + "To" + index, to.trim());
                        return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:"
                            + column + "To" + index + "))";
                    } else if (!Util.isEmpty(from)) {
                        params.put(column + "From" + index, from.trim());
                        return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
                    }
                } else {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.put(column + "From" + index, from.trim());
                        params.put(column + "To" + index, to.trim());
                        return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:"
                            + column + "To" + index + "))";
                    } else if (!Util.isEmpty(from)) {
                        params.put(column + "From" + index, from.trim());
                        return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
                    }
                }
            } else {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.put(column + "From" + index, from.trim());
                        params.put(column + "To" + index, to.trim());
                        return " AND ((UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:"
                            + column + "To" + index + "))";
                    } else if (!Util.isEmpty(from)) {
                        params.put(column + "From" + index, from.trim());
                        return " AND ((UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
                    }
                } else {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.put(column + "From" + index, from);
                        params.put(column + "To" + index, to);
                        return " AND (UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:"
                            + column + "To" + index + "))";
                    } else if (!Util.isEmpty(from)) {
                        params.put(column + "From" + index, from);
                        return " AND (UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
                    }
                }
            }

        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.put(column + "From" + index, from.trim());
                    params.put(column + "To" + index, to.trim());
                    return " OR (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                        + "To" + index + ")))";
                } else if (!Util.isEmpty(from)) {
                    params.put(column + "From" + index, from.trim());
                    return " OR (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.put(column + "From" + index, from.trim());
                    params.put(column + "To" + index, to.trim());
                    return " OR (UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                        + "To" + index + "))";
                } else if (!Util.isEmpty(from)) {
                    params.put(column + "From" + index, from.trim());
                    return " OR (UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
                }
            }

        }

        return "";
    }

    public static String whereClauseRangeOr(String from, String to, String column, List<Object> params,
                                            int index, int sequence) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from);
                        return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
                    }
                } else {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from);
                        return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
                    }
                }
            } else {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return " AND ((UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return " AND ((UPPER(" + column + ") LIKE UPPER(?))";
                    }
                } else {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return " AND (UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return " AND (UPPER(" + column + ") LIKE UPPER(?))";
                    }
                }
            }

        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?)))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from);
                    return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return " AND (UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from);
                    return " AND (UPPER(" + column + ") LIKE UPPER(?))";
                }
            }

        }

        return "";
    }

    public static String whereClauseNotRangeOr(String from, String to, String column, Map<String, Object> params,
                                               int index, int sequence) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.put(column + "From" + index, from.trim());
                    params.put(column + "To" + index, to.trim());
                    return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:"
                        + column + "To" + index + "))";
                } else if (!Util.isEmpty(from)) {
                    params.put(column + "From" + index, from.trim());
                    return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT LIKE UPPER(:" + column + "From" + index + "))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.put(column + "From" + index, from.trim());
                    params.put(column + "To" + index, to.trim());
                    return " AND ((UPPER(" + column + ") NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:"
                        + column + "To" + index + "))";
                } else if (!Util.isEmpty(from)) {
                    params.put(column + "From" + index, from.trim());
                    return " AND ((UPPER(" + column + ") NOT LIKE UPPER(:" + column + "From" + index + "))";
                }
            }
        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.put(column + "From" + index, from.trim());
                    params.put(column + "To" + index, to.trim());
                    return " OR (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                        + "To" + index + "))";
                } else if (!Util.isEmpty(from)) {
                    params.put(column + "From" + index, from.trim());
                    return " OR (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT LIKE UPPER(:" + column + "From" + index + "))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.put(column + "From" + index, from.trim());
                    params.put(column + "To" + index, to.trim());
                    return " OR (UPPER(" + column + ")  NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                        + "To" + index + "))";
                } else if (!Util.isEmpty(from)) {
                    params.put(column + "From" + index, from.trim());
                    return " OR (UPPER(" + column + ")  NOT LIKE UPPER(:" + column + "From" + index + "))";
                }
            }
        }

        return "";
    }

    public static String whereClauseNotRangeOr(String from, String to, String column, List<Object> params,
                                               int index, int sequence) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from);
                    return " AND ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT LIKE UPPER(?))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return " AND ((UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from);
                    return " AND ((UPPER(" + column + ") NOT LIKE UPPER(?))";
                }
            }
        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT LIKE UPPER(?))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return " AND (UPPER(" + column + ")  NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return " AND (UPPER(" + column + ")  NOT LIKE UPPER(?))";
                }
            }
        }

        return "";
    }

    public static String whereClauseRangeOne(String from, String to, String column, Map<String, Object> params,
                                             int index) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.put(column + "From" + index, from.trim());
                params.put(column + "To" + index, to.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                    + "To" + index + "))";
            } else if (!Util.isEmpty(from)) {
                params.put(column + "From" + index, from.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.put(column + "From" + index, from.trim());
                params.put(column + "To" + index, to.trim());
                return " AND (UPPER(" + column + ") BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                    + "To" + index + "))";
            } else if (!Util.isEmpty(from)) {
                params.put(column + "From" + index, from.trim());
                return " AND (UPPER(" + column + ") LIKE UPPER(:" + column + "From" + index + "))";
            }
        }

        return "";
    }

    public static String whereClauseRangeOne(String from, String to, String column, List<Object> params,
                                             int index) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (UPPER(" + column + ") LIKE UPPER(?))";
            }
        }

        return "";
    }

    public static String whereClauseRangeOneTuning(String from, String to, String column, List<Object> params,
                                                   int index) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (" + column + " IS NULL OR " + column + " BETWEEN ? AND ?)";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (" + column + " IS NULL OR " + column + " = ?)";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (" + column + " BETWEEN ? AND ?)";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (" + column + " = ?)";
            }
        }

        return "";
    }

    public static String whereClauseNotRangeOne(String from, String to, String column, Map<String, Object> params,
                                                int index) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.put(column + "From" + index, from.trim());
                params.put(column + "To" + index, to.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                    + "To" + index + "))";
            } else if (!Util.isEmpty(from)) {
                params.put(column + "From" + index, from.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT  LIKE UPPER(:" + column + "From" + index + "))";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.put(column + "From" + index, from.trim());
                params.put(column + "To" + index, to.trim());
                return " AND (UPPER(" + column + ") NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
                    + "To" + index + "))";
            } else if (!Util.isEmpty(from)) {
                params.put(column + "From" + index, from.trim());
                return " AND (UPPER(" + column + ") NOT  LIKE UPPER(:" + column + "From" + index + "))";
            }
        }

        return "";
    }

    public static String whereClauseNotRangeOne(String from, String to, String column, List<Object> params,
                                                int index) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT  LIKE UPPER(?))";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (UPPER(" + column + ") NOT  LIKE UPPER(?))";
            }
        }

        return "";
    }

    public static String whereClauseNotRangeOneTuning(String from, String to, String column, List<Object> params,
                                                      int index) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (" + column + " IS NULL OR " + column + " NOT BETWEEN ? AND ?)";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (" + column + " IS NULL OR " + column + " != ?)";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return " AND (" + column + " NOT BETWEEN ? AND ?)";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return " AND (" + column + " != ?)";
            }
        }

        return "";
    }
//
//    public static String whereClauseNotRangeOne(String from, String to, String column, List<Object> params,
//                                                int index) {
//        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
//            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
//                params.add(column + "From" + index);
//                params.add(column + "To" + index);
//                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
//                        + "To" + index + "))";
//            } else if (!Util.isEmpty(from)) {
//                params.add(column + "From" + index);
//                return " AND (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT  LIKE UPPER(:" + column + "From" + index + "))";
//            }
//        } else {
//            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
//                params.add(column + "From" + index);
//                params.add(column + "To" + index);
//                return " AND (UPPER(" + column + ") NOT BETWEEN UPPER(:" + column + "From" + index + ") AND UPPER(:" + column
//                        + "To" + index + "))";
//            } else if (!Util.isEmpty(from)) {
//                params.add(column + "From" + index);
//                return " AND (UPPER(" + column + ") NOT  LIKE UPPER(:" + column + "From" + index + "))";
//            }
//        }
//        return "";
//    }

    public static String whereClause(String value, String column, Map<String, Object> params) {
        if (!Util.isEmpty(value)) {
            params.put(column, value.trim());
            return " AND (UPPER(" + column + ") LIKE UPPER(:" + column + "))";
        }

        return "";
    }

    public static String whereClause(String value, String column, List<Object> params) {
        if (!Util.isEmpty(value)) {
            params.add(value.trim());
            return " AND (UPPER(" + column + ") LIKE UPPER(?))";
        }

        return "";
    }

    public static String whereClauseEqual(String value, String column, List<Object> params) {
        if (!Util.isEmpty(value)) {
            params.add(value.trim());
            return " AND " + column + " = ?";
        }

        return "";
    }

    public static String whereClause(Long value, String column, List<Object> params) {
        if (0 != value) {
            params.add(value);
            return " AND (" + column + " = ?)";
        }

        return "";
    }

    public static String whereClauseNot(Long value, String column, List<Object> params) {
        if (0 != value) {
            params.add(value);
            return " AND (" + column + " != ?)";
        }

        return "";
    }

    public static String whereClauseNot(String value, String column, Map<String, Object> params) {
        if (!Util.isEmpty(value)) {
            if (value.contains("*")) {
                value = value.replace("*", "%");
            }
            params.put(column, value.trim());
            return " AND (UPPER(" + column + ") NOT LIKE UPPER(:" + column + "))";
        }

        return "";
    }

    public static String whereClauseNot(String value, String column, List<Object> params) {
        if (!Util.isEmpty(value)) {
            if (value.contains("*")) {
                value = value.replace("*", "%");
            }
            params.add(value.trim());
            return " AND (UPPER(" + column + ") NOT LIKE UPPER(?))";
        }

        return "";
    }

    public static String whereClauseNotEqual(Long value, String column, List<Object> params) {
        if (!Util.isEmpty(value)) {
            params.add(value);
            return " AND (" + column + " <> ?)";
        }

        return "";
    }


    public static String whereClauseOr(String value, String column, Map<String, Object> params) {
        if (!Util.isEmpty(value)) {
            if (value.contains("*")) {
                value = value.replace("*", "%");
            }
            params.put(column, value.trim());
            return " OR (UPPER(" + column + ") LIKE UPPER(:" + column + "))";
        }

        return "";
    }

    public static String whereClauseOr(String value, Map<String, Object> params, String... columns) {
        if (!Util.isEmpty(value)) {
            if (value.contains("*")) {
                value = value.replace("*", "%");
            }
            StringBuilder sql = new StringBuilder(" AND (");
            for (int i = 0; i < columns.length; i++) {
                if (i != 0) {
                    sql.append(" OR ");
                }
                params.put(columns[i], value.trim());
                sql.append(" (UPPER(" + columns[i] + ") LIKE UPPER(:" + columns[i] + "))");
            }
            sql.append(") ");
            return sql.toString();
        }
        return "";
    }

    public static String whereClauseOr(String value, List<Object> params, String... columns) {
        if (!Util.isEmpty(value)) {
            if (value.contains("*")) {
                value = value.replace("*", "%");
            }
            StringBuilder sql = new StringBuilder(" AND (");
            for (int i = 0; i < columns.length; i++) {
                if (i != 0) {
                    sql.append(" OR ");
                }
                params.add(value.trim());
                sql.append(" (UPPER(" + columns[i] + ") LIKE UPPER(?))");
            }
            sql.append(") ");
            return sql.toString();
        }
        return "";
    }

    public static String generateProposalDocument(Long seq) {
        return "PAYS" + String.format("%06d", seq);
    }

    public static String generatePaymentDocument(Long seq) {
        return "4100" + String.format("%06d", seq);
    }

    public static String whereClauseNotRangeOneTime(Timestamp from, Timestamp to, String column, Map<String, Object> params,
                                                    int index) {

        if (null != from && null != to) {
            params.put(column + "From", from);
            params.put(column + "To", to);
            return " AND (" + column + " NOT BETWEEN TRUNC(:" + column + "From,'dd') AND TRUNC(:" + column + "To,'dd'))";
        } else if (null != from) {
            params.put(column + "From", from);
            return " AND (" + column + "!=TRUNC(:" + column + "From,'dd'))";
        }

        return "";
    }

    public static String whereClauseNotRangeOneTime(Timestamp from, Timestamp to, String column, List<Object> params,
                                                    int index) {
        if (null != from && null != to) {
            params.add(from);
            params.add(to);
            return " AND (" + column + " NOT BETWEEN TRUNC(?) AND TRUNC(?))";
        } else if (null != from) {
            params.add(from);
            return " AND (" + column + "!=TRUNC(?))";
        }
        return "";
    }

    public static String whereClauseRangeOneTime(Timestamp from, Timestamp to, String column, Map<String, Object> params,
                                                 int index) {

        if (null != from && null != to) {
            params.put(column + "From", from);
            params.put(column + "To", to);
            return " AND (" + column + " BETWEEN TRUNC(:" + column + "From,'dd') AND TRUNC(:" + column + "To,'dd'))";
        } else if (null != from) {
            params.put(column + "From", from);
            return " AND (" + column + "=TRUNC(:" + column + "From,'dd'))";
        }

        return "";
    }

    public static String whereClauseRangeOneTime(Timestamp from, Timestamp to, String column, List<Object> params,
                                                 int index) {
        if (null != from && null != to) {
            params.add(from);
            params.add(to);
            return " AND (" + column + " BETWEEN TRUNC(?,'dd') AND TRUNC(?,'dd'))";
        } else if (null != from) {
            params.add(from);
            return " AND (" + column + "=TRUNC(?,'dd'))";
        }

        return "";
    }

    public static String whereClauseNotRangeTimeOr(Timestamp from, Timestamp to, String column, Map<String, Object> params,
                                                   int index, int sequence) {
        if (index == 1) {
            if (null != from && null != to) {
                params.put(column + "From" + index, from);
                params.put(column + "To" + index, to);
                return " AND (" + column + " NOT BETWEEN TRUNC(:" + column + "From" + index + ",'dd') AND TRUNC(:" + column + "To,'dd'))";
            } else if (null != from) {
                params.put(column + "From" + index, from);
                return " AND (" + column + "!=TRUNC(:" + column + "From" + index + ",'dd'))";
            }
        } else {
            if (null != from && null != to) {
                params.put(column + "From" + index, from);
                params.put(column + "To" + index, to);
                return " OR (" + column + " NOT BETWEEN TRUNC(:" + column + "From" + index + ",'dd') AND TRUNC(:" + column + "To,'dd'))";
            } else if (null != from) {
                params.put(column + "From" + index, from);
                return " OR (" + column + "!=TRUNC(:" + column + "From" + index + ",'dd'))";
            }
        }

        return "";
    }

    public static String whereClauseNotRangeTimeOr(Timestamp from, Timestamp to, String column, List<Object> params,
                                                   int index, int sequence) {
        if (index == 1) {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return " AND (" + column + " NOT BETWEEN TRUNC(?) AND TRUNC(?))";
            } else if (null != from) {
                params.add(from);
                return " AND (" + column + "!=TRUNC(?))";
            }
        } else {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return " OR (" + column + " NOT BETWEEN TRUNC(:" + column + "From" + index + ",'dd') AND TRUNC(:" + column + "To,'dd'))";
            } else if (null != from) {
                params.add(from);
                return " OR (" + column + "!=TRUNC(:" + column + "From" + index + ",'dd'))";
            }
        }

        return "";
    }

    public static String whereClauseRangeTimeOr(Timestamp from, Timestamp to, String column, Map<String, Object> params,
                                                int index, int sequence) {
        if (index == 1) {
            if (null != from && null != to) {
                params.put(column + "From" + index, from);
                params.put(column + "To" + index, to);
                return " AND ((" + column + " BETWEEN TRUNC(:" + column + "From" + index + ",'dd') AND TRUNC(:" + column + "To,'dd'))";
            } else if (null != from) {
                params.put(column + "From" + index, from);
                return " AND ((" + column + "=TRUNC(:" + column + "From" + index + ",'dd'))";
            }
        } else {
            if (null != from && null != to) {
                params.put(column + "From" + index, from);
                params.put(column + "To" + index, to);
                return " OR (" + column + " BETWEEN TRUNC(:" + column + "From" + index + ",'dd') AND TRUNC(:" + column + "To,'dd'))";
            } else if (null != from) {
                params.put(column + "From" + index, from);
                return " OR (" + column + "=TRUNC(:" + column + "From" + index + ",'dd'))";
            }
        }

        return "";
    }

    public static String whereClauseRangeTimeOr(Timestamp from, Timestamp to, String column, List<Object> params,
                                                int index, int sequence) {
        if (index == 1) {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return " AND ((" + column + " BETWEEN TRUNC(?,'dd') AND TRUNC(?,'dd'))";
            } else if (null != from) {
                params.add(from);
                return " AND ((" + column + "=TRUNC(?,'dd'))";
            }
        } else {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return " OR (" + column + " BETWEEN TRUNC(?,'dd') AND TRUNC(?,'dd'))";
            } else if (null != from) {
                params.add(from);
                return " OR (" + column + "=TRUNC(?,'dd'))";
            }
        }
        return "";
    }

    public static StringBuilder dynamicCondition(List<BaseRange> baseRange, String columnName, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (null != baseRange && baseRange.size() > 0) {
            if (baseRange.size() == 1) {
                for (BaseRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOne(column.getFrom(),
                                column.getTo(), columnName, params, ++index));

                        } else {
                            sb.append(SqlUtil.whereClauseRangeOne(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        }

                    }
                }
            } else {
                int sequence = 0;
                int lastSequence = baseRange.size();
                for (BaseRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOr(column.getFrom(),
                                column.getTo(), columnName, params, ++index, sequence));
                        } else {
                            sb.append(SqlUtil.whereClauseRangeOr(column.getFrom(),
                                column.getTo(), columnName, params, ++index, sequence));
                        }
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
        return sb;
    }

    public static StringBuilder dynamicCondition(List<BaseRange> baseRange, String columnName, List<Object> params) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (null != baseRange && baseRange.size() > 0) {
            if (baseRange.size() == 1) {
                for (BaseRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOne(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        } else {
                            sb.append(SqlUtil.whereClauseRangeOne(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        }
                    }
                }
            } else {
                int sequence = 0;
                int lastSequence = baseRange.size();
                for (BaseRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOr(column.getFrom(),
                                column.getTo(), columnName, params, ++index, sequence));
                        } else {
                            sb.append(SqlUtil.whereClauseRangeOr(column.getFrom(),
                                column.getTo(), columnName, params, ++index, sequence));
                        }
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
        return sb;
    }

    public static StringBuilder dynamicDateCondition(List<BaseDateRange> baseRange, String columnName, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (null != baseRange && baseRange.size() > 0) {
            if (baseRange.size() == 1) {
                for (BaseDateRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOneTime(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        } else {
                            sb.append(SqlUtil.whereClauseRangeOneTime(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        }

                    }
                }
            } else {
                int sequence = 0;
                int lastSequence = baseRange.size();
                for (BaseDateRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOneTime(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        } else {
                            sb.append(SqlUtil.whereClauseRangeTimeOr(column.getFrom(),
                                column.getTo(), columnName, params, ++index, sequence));
                        }

                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
        return sb;
    }

    public static StringBuilder dynamicDateCondition(List<BaseDateRange> baseRange, String columnName, List<Object> params) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (null != baseRange && baseRange.size() > 0) {
            if (baseRange.size() == 1) {
                for (BaseDateRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOneTime(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        } else {
                            sb.append(SqlUtil.whereClauseRangeOneTime(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        }

                    }
                }
            } else {
                int sequence = 0;
                int lastSequence = baseRange.size();
                for (BaseDateRange column : baseRange) {
                    if (!Util.isEmpty(column.getFrom())) {
                        if (column.isOptionExclude()) {
                            sb.append(SqlUtil.whereClauseNotRangeOneTime(column.getFrom(),
                                column.getTo(), columnName, params, ++index));
                        } else {
                            sb.append(SqlUtil.whereClauseRangeTimeOr(column.getFrom(),
                                column.getTo(), columnName, params, ++index, sequence));
                        }
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
        return sb;
    }

    public static String whereClauseParentRangeOne(String compCode, String accDocNo, String fiscalYear,
                                                   String columnCompCode, String columnAccDocNo, String columnFiscalYear, List<Object> params,
                                                   int index) {
        if (!Util.isEmpty(compCode) && !Util.isEmpty(accDocNo) && !Util.isEmpty(fiscalYear)) {
//            params.add(columnCompCode + index, compCode);
//            params.add(columnAccDocNo + index, accDocNo);
//            params.add(columnFiscalYear + index, fiscalYear);

            params.add(compCode.trim());
            params.add(accDocNo.trim());
            params.add(fiscalYear.trim());

            return " AND (" + columnCompCode + " = ?" + " AND " + columnAccDocNo + " = ?" + " AND " + columnFiscalYear + " = ?" + ")";
        }
        return "";
    }

    public static String whereClauseChildRangeOne(String compCode, String accDocNo, String fiscalYear,
                                                  String columnCompCode, String columnAccDocNo, String columnFiscalYear, List<Object> params,
                                                  int index) {
        if (!Util.isEmpty(compCode) && !Util.isEmpty(accDocNo) && !Util.isEmpty(fiscalYear)) {
//            params.add(columnCompCode + index, compCode);
//            params.add(columnAccDocNo + index, accDocNo);
//            params.add(columnFiscalYear + index, fiscalYear);

            params.add(compCode.trim());
            params.add(accDocNo.trim());
            params.add(fiscalYear.trim());

            return " AND (" + columnCompCode + " = ?" + " AND " + columnAccDocNo + " = ?" + " AND " + columnFiscalYear + " = ?" + ")";
        }
        return "";
    }

    public static String whereClauseParentRangeOr(String compCode, String accDocNo, String fiscalYear,
                                                  String columnCompCode, String columnAccDocNo, String columnFiscalYear, List<Object> params,
                                                  int index, int sequence) {
        if (index == 1) {
            if (!Util.isEmpty(compCode) && !Util.isEmpty(accDocNo) && !Util.isEmpty(fiscalYear)) {
//                params.put(columnCompCode + index, compCode);
//                params.put(columnAccDocNo + index, accDocNo);
//                params.put(columnFiscalYear + index, fiscalYear);

                params.add(compCode.trim());
                params.add(accDocNo.trim());
                params.add(fiscalYear.trim());
                return " AND ((" + columnCompCode + " = ?" + " AND " + columnAccDocNo + " = ?" + " AND " + columnFiscalYear + " = ?" + ")";
            }
        } else {

            if (!Util.isEmpty(compCode) && !Util.isEmpty(accDocNo) && !Util.isEmpty(fiscalYear)) {
//                params.put(columnCompCode + index, compCode);
//                params.put(columnAccDocNo + index, accDocNo);
//                params.put(columnFiscalYear + index, fiscalYear);
                params.add(compCode.trim());
                params.add(accDocNo.trim());
                params.add(fiscalYear.trim());
                return " OR (" + columnCompCode + " = ?" + " AND " + columnAccDocNo + " = ?" + " AND " + columnFiscalYear + " = ?" + ")";
            }
        }

        return "";
    }

    public static String whereClauseChildRangeOr(String compCode, String accDocNo, String fiscalYear,
                                                 String columnCompCode, String columnAccDocNo, String columnFiscalYear, List<Object> params,
                                                 int index, int sequence) {
        if (index == 1) {
            if (!Util.isEmpty(compCode) && !Util.isEmpty(accDocNo) && !Util.isEmpty(fiscalYear)) {
//                params.put(columnCompCode + index, compCode);
//                params.put(columnAccDocNo + index, accDocNo);
//                params.put(columnFiscalYear + index, fiscalYear);

                params.add(compCode.trim());
                params.add(accDocNo.trim());
                params.add(fiscalYear.trim());
                return " AND ((" + columnCompCode + " = ?" + " AND " + columnAccDocNo + " = ?" + " AND " + columnFiscalYear + " = ?" + ")";
            }
        } else {

            if (!Util.isEmpty(compCode) && !Util.isEmpty(accDocNo) && !Util.isEmpty(fiscalYear)) {
//                params.put(columnCompCode + index, compCode);
//                params.put(columnAccDocNo + index, accDocNo);
//                params.put(columnFiscalYear + index, fiscalYear);
                params.add(compCode.trim());
                params.add(accDocNo.trim());
                params.add(fiscalYear.trim());
                return " OR (" + columnCompCode + " = ?" + " AND " + columnAccDocNo + " = ?" + " AND " + columnFiscalYear + " = ?" + ")";
            }
        }

        return "";
    }

    public static String newWhereClauseNotRangeOr(String from, String to, String column, List<Object> params,
                                                  int index, int sequence, String checkOptionExclude) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + " ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT LIKE UPPER(?))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + "  ((UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  ((UPPER(" + column + ") NOT LIKE UPPER(?))";
                }
            }
        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + "  (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT LIKE UPPER(?))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + "  (UPPER(" + column + ")  NOT BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  (UPPER(" + column + ")  NOT LIKE UPPER(?))";
                }
            }
        }

        return "";
    }

    public static String newWhereClauseNotRangeOrTuning(String from, String to, String column, List<Object> params,
                                                        int index, int sequence, String checkOptionExclude) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + " ((" + column + " IS NULL OR " + column + " NOT BETWEEN ? AND ?)";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  ((" + column + " IS NULL OR " + column + " != ?)";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + "  ((" + column + " NOT BETWEEN ? AND ?)";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  ((" + column + " != ?)";
                }
            }
        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + "  (" + column + " IS NULL OR " + column + " NOT BETWEEN ? AND ?)";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  (" + column + " IS NULL OR " + column + " != ?)";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + "  (" + column + " NOT BETWEEN ? AND ?)";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + "  (" + column + " != ?)";
                }
            }
        }

        return "";
    }

    public static String newWhereClauseRangeOr(String from, String to, String column, List<Object> params,
                                               int index, int sequence, String checkOptionExclude) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " ((UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
                    }
                } else {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
                    }
                }
            } else {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " ((UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " ((UPPER(" + column + ") LIKE UPPER(?))";
                    }
                } else {
                    if (!Util.isEmpty(from.trim()) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " (UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " (UPPER(" + column + ") LIKE UPPER(?))";
                    }
                }
            }

        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?)))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + " (UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + " (UPPER(" + column + ") LIKE UPPER(?))";
                }
            }

        }

        return "";
    }

    public static String newWhereClauseRangeOrTuning(String from, String to, String column, List<Object> params,
                                                     int index, int sequence, String checkOptionExclude) {
        if (index == 1) {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " ((" + column + " IS NULL OR " + column + " BETWEEN ? AND ?)";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " ((" + column + " IS NULL OR " + column + " LIKE ?)";
                    }
                } else {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " (" + column + " IS NULL OR " + column + " BETWEEN ? AND ?)";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " (" + column + " IS NULL OR " + column + " = ?)";
                    }
                }
            } else {
                if (sequence == 0) {
                    if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " ((" + column + " BETWEEN ? AND ?)";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " ((" + column + " = ?)";
                    }
                } else {
                    if (!Util.isEmpty(from.trim()) && !Util.isEmpty(to)) {
                        params.add(from.trim());
                        params.add(to.trim());
                        return checkOptionExclude + " (" + column + " BETWEEN ? AND ?)";
                    } else if (!Util.isEmpty(from)) {
                        params.add(from.trim());
                        return checkOptionExclude + " (" + column + " = ?)";
                    }
                }
            }

        } else {
            if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + " (" + column + " IS NULL OR " + column + " BETWEEN ? AND ?))";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + " (" + column + " IS NULL OR " + column + " = ?)";
                }
            } else {
                if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                    params.add(from.trim());
                    params.add(to.trim());
                    return checkOptionExclude + " (" + column + " BETWEEN ? AND ?)";
                } else if (!Util.isEmpty(from)) {
                    params.add(from.trim());
                    return checkOptionExclude + " (" + column + " = ?)";
                }
            }

        }

        return "";
    }

    public static String newWhereClauseRangeOne(String from, String to, String column, List<Object> params,
                                                int index, String checkOptionExclude) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") LIKE UPPER(?))";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return checkOptionExclude + " (UPPER(" + column + ") BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return checkOptionExclude + " (UPPER(" + column + ") LIKE UPPER(?))";
            }
        }

        return "";
    }

    public static String newWhereClauseNotRangeOne(String from, String to, String column, List<Object> params,
                                                   int index, String checkOptionExclude) {
        if (column.equalsIgnoreCase("GL.PAYMENT_BLOCK")) {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return checkOptionExclude + " (UPPER(" + column + ") IS NULL OR UPPER(" + column + ") NOT  LIKE UPPER(?))";
            }
        } else {
            if (!Util.isEmpty(from) && !Util.isEmpty(to)) {
                params.add(from.trim());
                params.add(to.trim());
                return checkOptionExclude + " (UPPER(" + column + ") NOT BETWEEN UPPER(?) AND UPPER(?))";
            } else if (!Util.isEmpty(from)) {
                params.add(from.trim());
                return checkOptionExclude + " (UPPER(" + column + ") NOT  LIKE UPPER(?))";
            }
        }

        return "";
    }

    public static String newWhereClauseNotRangeTimeOr(Timestamp from, Timestamp to, String column, List<Object> params,
                                                      int index, int sequence, String checkOptionExclude) {
        if (index == 1) {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return checkOptionExclude + " ((" + column + " NOT BETWEEN TRUNC(?,'dd') AND TRUNC(?,'dd'))";
            } else if (null != from) {
                params.add(from);
                return checkOptionExclude + " ((" + column + "!=TRUNC(?,'dd'))";
            }
        } else {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return checkOptionExclude + " (" + column + " NOT BETWEEN TRUNC(?,'dd') AND TRUNC(?,'dd'))";
            } else if (null != from) {
                params.add(from);
                return checkOptionExclude + " (" + column + "!=TRUNC(?,'dd'))";
            }
        }

        return "";
    }

    public static String newWhereClauseRangeTimeOr(Timestamp from, Timestamp to, String column, List<Object> params,
                                                   int index, int sequence, String checkOptionExclude) {
        if (index == 1) {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return checkOptionExclude + " ((" + column + " BETWEEN TRUNC(?,'dd') AND TRUNC(?,'dd'))";
            } else if (null != from) {
                params.add(from);
                return checkOptionExclude + " ((" + column + "=TRUNC(?,'dd'))";
            }
        } else {
            if (null != from && null != to) {
                params.add(from);
                params.add(to);
                return checkOptionExclude + " (" + column + " BETWEEN TRUNC(?,'dd') AND TRUNC(?,'dd'))";
            } else if (null != from) {
                params.add(from);
                return checkOptionExclude + " (" + column + "=TRUNC(?,'dd'))";
            }
        }
        return "";
    }

    public static synchronized Long getNextSeries(JdbcTemplate jdbcTemplate, String seqName, Long lastNumber) {
        Long nextVal = jdbcTemplate.queryForObject("SELECT " + seqName + ".NEXTVAL FROM DUAL", Long.class);
        if (lastNumber != null) {
            jdbcTemplate.execute("ALTER SEQUENCE " + seqName + " RESTART START WITH " + (nextVal + lastNumber));
        }
        return nextVal;

    }

//    public static Long getNextSeries(JdbcTemplate jdbcTemplate, String seqName) {
//        String sql = "SELECT " + seqName + ".NEXTVAL FROM DUAL";
//        return jdbcTemplate.queryForObject(sql, Long.class);
//    }
//
//    public static void updateNextSeries(JdbcTemplate jdbcTemplate, Long lastNumber, String seqName) {
//        String sql = "";
//        sql = "ALTER SEQUENCE " + seqName + " RESTART START WITH " + lastNumber;
//        jdbcTemplate.execute(sql);
//    }
}

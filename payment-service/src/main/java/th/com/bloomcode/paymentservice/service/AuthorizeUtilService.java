package th.com.bloomcode.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.authorization.AuthorizationObject;
import th.com.bloomcode.paymentservice.authorization.IAuthorizationObject;
import th.com.bloomcode.paymentservice.model.idem.CAAuthorizationActivity;
import th.com.bloomcode.paymentservice.model.idem.CAAuthorizationAttribute;
import th.com.bloomcode.paymentservice.model.idem.CAEffectiveAuthorization;
import th.com.bloomcode.paymentservice.model.idem.CAUser;
import th.com.bloomcode.paymentservice.repository.idem.CAEffectiveAuthorizationRepository;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthorizeUtilService {

    private final JdbcTemplate jdbcTemplate;
    private final CAEffectiveAuthorizationRepository caEffectiveAuthorizationRepository;

    public static final String SYS_CONFIG = "TH.TGAS.AUTH.DBLINK_NOCACHE";
    public static final String ALL_AUTHORIZATION_PATTERN = "^\\s*\\*\\s*$";
    public static final String NOT_ALLOW_ACCESS_SQL = "1=0";
    public static final String ALLOW_ACCESS_SQL = "1=1";

    // Global authorization object
    public static final String GLOBAL_MINISTRY_OBJ = "CAMN";
    public static final String GLOBAL_COMPANY_CODE_OBJ = "CACC";
    public static final String GLOBAL_PAYMENT_CENTER_OBJ = "BGPC";
    public static final String GLOBAL_COST_CENTER_OBJ = "BGCC";
    public static final String GLOBAL_AREA_OBJ = "BGBA";

    // authorization activity
    public static final String CREATE_ACTIVITY = "01";
    public static final String UPDATE_ACTIVITY = "02";
    public static final String READ_ACTIVITY = "03";
    public static final String DELETE_ACTIVITY = "04";
    public static final String APPROVE_ACTIVITY = "05";
    public static final String CANCEL_ACTIVITY = "06";
    public static final String POST_ACTIVITY = "07";
    public static final String REVERSE_ACTIVITY = "08";
    public static final String BLOCK_ACTIVITY = "09";
    public static final String CONFIRM_ACTIVITY = "10";
    public static final String ACCEPT_ACTIVITY = "11";
    public static final String SELECT_ACTIVITY = "12";
    public static final String UPDATE_PBK_ACTIVITY = "13";
    public static final String APPROVE_2_ACTIVITY = "14";
    public static final String APPROVE_3_ACTIVITY = "15";
    public static final String APPROVE_4_ACTIVITY = "16";
    public static final String CREATE_PARK_ACTIVITY = "17";
    public static final String USE_ACTIVITY = "18";

    // authorization attribute
    public static final String MINISTRY_ATTRBUTE = "MINISTRY";
    public static final String COMPANY_CODE_ATTRIBUTE = "COMPCODE";
    public static final String AREA_ATTRIBUTE = "AREA";
    public static final String PAYMENT_CENTER_ATTRIBUTE = "PAYCTR";
    public static final String COST_CENTER_ATTRIBUTE = "COSTCTR";
    public static final String DOC_TYPE_ATTRIBUTE = "DOCTYPE";
    public static final String BUDGET_TYPE_ATTRIBUTE = "ITEMTYPE";
    public static final String BUDGET_CODE_ATTRIBUTE = "BGCODE";
    public static final String BUDGET_ACTIVITY_ATTRIBUTE = "BGACTV";
    public static final String COST_ACTIVITY_ATTRIBUTE = "COSTACTV";
    public static final String FUND_CENTER_ATTRIBUTE = "FUNDCTR";
    public static final String VENDOR_GROUP_ATTRIBUTE = "VDGRP";
    public static final String FUND_SOURCE_ATTRIBUTE = "FUND";
    public static final String BUDGET_ACCOUNT_ATTIBUTE = "BGACCT";
    public static final String GL_ACCOUNT_ATTRIBUTE = "GLACCT";
    public static final String ASSET_CLASS_ATTRIBUTE = "ASCLS";
    public static final String ASSET_TRANSACTION_TYPE_ATTRIBUTE = "ASTXTYPE";
    public static final String PAYMENT_BLOCK_ATTRIBUTE = "PAYBLK";
    public static final String ACCOUNT_TYPE_ATTIBUTE = "ACCTYPE";


    // authorization mode
    public static final String AUTHORIZATION_MODE = "TH.CA.AUTH.MODE";
    public static final int NONE_AUTH_MODE = 0;
    public static final int IAM_AUTH_MODE = 1;
    public static final int IDEM_AUTH_MODE = 2;
    private static final String AUTH_PAYMENT_CENTER = "TH.TGAS.AUTH.PAYMENT_CENTER";
    private static final String AUTH_COST_CENTER = "TH.TGAS.AUTH.COST_CENTER";
    private static final String AUTH_FI_AREA = "TH.TGAS.AUTH.FI_AREA";
    private static final String AUTH_COMP_CODE = "TH.TGAS.AUTH.COMP_CODE";
    private static final String IAM_SQL_CLAUSE =
            "	FROM IAM_USER_UNIT_DISBURSEMENT@IAM UD INNER JOIN IAM_USER_PROFILE@IAM UR ON (UR.USER_ID = UD.USER_ID)"
                    + " WHERE UR.USER_PROFILE_ID=? AND UD.IS_DELETED = 'N'";
    private static final Map<String, String> sqlColumn = new HashMap<>();

    private static final Map<String, String> activity = new HashMap<>();
    private static final Map<String, String> attribute = new HashMap<>();

    static {
        sqlColumn.put(AUTH_PAYMENT_CENTER, "UD.UNIT_DISBURSEMENT_ID");
        sqlColumn.put(AUTH_COST_CENTER, "UD.COST_CENTER_ID");
        sqlColumn.put(AUTH_FI_AREA, "UD.AREA_ID");
        sqlColumn.put(AUTH_COMP_CODE, "UR.DEPARTMENT_ID");

        activity.put("CREATE", CREATE_ACTIVITY);
        activity.put("UPDATE", UPDATE_ACTIVITY);
        activity.put("READ", READ_ACTIVITY);
        activity.put("DELETE", DELETE_ACTIVITY);
        activity.put("APPROVE", APPROVE_ACTIVITY);
        activity.put("CANCEL", CANCEL_ACTIVITY);
        activity.put("POST", POST_ACTIVITY);
        activity.put("REVERSE", REVERSE_ACTIVITY);
        activity.put("BLOCK", BLOCK_ACTIVITY);
        activity.put("CONFIRM", CONFIRM_ACTIVITY);
        activity.put("ACCEPT", ACCEPT_ACTIVITY);
        activity.put("SELECT", SELECT_ACTIVITY);
        activity.put("UPDATE_PBK", UPDATE_PBK_ACTIVITY);
        activity.put("APPROVE_2", APPROVE_2_ACTIVITY);
        activity.put("APPROVE_3", APPROVE_3_ACTIVITY);
        activity.put("APPROVE_4", APPROVE_4_ACTIVITY);
        activity.put("CREATE_PARK", CREATE_PARK_ACTIVITY);
        activity.put("USE", USE_ACTIVITY);

        attribute.put("MINISTRY", MINISTRY_ATTRBUTE);
        attribute.put("COMPANY_CODE", COMPANY_CODE_ATTRIBUTE);
        attribute.put("AREA", AREA_ATTRIBUTE);
        attribute.put("PAYMENT_CENTER", PAYMENT_CENTER_ATTRIBUTE);
        attribute.put("COST_CENTER", COST_CENTER_ATTRIBUTE);
        attribute.put("DOC_TYPE", DOC_TYPE_ATTRIBUTE);
        attribute.put("BUDGET_TYPE", BUDGET_TYPE_ATTRIBUTE);
        attribute.put("BUDGET_CODE", BUDGET_CODE_ATTRIBUTE);
        attribute.put("BUDGET_ACTIVITY", BUDGET_ACTIVITY_ATTRIBUTE);
        attribute.put("COST_ACTIVITY", COST_ACTIVITY_ATTRIBUTE);
        attribute.put("FUND_CENTER", FUND_CENTER_ATTRIBUTE);
        attribute.put("VENDOR_GROUP", VENDOR_GROUP_ATTRIBUTE);
        attribute.put("FUND_SOURCE", FUND_SOURCE_ATTRIBUTE);
        attribute.put("BUDGET_ACCOUNT", BUDGET_ACCOUNT_ATTIBUTE);
    }

    public AuthorizeUtilService(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate, CAEffectiveAuthorizationRepository caEffectiveAuthorizationRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.caEffectiveAuthorizationRepository = caEffectiveAuthorizationRepository;
    }

    /*
     * IAM authorization
     */
    private List<String> getAuthorizationData(String dataType, String userName) {
        //		@SuppressWarnings("unchecked")
        //		List<String> result = (List<String>) ctx.get(dataType + "." + userName);
        //		if (result != null)
        //			return result;
        //		result = new ArrayList<>();

        List<String> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
        sql.append(sqlColumn.get(dataType)).append(IAM_SQL_CLAUSE);

        try {
            // prepare sql statement
            result = jdbcTemplate.queryForList(sql.toString(), new Object[]{userName}, String.class);
        } catch (Exception e) {
            result.clear();
            //			ctx.remove(dataType + "." + userName);
            return result;
        } finally {
            // TODO close DB
        }

        //		ctx.put(dataType + "." + userName, result);
        return result;
    }

    public List<String> getAuthPaymentCenter(String userName) {
        return getAuthorizationData(AUTH_PAYMENT_CENTER, userName);
    }

    public List<String> getAuthCostCenter(String userName) {
        return getAuthorizationData(AUTH_COST_CENTER, userName);
    }

    public List<String> getAuthFIArea(String userName) {
        return getAuthorizationData(AUTH_FI_AREA, userName);
    }

    public List<String> getAuthCompanyCode(String userName) {
        return getAuthorizationData(AUTH_COMP_CODE, userName);
    }

    /*
     * Local authorization
     */
    // User level

    public boolean isAuthorized(
            String name,
            String object,
            String operation,
            Map<String, List<String>> conditions,
            List<Map.Entry<String, String>> errors) {
        CAUser user = CAUser.get(name, jdbcTemplate);
        return isAuthorized(user, object, operation, conditions, errors);
    }

    public boolean isAuthorized(
            CAUser user,
            String object,
            String operation,
            Map<String, List<String>> conditions,
            List<Map.Entry<String, String>> errors) {
        if (user == null || "N".equalsIgnoreCase(user.getIsActive()) || user.getUserId() == 0)
            return false;

        boolean result = true;
        for (Map.Entry<String, List<String>> entry : conditions.entrySet()) {
            String attribute = entry.getKey();
            List<String> valueList = entry.getValue();
            if (valueList.isEmpty()) {
                errors.add(new AbstractMap.SimpleEntry<String, String>(attribute, ""));
                result = false;
            }
            for (String value : valueList) {
                if (!user.isAuthorized(object, operation, attribute, value, jdbcTemplate)) {
                    errors.add(new AbstractMap.SimpleEntry<String, String>(attribute, value));
                    result = false;
                }
            }
        }
        return result;
    }

    public String getAuthorizationSQL(
            CAUser user, String object, String activity, String attribute, String column, List<Object> params) {
        if ("N".equalsIgnoreCase(user.getIsActive())) return AuthorizeUtilService.NOT_ALLOW_ACCESS_SQL;
//        AuthorizeUtilService authorizeUtilService = new AuthorizeUtilService(jdbcTemplate);
        return createAuthorizationSQL(
                user.getUsername(), object, activity, attribute, column, params);
    }

    public boolean isAuthorized(
            String name, String object, String operation, String attribute, String value) {
        CAUser user = CAUser.get(name, jdbcTemplate);
        return isAuthorized(user, object, operation, attribute, value);
    }

    public boolean isAuthorized(
            CAUser user, String object, String operation, String attribute, String value) {
        if (user != null && user.getUserId() > 0 && "Y".equalsIgnoreCase(user.getIsActive()))
            return user.isAuthorized(object, operation, attribute, value, jdbcTemplate);
        return false;
    }

//    public String getUserAuthorizationSQL(
//            String name,
//            String object,
//            String activity,
//            String attribute,
//            String column,
//            List<Object> params) {
//        AuthorizeUtilService authorizeUtilService = new AuthorizeUtilService(jdbcTemplate);
//        return authorizeUtilService.getUserAuthorizationSQLCache(name, object, activity, attribute, column, params);
//    }

    @Cacheable(value = "userAuthorizationSQLCache", key = "{#name, #object, #activity, #attribute, #column, #params}", unless = "#result==null")
    public String getUserAuthorizationSQL(
            String name,
            String object,
            String activity,
            String attribute,
            String column,
            List<Object> params) {
        log.info(" getUserAuthorizationSQL {} ","aa");
        CAUser user = CAUser.get(name, jdbcTemplate);
        if (user == null || "N".equalsIgnoreCase(user.getIsActive()) || user.getUserId() == 0)
            return NOT_ALLOW_ACCESS_SQL;
        return getAuthorizationSQL(user, object, activity, attribute, column, params);
    }

    public String getAuthorizationActivity(String code) {
        CAAuthorizationActivity aa = CAAuthorizationActivity.get(code);
        if (aa != null) return aa.getDescription();
        return "";
    }

    public String getAuthorizationAttribute(String code) {
        CAAuthorizationAttribute aa = CAAuthorizationAttribute.get(code);
        if (aa != null) return aa.getDescription();
        return "";
    }

    /**
     * setStringFromToParameter - set range between parameter
     *
     * @param paramFrom
     * @param paramTo
     * @param params
     */
    public void setStringFromToParameter(
            String paramFrom, String paramTo, List<String> params) {
        if (!Util.isEmpty(paramFrom)) {
            paramFrom = getStringFromParameter(paramFrom, paramTo);
            params.add(paramFrom);
            if (!Util.isEmpty(paramTo)) {
                paramTo = getStringToParameter(paramTo);
                params.add(paramTo);
            }
        }
    }

    /**
     * getStringFromParameter - Transform from parameter wildcard
     *
     * @param paramFrom
     * @param paramTo
     * @return range value for from parameter
     */
    private String getStringFromParameter(String paramFrom, String paramTo) {
        String result = null;
        if (!Util.isEmpty(paramTo)) result = paramFrom.replaceAll("[*%]$", "");
        else {
            result = paramFrom.replaceAll("[*]", "%");
            result = result.replaceAll("[+]", "_");
        }
        return result;
    }

    /**
     * getStringToParameter - Transform to parameter wildcard
     *
     * @param paramTo
     * @return range value for to parameter
     */
    private String getStringToParameter(String paramTo) {
        return paramTo.replaceAll("[*%]$", "Z");
    }

    /**
     * createAuthorizationObjet - Convert SQL result set to authorization object list
     *
     * @param effectiveAuthorizations List<CAEffectiveAuthorization> from SQL query
     * @return List of authorization object
     * @throws SQLException
     */
    public List<AuthorizationObject> createAuthorizationObject(
            List<CAEffectiveAuthorization> effectiveAuthorizations) throws SQLException {
        List<AuthorizationObject> result = new ArrayList<>();
        for (CAEffectiveAuthorization caEffectiveAuthorization : effectiveAuthorizations) {
//      log.debug("caEffectiveAuthorization : {}", caEffectiveAuthorization);
            result.add(new AuthorizationObject(caEffectiveAuthorization));
        }
        return result;
    }

    /**
     * createAuthorizationSQL - create SQL staement for user
     *
     * @param username
     * @param object
     * @param activity
     * @param attribute
     * @param column
     * @param params
     * @return
     */
    public String createAuthorizationSQL(
            String username,
            String object,
            String activity,
            String attribute,
            String column,
            List<Object> params) {
        List<AuthorizationObject> authorizations = null;

        try {
            log.info("object : {}, activity : {}, attribute : {}", object, activity, attribute);
//            AuthorizeUtilService authorizeUtilService = new AuthorizeUtilService(jdbcTemplate);
            List<CAEffectiveAuthorization> effectiveAuthorizations = caEffectiveAuthorizationRepository.findByUsername(username, object, activity, attribute);
            log.info("effectiveAuthorizations : {}", effectiveAuthorizations);
            authorizations = createAuthorizationObject(effectiveAuthorizations);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // TODO close DB
        }

//      log.info("authorizations : {}", authorizations);
//        log.info("object : {}", object);
//        log.info("activity : {}", activity);
//        log.info("attribute : {}", attribute);
//        log.info("column : {}", column);
//
//
//        for (int i = 0; i < params.size(); i++) {
//            log.info("params : {}", params.get(i));
//        }
        return createAuthorizationSQL(authorizations, object, activity, attribute, column, params);
    }

    /**
     * createAuthorizationSQL - create SQL where clause for authorization field with parameters
     *
     * @param authorizations list of all authorization data object
     * @param object         authorization object type
     * @param column         SQL column name in where clause condition
     * @return SQL where clause for column authorization
     */
    public String createAuthorizationSQL(
            List<? extends IAuthorizationObject> authorizations,
            String object,
            String activity,
            String attribute,
            String column) {
        return createAuthorizationSQL(authorizations, object, activity, attribute, column, null);
    }

    /**
     * createAuthorizationSQL - create SQL where clause for authorization field with parameters
     *
     * @param authorizations list of all authorization data object
     * @param object         authorization object type
     * @param column         SQL column name in where clause condition
     * @param params         return list of parameter to send to SQL statement (if null - string concatenate
     *                       SQL will be used)
     * @return SQL where clause for column authorization
     */
    public String createAuthorizationSQL(
            List<? extends IAuthorizationObject> authorizations,
            String object,
            String activity,
            String attribute,
            String column,
            List<Object> params) {
        if (authorizations == null || authorizations.isEmpty())
            return NOT_ALLOW_ACCESS_SQL;

        boolean isAllAuthorized = false;
        StringBuilder result = new StringBuilder("");
        StringBuilder include = new StringBuilder("");
        StringBuilder exclude = new StringBuilder("");
        List<String> includeParams = new ArrayList<>();
        List<String> excludeParams = new ArrayList<>();

        // filter required authorization object, operation, attribute field
        List<IAuthorizationObject> filters = authorizations.parallelStream().filter(a -> a.isActive() && a.getAuthorizationObject().equalsIgnoreCase(object)
                && a.getAuthorizationActivity().equals(activity) && a.getAuthorizationAttribute().equals(attribute)).collect(Collectors.toList());

        if (filters == null || filters.isEmpty())
            return NOT_ALLOW_ACCESS_SQL;

        for (IAuthorizationObject auth : filters) {
            // SQL between parameter
            String fromParameter = "?";
            String toParameter = "?";
            if (params == null) {
                fromParameter = getStringFromParameter(auth.getFromValue(), auth.getToValue());
                if (!Util.isEmpty(auth.getToValue()))
                    toParameter = getStringToParameter(auth.getToValue());
            }

            // default is include condition
            StringBuilder currentSQL = include;
            List<String> currentParams = includeParams;

            // change to exclude variable if exclude condition
            if (auth.isExclude()) {
                currentSQL = exclude;
                currentParams = excludeParams;

                // check all authorization on from parameter
            } else if (!isAllAuthorized && Pattern.matches(ALL_AUTHORIZATION_PATTERN, auth.getFromValue())) {
                isAllAuthorized = true;
            }

            if (!isAllAuthorized || auth.isExclude()) {
                // create SQL statement
                if (currentSQL.length() > 0)
                    currentSQL.append(" OR ");
                currentSQL.append("UPPER(").append(column).append(")");

                if (!Util.isEmpty(auth.getToValue())) {
                    currentSQL.append(" BETWEEN UPPER(");
                    if (params == null)
                        currentSQL.append("'");
                    currentSQL.append(fromParameter);
                    if (params == null)
                        currentSQL.append("'");
                    currentSQL.append(") AND UPPER(");
                    if (params == null)
                        currentSQL.append("'");
                    currentSQL.append(toParameter);
                    if (params == null)
                        currentSQL.append("'");
                    currentSQL.append(")");

                } else {
                    currentSQL.append(" LIKE UPPER(");
                    if (params == null)
                        currentSQL.append("'");
                    currentSQL.append(fromParameter);
                    if (params == null)
                        currentSQL.append("'");
                    currentSQL.append(")");
                }

                // collect parameter
                if (params != null) {
                    currentParams.add(getStringFromParameter(auth.getFromValue(), auth.getToValue()));
                    if (!Util.isEmpty(auth.getToValue())) {
                        currentParams.add(getStringToParameter(auth.getToValue()));
                    }
                }
            }
        }

        // include statement
        if (isAllAuthorized) {
            result.append(ALLOW_ACCESS_SQL);

        } else if (include.length() > 0) {
            result.append("(").append(include.toString()).append(")");
            if (params != null)
                params.addAll(includeParams);
        }

        // exclude statement
        if (exclude.length() > 0) {
            if (result.length() > 0)
                result.append(" AND ");
            result.append("NOT (").append(exclude.toString()).append(")");
            if (params != null)
                params.addAll(excludeParams);
        }

        return result.toString();
    }

    //	public IAuthorizationProvider getAuthorizationProvider(String authObjectName, Object po)
    // {
    //		List<IAuthorizationFactory> factories =
    // Service.locator().list(IAuthorizationFactory.class).getServices();
    //
    //		for (IAuthorizationFactory factory : factories) {
    //			IAuthorizationProvider provider = factory.get(authObjectName, po);
    //			if (provider != null)
    //				return provider;
    //		}
    //		return null;
    //	}

    public String getActivity(String key) {
        return activity.get(key);
    }

    public String getAttribute(String key) {
        return attribute.get(key);
    }

//    @Cacheable(value = "caEffectiveAuthorization", key = "{#username, #object, #activity, #attribute}", unless = "#result==null")
//    public List<CAEffectiveAuthorization> findByUsername(String username, String object, String activity, String attribute) {
//        String sb = " SELECT ea.* FROM TH_CAEffectiveAuthorization ea" +
////                " INNER JOIN TH_CAUser u ON (u.TH_CAUser_ID=ea.TH_CAUser_ID)" +
//                " WHERE ea.UserName = ? AND ea.AuthorizationObject = ? AND ea.AuthorizationActivity = ? AND AuthorizationAttribute = ?";
//
//        return jdbcTemplate.query(
//                sb,
//                new Object[]{username, object, activity, attribute},
//                ColumnRowMapper.newInstance(CAEffectiveAuthorization.class));
//    }
}

package th.com.bloomcode.paymentservice.context;

import th.com.bloomcode.paymentservice.idem.entity.CompCode;
import th.com.bloomcode.paymentservice.idem.entity.PaymentMethod;
import th.com.bloomcode.paymentservice.idem.entity.VendorBankAccount;
import th.com.bloomcode.paymentservice.idem.entity.Wtx;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.model.idem.IdemConfig;
import th.com.bloomcode.paymentservice.model.idem.Vendor;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;
import th.com.bloomcode.paymentservice.model.payment.GLAccount;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
import th.com.bloomcode.paymentservice.payment.entity.SmartFee;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    public static Map<String, CAClient> sessionCAClient = new HashMap<>();
    public static List<String> sessionPayment = new ArrayList<>();
    public static Map<String, CAClient> sessionMap = new HashMap<>();
//    public static Map<String, Vendor> sessionVendor = new HashMap<>();
    public static Map<String, CompCode> sessionCompCode = new HashMap<>();
    public static Map<String, PaymentMethod> sessionPaymentMethod = new HashMap<>();
//    public static List<BankCode> sessionBankCodes = new ArrayList<>();
    public static List<SmartFee> sessionSmartFees = new ArrayList<>();
    public static List<SwiftFee> sessionSwiftFee = new ArrayList<>();
//    public static List<Vendor> sessionVendors = new ArrayList<>();
    public static Map<Vendor, ConfigWebOnline> sessionConfigWebOnline = new HashMap<>();
    public static Map<String, IdemConfig> sessionIdemConfig = new HashMap<>();
//    public static Map<String, BankCode> sessionBankCode = new HashMap<>();
//    public static Map<String, Vendor> sessionAlternativeVendor = new HashMap<>();
    public static Map<String, VendorBankAccount> sessionVendorBankAccount = new HashMap<>();
    public static Map<String, HouseBankPaymentMethod> sessionHouseBankPaymentMethod = new HashMap<>();
    public static Map<String, GLAccount> sessionGlAccountForPayment = new HashMap<>();
    public static Map<String, PayMethodConfig> sessionPayMethodConfig = new HashMap<>();


    public static Map<String, Wtx> sessionWtx = new HashMap<>();

    public static CAClient getUrl(String key) {

        if (null != key && key.length() > 2) {
            return sessionCAClient.get(key.substring(0, 2));
        } else {
            return null;
        }
    }
    public static CAClient getUrl(String formId, String key) {
        if (null != formId
                && (formId.equalsIgnoreCase("V01")
                || formId.equalsIgnoreCase("J12")
                || formId.equalsIgnoreCase("J13")
                || formId.equalsIgnoreCase("J14")
                || formId.equalsIgnoreCase("J41")
                || formId.equalsIgnoreCase("J42")
                || formId.equalsIgnoreCase("F23")
                || formId.equalsIgnoreCase("F24")
                || formId.equalsIgnoreCase("F33")
                || formId.equalsIgnoreCase("F34")
                || formId.equalsIgnoreCase("MASS"))) {
            if (key.startsWith("99")) {
                return sessionMap.get("99999");
            } else {
                return sessionMap.get("99999");
            }
        } else if (null != formId
                && (formId.equalsIgnoreCase("A30")
                || formId.equalsIgnoreCase("A31")
                || formId.equalsIgnoreCase("A32"))) {
            if (null != key) {
                return sessionMap.get(key);
            }
        } else {
            if (null != key && key.length() == 5) {
                return sessionMap.get(key);
            }
        }

        return new CAClient();
    }
}

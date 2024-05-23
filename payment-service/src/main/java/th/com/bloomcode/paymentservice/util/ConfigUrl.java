package th.com.bloomcode.paymentservice.util;

import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.model.idem.IdemConfig;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;

public class ConfigUrl {

  public static ConfigWebOnline getUrl(String formId, String key) {
    if (null != formId &&(formId.equalsIgnoreCase("V01") || formId.equalsIgnoreCase("J12") || formId.equalsIgnoreCase("J13")
            || formId.equalsIgnoreCase("J14")
            || formId.equalsIgnoreCase("J41") || formId.equalsIgnoreCase("J42")
            || formId.equalsIgnoreCase("F23") || formId.equalsIgnoreCase("F24")
            || formId.equalsIgnoreCase("F33") || formId.equalsIgnoreCase("F34"))) {
      return Context.sessionConfigWebOnline.get("99");
    } else {
      if (null != key && key.length() > 2) {
        return Context.sessionConfigWebOnline.get(key.substring(0, 2));
      }
    }

    return new ConfigWebOnline();
  }

  public static ConfigWebOnline getUrl(String key) {
    return getUrl("", key);
  }

  public static IdemConfig getLogin(String formId, String key) {
    if (null != formId &&(formId.equalsIgnoreCase("V01") || formId.equalsIgnoreCase("J12") || formId.equalsIgnoreCase("J13")
            || formId.equalsIgnoreCase("J14") || formId.equalsIgnoreCase("J41") || formId.equalsIgnoreCase("J42")
            || formId.equalsIgnoreCase("F23") || formId.equalsIgnoreCase("F24"))
            || formId.equalsIgnoreCase("F33") || formId.equalsIgnoreCase("F34")) {
      return Context.sessionIdemConfig.get("99");
    } else {
      if (null != key && key.length() > 2) {
        return Context.sessionIdemConfig.get(key.substring(0, 2));
      }
    }

    return new IdemConfig();
  }

  public static IdemConfig getLogin(String key) {
    return getLogin("", key);
  }
}

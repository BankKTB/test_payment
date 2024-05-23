package th.com.bloomcode.paymentservice.webservice.config;

import lombok.extern.slf4j.Slf4j;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.LoginRequest;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.springframework.stereotype.Component;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.model.idem.IdemConfig;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;

@Slf4j
@Component
public class ConfigIdempiere {

	public static LoginRequest getLogin(CAClient client) {
		LoginRequest login = new LoginRequest();
		login.setUser(client.getUsername());
		login.setPass(client.getPassword());
		login.setClientID(client.getClientId());
		login.setRoleID(client.getRoleId());
		login.setOrgID(client.getOrgId());
		login.setLang(Enums.Language.th_TH);
		return login;
	}

	public static WebServiceConnection getConnection(CAClient client) {
		//		System.out.println(configWebonline.getUrl());

		log.info("payment : {}", client.getTargetUrl());
		WebServiceConnection connection = new WebServiceConnection();
		connection.setAttempts(3);
		connection.setTimeout(300000);
		connection.setAttemptsTimeout(500);
		connection.setUrl(client.getTargetUrl());
		connection.setAppName("newgf-webonline");
		return connection;
	}

//	public static WebServiceConnection getConnection(String formId) {
//
//		WebServiceConnection connection = new WebServiceConnection();
//		connection.setAttempts(3);
//		connection.setTimeout(3000000);
//		connection.setAttemptsTimeout(3000000);
//		connection.setUrl(getUrlBase(formId, ""));
//		connection.setAppName("newgf-webonline");
//		return connection;
//	}
//
//	public static WebServiceConnection getConnection(String formId, String compCode) {
//		WebServiceConnection connection = new WebServiceConnection();
//		connection.setAttempts(3);
//		connection.setTimeout(3000000);
//		connection.setAttemptsTimeout(3000000);
//		connection.setUrl(getUrlBase(formId, compCode));
//		System.out.println(getUrlBase(formId, compCode));
//		connection.setAppName("newgf-webonline");
//		return connection;
//	}
}

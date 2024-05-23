package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

@Data
public class FIID {
	
	private String clientValue = null;
	private String tableLog = null;
	private int logID = 0;
	private String tableMQLog = null;
	private int mqLogID = 0;
	
	public FIID(String clientValue, String tableLog, int logID) {
		this.clientValue = clientValue;
		this.tableLog = tableLog;
		this.logID = logID;
	}
	
	public FIID(String clientValue, String tableLog, int logID, String tableMQLog, int mqLogID) {
		this.clientValue = clientValue;
		this.tableLog = tableLog;
		this.logID = logID;
		this.tableMQLog = tableMQLog;
		this.mqLogID = mqLogID;
	}
	
	public String getGroup3() {
		return clientValue + "." + tableLog + "." + logID;
	}
	

	
}

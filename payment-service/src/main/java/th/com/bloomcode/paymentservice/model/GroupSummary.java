package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupSummary {

    private String mappingName;
    private BigDecimal summary;
    private String summaryString; //*Tao 2021-02-23 convert bigdecimal to string
    
	public GroupSummary(String mappingName, BigDecimal summary, String summaryString) {
		super();
		this.mappingName = mappingName;
		this.summary = summary;
		this.summaryString = summaryString;
	}

	public GroupSummary() {
		super();
	}

}

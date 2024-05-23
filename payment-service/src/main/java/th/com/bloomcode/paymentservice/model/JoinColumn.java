package th.com.bloomcode.paymentservice.model;

import lombok.Data;

@Data
public class JoinColumn {
	private String joinColumnName;
	private SearchCriteria searchCriteria;
}
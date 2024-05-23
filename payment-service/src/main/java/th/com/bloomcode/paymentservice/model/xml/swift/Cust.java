package th.com.bloomcode.paymentservice.model.xml.swift;

import lombok.Data;

@Data
public class Cust {
    private String acct;
    private String info1;
    private String info2;
    private String info3;
    private String info4;
    private String opt;
}

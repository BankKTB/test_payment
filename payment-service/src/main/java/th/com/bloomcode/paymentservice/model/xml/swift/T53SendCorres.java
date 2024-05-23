package th.com.bloomcode.paymentservice.model.xml.swift;

import lombok.Data;

@Data
public class T53SendCorres {
    private String drCRInd;
    private String prtyID;
    private String loc;
    private String opt;
    private String bic;
}

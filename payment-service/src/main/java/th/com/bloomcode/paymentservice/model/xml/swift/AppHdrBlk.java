package th.com.bloomcode.paymentservice.model.xml.swift;

import lombok.Data;

@Data
public class AppHdrBlk {
    private String msgType;
    private MsgIPRef msgIPRef;
    private String dataRefNum;
    private String userID;
}

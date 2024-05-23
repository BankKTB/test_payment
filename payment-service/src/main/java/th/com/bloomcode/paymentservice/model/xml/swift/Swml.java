package th.com.bloomcode.paymentservice.model.xml.swift;

import lombok.Data;

@Data
public class Swml {
    private BasicHdrBlk basicHdrBlk;
    private AppHdrBlk appHdrBlk;
    private USRHdrBlk usrHdrBlk;
    private TxtBlk txtBlk;
    private String version;
}

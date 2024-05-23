package th.com.bloomcode.paymentservice.model.xml.swift;

import lombok.Data;

@Data
public class TxtBlk {
    private String t20SendRef;
    private T23BankOperCode t23BankOperCode;
    private T23BankOperCode t26TxTypeCode;
    private T32DateAmt t32DateAmt;
    private Cust t50OrdCust;
    private T53SendCorres t53SendCorres;
    private T53SendCorres t57AcctWithInst;
    private Cust t59BenefCust;
    private T23BankOperCode t71DetChrg;
    private T72SendToRecvInfo t72SendToRecvInfo;
    private T77RegltryRpt t77RegltryRpt;
}

package th.com.bloomcode.paymentservice.model.xml.swift;

import lombok.Data;

import java.time.LocalDate;

@Data
public class T32DateAmt {
    private LocalDate valueDate;
    private String ccy;
    private String amt;
    private String opt;
}

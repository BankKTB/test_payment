package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.util.List;

@Data
public class GenerateFile {

    private String reference;
    private List<OutputReport> smarts;
    private List<OutputReport> swifts;
    private List<OutputReport> giros;
    private List<OutputReport> inhouses;
    private List<OutputReport> gm;
    private List<OutputReport> ggiro;
    private List<OutputReport> inh;
    private List<String> errors;
}

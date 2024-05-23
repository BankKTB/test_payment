package th.com.bloomcode.paymentservice.model;

import lombok.Data;

@Data
public class ReturnLogError {
    private String fileName;
    private String error;
}

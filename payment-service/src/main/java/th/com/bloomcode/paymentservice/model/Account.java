package th.com.bloomcode.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {

    private String accountId;
    private String accountType;
    private String accountNo;
}

package th.com.bloomcode.paymentservice.model;

import lombok.Data;

@Data
public class ChangeFileStatusReturnRequest {
    private Long id;
    private String fileStatus;
}

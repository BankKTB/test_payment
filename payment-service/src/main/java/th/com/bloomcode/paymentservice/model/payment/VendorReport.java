package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class VendorReport extends BaseModel {

    private String vendorCode;
    private String vendorName;
    private String bankAccountNo;
    private Date paymentDate;
    private String paymentName;
    private String payingCompanyCode;
    private String payingCompanyName;
    private Long total;

}

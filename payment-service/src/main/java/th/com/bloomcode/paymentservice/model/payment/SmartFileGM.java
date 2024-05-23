package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class SmartFileGM extends BaseModel {

    public SmartFileGM(String account, String filename, String amount, String fee, String total, String transNo) {
        this.account = account;
        this.filename = filename;
        this.amount = amount;
        this.fee = fee;
        this.total = total;
        this.transNo = transNo;
    }

    private String account;
    private String filename;
    private String amount;
    private String fee;
    private String total;
    private String transNo;

    @Override
    public String toString() {
        return account + filename + amount + fee + total + transNo;
    }
}

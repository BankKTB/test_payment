package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.ReturnLogDetail;
import th.com.bloomcode.paymentservice.model.ReturnLogError;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReturnLogResultResponse {
    List<ReturnLogDetail> details = new ArrayList<>();
    List<ReturnLogError> errors = new ArrayList<>();

    public void addDetails(ReturnLogDetail detail) {
        details.add(detail);
    }

    public void addErrors(ReturnLogError error) {
        errors.add(error);
    }
}

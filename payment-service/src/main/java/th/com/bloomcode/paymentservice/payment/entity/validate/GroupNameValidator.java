package th.com.bloomcode.paymentservice.payment.entity.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class GroupNameValidator implements ConstraintValidator<GroupName, String> {

    List<String> groupNames = Arrays.asList("Document", "Vendor");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return groupNames.contains(s);
    }
}

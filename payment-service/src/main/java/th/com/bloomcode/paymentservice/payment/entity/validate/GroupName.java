package th.com.bloomcode.paymentservice.payment.entity.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GroupNameValidator.class)
@Documented
public @interface GroupName {

    String message() default "Group name is not allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

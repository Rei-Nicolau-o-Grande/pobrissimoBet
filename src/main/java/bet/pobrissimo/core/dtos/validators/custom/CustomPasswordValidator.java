package bet.pobrissimo.core.dtos.validators.custom;

import bet.pobrissimo.core.dtos.validators.MyPasswordValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MyPasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPasswordValidator {

    String message() default "";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}

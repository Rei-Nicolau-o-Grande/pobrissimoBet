package bet.pobrissimo.core.dtos.validators.custom;

import bet.pobrissimo.core.dtos.validators.UniqueEmailValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "Email já cadastrado";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}

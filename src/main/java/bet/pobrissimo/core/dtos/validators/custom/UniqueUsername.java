package bet.pobrissimo.core.dtos.validators.custom;

import bet.pobrissimo.core.dtos.validators.UniqueUsernameValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {

    String message() default "Usuário já cadastrado";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}

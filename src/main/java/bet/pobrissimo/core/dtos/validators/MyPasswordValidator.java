package bet.pobrissimo.core.dtos.validators;

import bet.pobrissimo.core.dtos.validators.custom.CustomPasswordValidator;
import bet.pobrissimo.core.service.PasswordService;
import bet.pobrissimo.infra.exception.PasswordInvalidException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyPasswordValidator implements ConstraintValidator<CustomPasswordValidator, String> {

    private final PasswordService passwordService;

    public MyPasswordValidator(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public void initialize(CustomPasswordValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            passwordService.validate(value);
            return true;
        } catch (PasswordInvalidException e) {
            List<String> errors = e.getErrors();
            context.disableDefaultConstraintViolation();
            errors.forEach(error ->
                    context.buildConstraintViolationWithTemplate(error)
                            .addConstraintViolation()
            );
            return false;
        }
    }
}

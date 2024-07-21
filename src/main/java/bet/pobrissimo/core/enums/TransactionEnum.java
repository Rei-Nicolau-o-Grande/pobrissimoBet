package bet.pobrissimo.core.enums;

import bet.pobrissimo.infra.exception.TransactionTypeDoesNotExistException;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;

public enum TransactionEnum {
    DEPOSIT,
    WITHDRAW;

    @JsonCreator
    public static TransactionEnum fromValue(String value) {
        if (value.isBlank()) {
            throw new TransactionTypeDoesNotExistException(
                    HttpStatus.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "O tipo de transação não pode ser nulo ou vazio."
            );
        }

        for (TransactionEnum type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TransactionTypeDoesNotExistException(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Opção de transação inválido: " + value
        );
    }
}

package bet.pobrissimo.core.enums;

import bet.pobrissimo.infra.exception.TransactionTypeDoesNotExistException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionEnum {
    DEPOSIT,
    WITHDRAW;

    @JsonCreator
    public static TransactionEnum fromValue(String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("O tipo de transação não pode ser nulo ou vazio.");
        }

        for (TransactionEnum type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TransactionTypeDoesNotExistException("Opção de transação inválido. " + value);
    }
}

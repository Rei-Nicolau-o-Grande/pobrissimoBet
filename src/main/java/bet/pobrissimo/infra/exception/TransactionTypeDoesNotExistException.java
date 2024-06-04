package bet.pobrissimo.infra.exception;

public class TransactionTypeDoesNotExistException extends RuntimeException {
    public TransactionTypeDoesNotExistException(String message) {
        super(message);
    }
}

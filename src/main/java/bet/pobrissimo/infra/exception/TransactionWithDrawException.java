package bet.pobrissimo.infra.exception;

public class TransactionWithDrawException extends RuntimeException {
    public TransactionWithDrawException(String message) {
        super(message);
    }
}

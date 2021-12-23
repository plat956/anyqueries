package by.latushko.anyqueries.exception;

public class EntityTransactionException extends Exception{
    public EntityTransactionException() {
        super();
    }

    public EntityTransactionException(String message) {
        super(message);
    }

    public EntityTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityTransactionException(Throwable cause) {
        super(cause);
    }
}

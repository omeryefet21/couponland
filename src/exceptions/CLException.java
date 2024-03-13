package exceptions;

public abstract class CLException extends Exception{
    protected final Integer errorCode;

    public CLException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}

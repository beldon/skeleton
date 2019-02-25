package skeleton.exception;

/**
 * @author Beldon
 */
public class CookieParseException extends RuntimeException {

    public CookieParseException(String message) {
        super(message);
    }

    public CookieParseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

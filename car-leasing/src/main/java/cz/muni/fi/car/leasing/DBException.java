package cz.muni.fi.car.leasing;

/**
 * @author Jan Budinsky
 */

public class DBException extends RuntimeException {

    public DBException(String message) {
        super(message);
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}

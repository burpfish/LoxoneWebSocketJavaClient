package org.chelmer.exceptions;

/**
 * Created by burfo on 13/02/2017.
 */
public class UnknownCommandException extends RuntimeException {

    public UnknownCommandException(String message) {
        super(message);
    }

    public UnknownCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}

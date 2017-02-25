package org.chelmer.exceptions;

/**
 * Created by burfo on 14/02/2017.
 */
public class UnexpectedTypeException extends RuntimeException {

    public UnexpectedTypeException(String message) {
        super(message);
    }

    public UnexpectedTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}

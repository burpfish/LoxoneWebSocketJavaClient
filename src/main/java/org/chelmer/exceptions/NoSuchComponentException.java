package org.chelmer.exceptions;

/**
 * Created by burfo on 13/02/2017.
 */
public class NoSuchComponentException extends RuntimeException {

    public NoSuchComponentException(String message) {
        super(message);
    }

    public NoSuchComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}

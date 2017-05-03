package org.chelmer.exceptions;

/**
 * Created by burfo on 13/02/2017.
 */
public class ParsingException extends RuntimeException {

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

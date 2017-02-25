package org.chelmer.exceptions;

/**
 * Created by burfo on 13/02/2017.
 */
public class CouldNotDeserializeException extends RuntimeException {

    public CouldNotDeserializeException(String message) {
        super(message);
    }

    public CouldNotDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }
}

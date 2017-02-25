package org.chelmer.exceptions;

/**
 * Created by burfo on 21/02/2017.
 */
public class CoundNotAuthenticateException extends RuntimeException {

    public CoundNotAuthenticateException(String message) {
        super(message);
    }

    public CoundNotAuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }
}

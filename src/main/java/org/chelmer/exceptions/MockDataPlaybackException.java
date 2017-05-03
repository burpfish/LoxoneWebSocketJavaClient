package org.chelmer.exceptions;

/**
 * Created by burfo on 21/02/2017.
 */
public class MockDataPlaybackException extends RuntimeException {

    public MockDataPlaybackException(String message) {
        super(message);
    }

    public MockDataPlaybackException(String message, Throwable cause) {
        super(message, cause);
    }
}

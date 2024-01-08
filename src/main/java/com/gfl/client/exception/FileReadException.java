package com.gfl.client.exception;

public class FileReadException extends RuntimeException {
    public FileReadException(final String message) {
        super(message);
    }

    public FileReadException(final String message, Throwable cause) {
        super(message, cause);
    }

}

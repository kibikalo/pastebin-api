package com.kibikalo.pastebin.exceptions;

public class S3ServiceException extends RuntimeException {
    public S3ServiceException(String message) {
        super(message);
    }

    public S3ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

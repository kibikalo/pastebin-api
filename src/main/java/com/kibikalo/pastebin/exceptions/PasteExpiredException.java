package com.kibikalo.pastebin.exceptions;

public class PasteExpiredException extends RuntimeException {
    public PasteExpiredException(String message) {
        super(message);
    }
}

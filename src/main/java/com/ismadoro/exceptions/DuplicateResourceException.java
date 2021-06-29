package com.ismadoro.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public String message;
    public DuplicateResourceException(String message) {
        this.message = message;
    }
}

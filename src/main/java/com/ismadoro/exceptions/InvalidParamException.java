package com.ismadoro.exceptions;

public class InvalidParamException extends RuntimeException {
    public String message;
    public InvalidParamException(String message) {
        this.message = message;
    }
}
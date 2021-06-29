package com.ismadoro.exceptions;

public class InvalidInput extends RuntimeException{
    public String message;
    public InvalidInput(String message) {
        this.message = message;
    }
}

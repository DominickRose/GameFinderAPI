package com.ismadoro.exceptions;

public class InvalidJson extends RuntimeException{
    public String message;
    public InvalidJson(String message) {
        this.message = message;
    }
}

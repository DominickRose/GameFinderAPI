package com.ismadoro.exceptions;

public class ResourceNotFound extends RuntimeException{

    public String message;
    public ResourceNotFound(String message) {
        this.message = message;
    }
}

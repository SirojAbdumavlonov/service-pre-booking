package com.example.preordering.exception;



public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}

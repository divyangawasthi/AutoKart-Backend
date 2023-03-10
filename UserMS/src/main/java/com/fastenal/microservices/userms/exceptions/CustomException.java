package com.fastenal.microservices.userms.exceptions;

public class CustomException extends IllegalArgumentException {
    public CustomException(String msg){
        super(msg);
    }
}


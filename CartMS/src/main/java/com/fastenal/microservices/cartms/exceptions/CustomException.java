package com.fastenal.microservices.cartms.exceptions;

public class CustomException extends IllegalArgumentException {
    public CustomException(String msg){
        super(msg);
    }
}


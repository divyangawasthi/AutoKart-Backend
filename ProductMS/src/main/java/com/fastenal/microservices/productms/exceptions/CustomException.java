package com.fastenal.microservices.productms.exceptions;

public class CustomException extends IllegalArgumentException {
    public CustomException(String msg){
        super(msg);
    }
}


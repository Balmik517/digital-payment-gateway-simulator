package com.balmik.dpgs.exception;

public class PaymentAlreadyExistsException extends RuntimeException{

    public PaymentAlreadyExistsException(String message){
        super(message);
    }
}

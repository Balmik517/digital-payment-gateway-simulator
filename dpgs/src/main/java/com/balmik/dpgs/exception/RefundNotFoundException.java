package com.balmik.dpgs.exception;

public class RefundNotFoundException extends RuntimeException{

    public RefundNotFoundException(String message){
        super(message);
    }
}

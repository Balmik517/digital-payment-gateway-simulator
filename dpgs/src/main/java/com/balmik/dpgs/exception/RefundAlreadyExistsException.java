package com.balmik.dpgs.exception;

public class RefundAlreadyExistsException extends RuntimeException{

    public RefundAlreadyExistsException(String message){
        super(message);
    }
}

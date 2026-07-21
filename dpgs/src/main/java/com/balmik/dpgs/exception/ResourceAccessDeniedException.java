package com.balmik.dpgs.exception;

public class ResourceAccessDeniedException extends RuntimeException{
    public ResourceAccessDeniedException(String message){
        super(message);
    }
}

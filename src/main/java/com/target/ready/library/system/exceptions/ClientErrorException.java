package com.target.ready.library.system.exceptions;

public class ClientErrorException extends RuntimeException{
    public ClientErrorException(String message){
        super(message);
    }
}
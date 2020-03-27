package com.habeebcycle.springcacheredis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message){
        super(message);
    }
}

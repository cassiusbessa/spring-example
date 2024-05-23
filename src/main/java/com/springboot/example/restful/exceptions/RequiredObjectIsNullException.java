package com.springboot.example.restful.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequiredObjectIsNullException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public RequiredObjectIsNullException() {
        super("It is not allowed to persist a null object.");
    }
    
    public RequiredObjectIsNullException(String exception) {
        super(exception);
    }


}

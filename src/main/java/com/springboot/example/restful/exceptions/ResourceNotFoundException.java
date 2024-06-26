package com.springboot.example.restful.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String exception) {
        super(exception);
    }

    public ResourceNotFoundException(String exception, Throwable cause) {
        super(exception, cause);
    }


}

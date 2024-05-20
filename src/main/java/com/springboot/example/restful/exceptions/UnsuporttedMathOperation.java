package com.springboot.example.restful.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsuporttedMathOperation extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UnsuporttedMathOperation(String exception) {
        super(exception);
    }


}

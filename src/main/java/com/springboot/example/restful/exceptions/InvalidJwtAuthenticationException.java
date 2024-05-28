package com.springboot.example.restful.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJwtAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public InvalidJwtAuthenticationException(String exception) {
        super(exception);
    }

}

package com.example.mathengerapi.exceptions;

import javax.servlet.ServletException;

public class UnAuthorizedException extends ServletException {
    public UnAuthorizedException(String message) {
        super(message);
    }
}

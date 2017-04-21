/*
 * Copyright RepCar AD 2017
 */
package com.repcar.user.exception;

import org.springframework.http.HttpStatus;

public class ExceptionBody {
    private int status;
    private String message;

    public ExceptionBody(HttpStatus status, String message) {
        super();
        this.status = status.value();
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.controllers;

import org.springframework.http.HttpStatus;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
public class ExceptionBody {
    private int status;
    private String error;
    private String message;

    public ExceptionBody(HttpStatus status, Throwable e) {
        super();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        StringBuilder buffer = new StringBuilder(128).append(e.getMessage());
        while (e.getCause() != null) {
            e = e.getCause();
            buffer.append(" - ").append(e.getMessage());
        }
        this.message = buffer.toString();
    }

    public ExceptionBody(HttpStatus status, String message) {
        super();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}

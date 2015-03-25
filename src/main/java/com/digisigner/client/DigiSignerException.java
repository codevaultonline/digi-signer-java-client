package com.digisigner.client;

import java.util.List;

@SuppressWarnings("serial")
public class DigiSignerException extends RuntimeException {

    private final List<String> errors;
    private final Integer httpCode;

    private DigiSignerException(String message, List<String> errors, Integer httpCode) {
        super(message);
        this.errors = errors;
        this.httpCode = httpCode;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

}

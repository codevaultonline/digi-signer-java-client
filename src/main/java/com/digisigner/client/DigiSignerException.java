package com.digisigner.client;

import java.util.List;

@SuppressWarnings("serial")
public class DigiSignerException extends RuntimeException {

    private List<String> errors;
    private Integer httpCode;

    public DigiSignerException(String message) {
        super(message);
    }

    public DigiSignerException(String message, List<String> errors, Integer httpCode) {
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

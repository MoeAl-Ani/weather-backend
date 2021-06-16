package com.infotamia.weather.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammed Al-Ani
 */
public class RestCoreException extends RuntimeException {

    private ExceptionMessage exceptionMessage;
    private List<ExceptionMessage> errors = new ArrayList<>();
    private Integer statusCode;

    public RestCoreException(ExceptionMessage exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
        this.errors.add(exceptionMessage);
    }

    public RestCoreException(Integer status, BaseErrorCode code, String message) {
        this.exceptionMessage = new ExceptionMessage(status, code, message, "");
        this.statusCode = status;
        this.errors.add(exceptionMessage);

    }

    public RestCoreException(Integer status, BaseErrorCode code, String message, String developerMessage) {
        this.exceptionMessage = new ExceptionMessage(status, code, message, developerMessage);
        this.statusCode = status;
        this.errors.add(exceptionMessage);

    }

    public RestCoreException(Integer statusCode, List<ExceptionMessage> errors) {
        this.statusCode = statusCode;
        this.errors = errors;
    }

    public List<ExceptionMessage> getErrors() {
        return errors;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "RestCoreException{" +
                "exceptionMessage=" + exceptionMessage +
                ", errors=" + errors +
                ", statusCode=" + statusCode +
                '}';
    }
}

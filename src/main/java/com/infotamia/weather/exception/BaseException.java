package com.infotamia.weather.exception;

/**
 * Application base exception.
 *
 * @author Mohammed Al-Ani
 */
public abstract class BaseException extends Exception {
    private final BaseErrorCode errorCode;

    public BaseException() {
        this.errorCode = BaseErrorCode.UNKNOWN;
    }

    public <T extends BaseErrorCode> BaseException(String message, T errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseErrorCode getErrorCode() {
        return errorCode;
    }
}

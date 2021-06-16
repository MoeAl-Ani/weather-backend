package com.infotamia.weather.exception;

/**
 * @author Mohammed Al-Ani
 */
public class OperationNotAllowedException extends BaseException {
    public OperationNotAllowedException(String message, BaseErrorCode baseErrorCode) {
        super(message, baseErrorCode);
    }
}

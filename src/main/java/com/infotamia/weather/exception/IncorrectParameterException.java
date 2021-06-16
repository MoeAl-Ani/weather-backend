package com.infotamia.weather.exception;

/**
 * @author Mohammed Al-Ani
 */
public class IncorrectParameterException extends BaseException {
    public IncorrectParameterException(String message, BaseErrorCode baseErrorCode) {
        super(message, baseErrorCode);
    }
}

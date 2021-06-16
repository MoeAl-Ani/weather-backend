package com.infotamia.weather.exception;

/**
 * @author Mohammed Al-Ani
 */
public class DataCorruptedException extends BaseException {
    public DataCorruptedException(String message, BaseErrorCode baseErrorCode) {
        super(message, baseErrorCode);
    }
}

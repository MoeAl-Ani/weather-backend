package com.infotamia.weather.exception;

/**
 * @author Mohammed Al-Ani
 */
public class ItemNotFoundException extends BaseException {
    public ItemNotFoundException(String message, BaseErrorCode baseErrorCode) {
        super(message, baseErrorCode);
    }
}

package com.infotamia.weather.exception;

/**
 * @author Mohammed Al-Ani
 */
public class ExceptionMessage {

    private Integer status;
    private BaseErrorCode code;
    private String message;
    private String developerMessage;

    public ExceptionMessage(Integer status, BaseErrorCode code) {
        this.status = status;
        this.code = code;
        this.message = message = "";
        this.developerMessage = "";
    }

    public ExceptionMessage(Integer status, BaseErrorCode code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = "";
    }

    public ExceptionMessage(Integer status, BaseErrorCode code, String message, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BaseErrorCode getCode() {
        return code;
    }

    public void setCode(BaseErrorCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    @Override
    public String toString() {
        return "ExceptionMessage{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", developerMessage='" + developerMessage + '\'' +
                '}';
    }
}

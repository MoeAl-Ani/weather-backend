package com.infotamia.weather.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammed Al-Ani
 */
@ControllerAdvice
public class GlobalExceptionMapper  {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionMapper.class);
    @ExceptionHandler({BaseException.class})
    protected ResponseEntity<Object> handleBaseException(BaseException exception, WebRequest webRequest) {
        RestCoreException restCoreException = null;
        if (exception instanceof ItemNotFoundException || exception instanceof IncorrectParameterException) {
            restCoreException = new RestCoreException(
                    400,
                    exception.getErrorCode(),
                    exception.getMessage(),
                    exception.getMessage());
        } else if (exception instanceof OperationNotAllowedException) {
            restCoreException = new RestCoreException(
                    403,
                    exception.getErrorCode(),
                    exception.getMessage(),
                    exception.getMessage());
        } else if (exception instanceof DataCorruptedException) {
            restCoreException = new RestCoreException(
                    500,
                    exception.getErrorCode(),
                    exception.getMessage(),
                    exception.getMessage());
        }  else {
            restCoreException = new RestCoreException(
                    500,
                    BaseErrorCode.UNKNOWN,
                    exception.getMessage(),
                    exception.getMessage());
        }
        return handleRestCoreException(restCoreException);
    }

    @ExceptionHandler({RestCoreException.class})
    protected ResponseEntity<Object> handleRestCoreException(RestCoreException exception) {
        log.error("EXCEPTION: {}", exception.toString());
        return new ResponseEntity<>(exception, HttpStatus.valueOf(exception.getStatusCode()));
    }
    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<Object> handleThrowableException(Throwable exception) {
        RestCoreException restCoreException = new RestCoreException(500, BaseErrorCode.UNKNOWN, exception.getMessage());
        return handleRestCoreException(restCoreException);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleThrowableException(ConstraintViolationException exception) {
        RestCoreException restCoreException = new RestCoreException(400, prepareMessage(exception));
        return handleRestCoreException(restCoreException);
    }

    private List<ExceptionMessage> prepareMessage(ConstraintViolationException exception) {
        List<ExceptionMessage> messages = new ArrayList<>();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            ExceptionMessage error = new ExceptionMessage(400, BaseErrorCode.INVALID_PARAMETERS, cv.getMessage());
            messages.add(error);
        }
        return messages;
    }

    @ExceptionHandler(BindException.class)
    ResponseEntity<?> onMethodArgumentNotValidException(
            BindException e) {
        List<ExceptionMessage> messages = new ArrayList<>();
        for (ObjectError allError : e.getBindingResult().getAllErrors()) {
            messages.add(new ExceptionMessage(400, BaseErrorCode.INVALID_PARAMETERS, allError.getDefaultMessage()));
        }
        return new ResponseEntity<>(new RestCoreException(400, messages), HttpStatus.valueOf(400));
    }
}

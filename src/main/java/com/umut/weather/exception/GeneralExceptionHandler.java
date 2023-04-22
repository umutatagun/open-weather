package com.umut.weather.exception;

import com.umut.weather.constant.Constant;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handle(ConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handle(RequestNotPermitted ex) {
        return new ResponseEntity<>(Constant.ERROR_MESSAGES.REQUEST_LIMIT_EXCEEDED, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Object> handle(GenericException exception) {
        ExceptionModel exceptionModel = new ExceptionModel();
        exceptionModel.setCode(exception.getErrorType().getCode());
        exceptionModel.setMessage(exception.getErrorType().getMessage());

        logError(exception);
        return new ResponseEntity<>(exceptionModel, exception.getErrorType().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ExceptionModel handleNoHandlerFoundException(Exception exception) {
        ExceptionModel exceptionModel = new ExceptionModel();
        exceptionModel.setCode(GenericErrorType.GENERIC_ERROR.getCode());
        exceptionModel.setMessage(GenericErrorType.GENERIC_ERROR.getMessage());
        logError(exception);

        return exceptionModel;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionModel runtimeException(RuntimeException exception) {
        ExceptionModel exceptionModel = new ExceptionModel();
        exceptionModel.setCode(GenericErrorType.GENERIC_ERROR.getCode());
        exceptionModel.setMessage(GenericErrorType.GENERIC_ERROR.getMessage());
        logError(exception);

        return exceptionModel;
    }

    private void logError(Exception exception) {
        log.error("Unexpected exception", exception);
    }
}

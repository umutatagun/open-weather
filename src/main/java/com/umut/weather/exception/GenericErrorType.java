package com.umut.weather.exception;

import com.umut.weather.constant.Constant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Getter
public class GenericErrorType {
    public static final GenericErrorType GENERIC_ERROR =
            new GenericErrorType("GE_001", OK, Constant.ERROR_MESSAGES.GENERIC);
    public static final GenericErrorType REQUEST_LIMIT_ERROR =
            new GenericErrorType("GE_002", TOO_MANY_REQUESTS, Constant.ERROR_MESSAGES.REQUEST_LIMIT_EXCEEDED);


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public GenericErrorType(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

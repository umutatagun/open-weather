package com.umut.weather.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractBaseException extends RuntimeException{
    private final GenericErrorType errorType;
    private final String[] args;
}

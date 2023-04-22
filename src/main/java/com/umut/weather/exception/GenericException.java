package com.umut.weather.exception;

public class GenericException extends AbstractBaseException{
    public GenericException() {
        super(GenericErrorType.GENERIC_ERROR, null);
    }
    public GenericException(final GenericErrorType errorType) {
        super(errorType, null);
    }
    public GenericException(final GenericErrorType errorType, final String... args) {
        super(errorType, args);
    }
}

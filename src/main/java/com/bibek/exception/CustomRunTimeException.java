package com.bibek.exception;

public class CustomRunTimeException extends RuntimeException {
    /*
     * Required when we want to add a custom message when throwing the exception
     */
    public CustomRunTimeException(String message) {
        super(message);
    }

    /*
     * Required when we want to wrap the exception generated inside the catch block and rethrow it
     */
    public CustomRunTimeException(Throwable cause) {
        super(cause);
    }

    /*
     * Required when we want both the above
     */
    public CustomRunTimeException(String message, Throwable throwable) {
        super(message, throwable);
    }


}

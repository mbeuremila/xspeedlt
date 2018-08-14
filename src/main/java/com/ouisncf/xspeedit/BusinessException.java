package com.ouisncf.xspeedit;

/**
 * Exception for the app
 */
public class BusinessException extends Exception {

    /**
     * Constructor with error message
     * @param message
     */
    public BusinessException(String message){
        super(message);
    }
}

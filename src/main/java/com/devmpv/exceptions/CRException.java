package com.devmpv.exceptions;

/**
 * Chan-reactor exception
 * 
 * @author devmpv
 *
 */
public class CRException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5748872141056604785L;

    public CRException(String message) {
	super(message);
    }

    public CRException(String message, Throwable cause) {
	super(message, cause);
    }

}

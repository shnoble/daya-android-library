package com.daya.android;

/**
 * Created by shhong on 2017. 9. 21..
 */

public class DayaException extends RuntimeException {
    static final long serialVersionUID = -1;

    /**
     * Constructs a new DayaException.
     *
     * @param message the detail message of this exception
     */
    public DayaException(String message) {
        super(message);
    }
}

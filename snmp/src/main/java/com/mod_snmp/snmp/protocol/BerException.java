package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * BER encoder/decoder Exception handler.
 * This exception is to indicate that their is an on-the-wire message
 * format error while encoding or decoding.
 */
public class BerException extends Exception {
    /**
     * Default constructor.
     */
    protected BerException() {
        super();
    }
    /**
     * Default constructor with an error message.
     * @param e The informative cause of the exception.
     */
    protected BerException(String e) {
        super(e);
    }
}

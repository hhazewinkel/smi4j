package com.mod_snmp.snmp.protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Generic exception handler for the Security models.
 */
public class SecModelException extends Exception {
    public SecModelException() {
        super();
    }
    public SecModelException(String e) {
        super(e);
    }
}

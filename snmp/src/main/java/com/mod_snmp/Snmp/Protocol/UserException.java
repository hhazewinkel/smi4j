package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Generic exception handler for users.
 */
public class UserException extends Exception {
    public UserException() {
        super();
    }
    public UserException(String e) {
        super(e);
    }
}

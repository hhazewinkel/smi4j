package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Exception handler for the Message testing (when done).
 */
public class SnmpMessageException extends Exception {
    public SnmpMessageException() {
        super();
    }
    public SnmpMessageException(String e) {
        super(e);
    }
}

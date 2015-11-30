package com.mod_snmp.snmp.values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * SnmpValueException
 * Exception handler for the Value testing (when done).
 */
public class SnmpValueException extends Exception {
    public SnmpValueException() {
        super();
    }
    public SnmpValueException(String e) {
        super(e);
    }
}

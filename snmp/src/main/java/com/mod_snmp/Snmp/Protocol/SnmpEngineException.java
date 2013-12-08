package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Generic exception handler for the SNMP engine.
 */
public class SnmpEngineException extends Exception {
    public SnmpEngineException() {
        super();
    }
    public SnmpEngineException(String e) {
        super(e);
    }
}

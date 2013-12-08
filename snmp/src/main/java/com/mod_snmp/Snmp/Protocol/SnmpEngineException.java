package com.mod_snmp.Snmp.Protocol;

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

package com.mod_snmp.Snmp.Protocol;

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

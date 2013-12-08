package com.mod_snmp.Snmp.Protocol;

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

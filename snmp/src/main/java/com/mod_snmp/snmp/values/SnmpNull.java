package com.mod_snmp.snmp.values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * SnmpNull.java
 * A type handling the Exceptions.
 */


public class SnmpNull implements SnmpValue {
    public static final int NO_SUCH_OBJECT = 0;
    public static final int NO_SUCH_INSTANCE = 1;
    public static final int END_OF_MIB_VIEW = 2;

    public int exception;
    private boolean set;

    /**
     * Constructor for an empty no type/value instance
     * or an SNMP exception value.
     */
    public SnmpNull() {
        exception = 0;
        set = false;
    }
    /**
     * Constructor for an empty no type/value instance
     * or an SNMP exception value which sets the exception 
     * value.
     */
    public SnmpNull(int which) throws SnmpValueException {
        if ((which < 0) || (2 < which)) {
            throw new SnmpValueException();
        }
        exception = which;
        set = true;
    }
    /**
     * Test whether the exception is a "no such object".
     */
    public boolean isNoSuchObject() {
        return ((exception == NO_SUCH_OBJECT) && set);
    }
    /**
     * Test whether the exception is a "no such instance".
     */
    public boolean isNoSuchInstance() {
        return (exception == NO_SUCH_INSTANCE);
    }
    /**
     * Test whether the exception is an "end of MIB view".
     */
    public boolean isEndOfMIBView() {
        return (exception == END_OF_MIB_VIEW);
    }
    /**
     * Retrieve the value as a String.               
     */
    public String toString() {
        if (set) {
            switch (exception) {
            case NO_SUCH_OBJECT:
                return "NoSuchObject";
            case NO_SUCH_INSTANCE:
                return "NoSuchInstance";
            case END_OF_MIB_VIEW:
                return "EndOfMibView";
            }
        }
        return "<null>";
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        value: Null: " + toString());
    }
}

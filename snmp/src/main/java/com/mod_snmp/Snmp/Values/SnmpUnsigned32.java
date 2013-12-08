package com.mod_snmp.Snmp.Values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


public class SnmpUnsigned32 implements SnmpValue {
    private final static Long max = new Long("4294967295");
    private long value;

    /**
     * Default constructor that initializes with '0'.
     */
    public SnmpUnsigned32() {
        value = 0;
    }
    /**
     * Constructor that initializes with val.
     */
    public SnmpUnsigned32(long val) throws SnmpValueException {
        if (val < 0) {
            throw new SnmpValueException("Unsigned32 value is negative");
        }
        if (max.longValue() < val) {
            throw new SnmpValueException("Unsigned32 value is too big");
        }
        value = val;
    }
    /**
     * Constructor that initializes with val.
     */
    public SnmpUnsigned32(int val) throws SnmpValueException {
        if (val < 0) {
            throw new SnmpValueException("Unisgned value is negative");
        }
        value = val;
    }
    /**
     * Constructor that initializes with val.
     */
    public SnmpUnsigned32(String val) {
        try {
            value = (long)(Integer.parseInt(val)& max.longValue());
        } catch (NumberFormatException e) {
            value = 0;
        }
    }
    /**
     * Retrieve the value.
     */
    public long getValue() {
        return value & max.longValue();
    }
    /**
     * Retrieve the value as a String.               
     */
    public String toString() {
        return "" + value;
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        value: Unsigned32: " + value); 
    }
}

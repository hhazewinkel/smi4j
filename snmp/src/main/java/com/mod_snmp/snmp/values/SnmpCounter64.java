package com.mod_snmp.Snmp.Values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * SnmpCounter64.java
 */
public class SnmpCounter64 implements SnmpValue {
    private long value;

    /**
     * Default constructor that initializes the counter on 0.
     */
    public SnmpCounter64() {
        value = 0;
    }
    /**
     * Constructor that initializes the counter on val
     * which is of type val.
     */
    public SnmpCounter64(long val) {
        value = val;
    }
    /**
     * Constructor that initializes the counter on val
     * which is of type string.
     */
    public SnmpCounter64(String val) {
        try {
            value = Long.parseLong(val);
        } catch (NumberFormatException e) {
            value = 0;
        }
    }
    /**
     * Increment the counter by '1'.
     */
    public void incr() {
          value++;
    }
    /**
     * Increment the counter by nr.
     */
    public void incr(long nr) {
        value += nr;
    }
    /**
     * Retrieve the value of the counter.
     */
    public long getValue() {
        return value;
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
        System.out.println("        value: Counter64: " + Long.toHexString(value)); 
    }
}

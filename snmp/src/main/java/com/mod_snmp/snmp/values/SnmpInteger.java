package com.mod_snmp.Snmp.Values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * The SnmpInteger class implements the SNMP integer type.
 */
public class SnmpInteger implements SnmpValue {
    protected int value;

    /**
     * Constructor that initializes the integer with '0'.
     */
    public SnmpInteger() {
        value = 0;
    }
    /**
     * Constructor that initializes the integer on the
     * value of the integer val.
     */
    public SnmpInteger(int val) {
        value = val;
    }
    /**
     * Constructor that initializes the integer on the
     * value represented by the string. The string must
     * consists only of digits.
     */
    public SnmpInteger(String val) {
        value = Integer.parseInt(val);
    }
    /**
     * Retrieve the value.
     */
    public int getValue() {
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
        System.out.println("        value: Integer32: " + value); 
    }
}

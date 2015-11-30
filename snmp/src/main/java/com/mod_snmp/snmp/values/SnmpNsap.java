package com.mod_snmp.Snmp.Values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * SnmpNsap.java
 */

public class SnmpNsap implements SnmpValue {
    private byte address[];

    /**
     * Constructor that initializes an NSapAddress.
     */
    public SnmpNsap() {
        address = new byte[24];
    }
    /**
     * Constructor that initializes an NSapAddress.
     */
    public SnmpNsap(byte []addr) throws SnmpValueException {
        if (addr.length == 24) {
            address = addr;
        }
        throw new SnmpValueException("Incorrect NSapAddress length");
    }
    public SnmpNsap(String addr) {
        /* Do conversion */
    }
    /**
     * Retrieve the value of the NSapAddress.
     */
    public byte[] getValue() {
        return address;
    }
    /**
     * Retrieve the value as a String.               
     */
    public String toString() {
        return "" + address;
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        value: nSapAddress: "); 
    }    
}

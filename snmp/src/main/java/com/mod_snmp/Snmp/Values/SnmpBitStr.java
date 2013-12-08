package com.mod_snmp.Snmp.Values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.Snmp.Utils.TypeConversions;

/**
 * The SnmpBitStr class implements the SNMP BITS type.
 */
public class SnmpBitStr implements SnmpValue {
    private final static int DEFAULT_BIT_STR_LENGTH = 32;
    
    private boolean bits[];
    private int length;

    /**
     * Default constructor for a DEFAULT_BIT_STR_LENGTH bits.
     */
    public SnmpBitStr() {
        this(DEFAULT_BIT_STR_LENGTH);
    }
    /**
     * Constructor for a specific amount of bits.
     */
    public SnmpBitStr(int size) {
        bits = new boolean[size];
        length = 0;
    }
    /**
     * Constructor for a given array of bytes.
     */
    public SnmpBitStr(byte[] buf) {
        for (int i = 0 ; i < buf.length ; i++) {
        }
    }
    /**
     * Constructor that translates a String representing the
     * bits into the internal bit-array.
     */
    public SnmpBitStr(String bits) {
        /* some form need to be selected. */
    }
    
    public String SnmpBitString(int prefix_nr) {
        String str = new String("");
        if (prefix_nr > length) {
            prefix_nr = length;
        }
        if (prefix_nr > 0) {
            str += bits[0];
            for (int i = 1; i < prefix_nr; i++) {
                str += "." + bits[i];
            }
        }
        return str;
    }
    /**
     * Concatenate an SnmpBitStr.
     */
    public SnmpBitStr concat(SnmpBitStr suffix) {
        int i;
        int j;
        if (length + suffix.length < bits.length) {
            boolean newbits[] = new boolean[length+suffix.length];
            for (i = 0; i < length; i++) {
                newbits[i] = bits[i];
            }
            for (i = length, j = 0; j < suffix.length; i++, j++) {
                newbits[j] = suffix.bits[i];
            }
            bits = newbits;
        }
        for (i = length, j = 0; j < suffix.length; i++, j++) {
            bits[j] = suffix.bits[i];
        }
        length += suffix.length;
        return this;
    }
    /**
     * Retrieve the value of the boolean array containing the
     * bit-array.
     */
    public boolean[] getValue() {
        return bits;
    }
    /**
     * Get specific bit.
     */
    public boolean getBit(int index) {
        return bits[index];
    }
    /**
     * Set specific bit.
     */
    public void setBit(int index) {
        bits[index] = true;
    }
    /**
     * Clear specific bit.
     */
    public void clearBit(int index) {
        bits[index] = false;
    }
    /**
     * Retrieve the value of the boolean array containing the
     * bit-array as a byte-array..
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[bits.length % 8];
        for (int i = 0; i < bits.length; i++) {
            for (int j = 0; j < 8; j++) {
 	    }
        }
        return bytes;
    }
    public String toString() {
        return TypeConversions.bytes2HexString(getBytes());
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        type : Bit String");
        System.out.println("        value: ");
    }
}

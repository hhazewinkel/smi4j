package com.mod_snmp.Snmp.Values;

/**
 * SnmpCounter32.java
 */
public class SnmpCounter32 implements SnmpValue {
    private final static Long max = new Long("4294967295");
    private long value;

    /**
     * Default constructor that initializes the counter on 0.
     */
    public SnmpCounter32() {
        value = 0;
    }
    /**
     * Constructor that initializes the counter on val
     * which is of type int.
     */
    public SnmpCounter32(int val) {
        value = val;
    }
    /**
     * Constructor that initializes the counter on val
     * which is of type long.
     */
    public SnmpCounter32(long val) {
        value = val & max.longValue();
    }
    /**
     * Constructor that initializes the counter on val
     * which is of type string.
     */
    public SnmpCounter32(String val) {
        try {
            value = (Integer.parseInt(val)& max.longValue());
        } catch (NumberFormatException e) {
            value = 0;
        }
    }
    /**
     * Increment the counter by '1'.
     */
    public void incr() {
          value = (value + 1) & max.longValue();
    }
    /**
     * Increment the counter by nr.
     */
    public void incr(int nr) {
        value = (value + nr) & max.longValue();
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
        return String.valueOf(value);
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        value: Counter32: " + Long.toHexString(value));
    }
}

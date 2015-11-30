package com.mod_snmp.snmp.values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * The object class that implements the SNMP TimeTicks type.
 */
public class SnmpTimeTicks implements SnmpValue {
    private final static Long max = new Long("4294967295");
    private long ticks;

    /**
     * Default constructor that initializes the value with 0.
     */
    public SnmpTimeTicks() {
        ticks = 0;
    }
    /**
     * Constructor that initializes with the value val of type long.
     */
    public SnmpTimeTicks(long val) throws SnmpValueException {
        testValue(val);
    }
    /**
     * Constructor that initializes with the value val of type String.
     */
    public SnmpTimeTicks(String val) throws SnmpValueException {
        try {
            testValue(Long.parseLong(val));
        } catch (NumberFormatException e) {
            ticks = 0;
        }
    }
    private void testValue(long val) throws SnmpValueException {
        if (val < 0) {
            throw new SnmpValueException("time ticks is negative");
        }
        if (max.longValue() < val) {
            throw new SnmpValueException("time ticks is too big");
        }
        ticks = val;
    }
    /**
     * Reset the timer to start now.
     */
    public void now() {
        ticks = System.currentTimeMillis() / 10;
    }
    /**
     * Get the current value of the timer.
     */
    public long getValue() {
        return (System.currentTimeMillis() / 10) - ticks;
    }
    /**
     * Retrieve the value as a String.               
     */
    public String toString() {
        return String.valueOf((System.currentTimeMillis() / 10) - ticks);
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        value: TimeTicks : " + ticks); 
    }
}

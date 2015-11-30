package com.mod_snmp.snmp.protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import java.util.Hashtable;

/**
 * LocalizedKeysTable
 * Due to its global character it must all be static.
 * This class generates SecModelException belonging to the package.
 */
public class LocalizedKeysTable {
    private final static boolean debug = false;
    public static Hashtable hash = new Hashtable();

    static public void insert(String u, String eId, LocalizedKeys lk)
                                        throws SecModelException {
        try {
            String key = u + eId;
            if (debug) dump("insert:" + key);
            if (hash.containsKey(key)) {
                hash.remove(key);
            }
            hash.put(key, lk);
        } catch (NullPointerException exception) {
        }
    }

    static public LocalizedKeys lookup(byte[] u, byte[] eId)
                                        throws SecModelException {
         return lookup(new String(u), new String(eId));
    }
    static public LocalizedKeys lookup(String u, String eId)
                                        throws SecModelException {
        try {
            String key = u + eId;
            if (debug) dump("lookup:" + key);
            if (hash.containsKey(key)) {     
                return (LocalizedKeys)hash.get(key);        
            }
        } catch (NullPointerException exception) {
        }
        throw new SecModelException(
                        "Localized key not found (" + u + "/" + eId
                        + ")");
    }

    static public boolean exists(String u, String eId)
                                        throws SecModelException {
        try {
            String key = u + eId;
            if (debug) dump("exists:" + key);
            return hash.containsKey(key);
        } catch (NullPointerException exception) {
        }
        throw new SecModelException("User " + u + " unknown");
    }
    /**
     * Debug dumper
     */
    private static void dump(String s) {
        System.out.println("LKeys: " + s);
    }
}

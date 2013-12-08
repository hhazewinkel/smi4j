package com.mod_snmp.Snmp.Values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import java.util.Vector;

/**
 * The object that does handle a SnmpObjectId.
 */
public class SnmpObjectId implements SnmpValue {
    private final static int DEFAULT_OID_LENGTH = 25;
    private final static Long max_oid = new Long("4294967295");
    private final static SnmpObjectId zeroDotZero = new SnmpObjectId(2, new long[2]);

    private long name[];
    private int length;

    /**
     * Default constructor that creates an ObjectId of 'DEFAULT_OID_LENGTH'.
     */
    public SnmpObjectId() {
        this(DEFAULT_OID_LENGTH);
    }
    /**
     * Constructor that creates an ObjectId of size length.
     */
    public SnmpObjectId(int size) {
        name = new long[size];
        length = 0;
    }
    /**
     * Constructor that creates an ObjectId of the array of longs.
     */
    public SnmpObjectId(long objid[]) throws SnmpValueException {
        this(objid, objid.length);
    }
    /**
     * Constructor that creates an ObjectId of the array of longs
     * that contains objid_l valid sub identifiers.
     */
    public SnmpObjectId(long objid[], int objid_l) throws SnmpValueException {
        if (objid.length < objid_l) {
            throw new SnmpValueException("indicated length longer then length of array");
        }
        name = objid;
        length = objid_l;
    }
    /**
     * This function equals the above, but is only for class internal use.
     * It neither does throw the SnmpValueException.
     */
    private SnmpObjectId(int l, long[]oid) {
        length  = l;
        name = oid;
    }
    /**
     * Constructor that creates an ObjectId of the vector 
     * that contains per element valid sub identifiers.
     */
    public SnmpObjectId(Vector objid) throws SnmpValueException {
        this(objid.size());
        for (int i = 0; i < objid.size(); i++) {
            name[i] = ((Long)objid.elementAt(i)).longValue();
            if (name[i] < 0) {
                throw new SnmpValueException("Object Id is negative");
            }
            if (name[i] > max_oid.longValue()) {
                throw new SnmpValueException("Object Id overflow ");
            }
        }
        length = objid.size();
    }
    /**
     * Retrieve the prefix value as a String.
     * The prefix is the first prefix_nr number of sub identifiers.
     */
    public String toString(int prefix_nr) {
        String str = new String("");
        if (prefix_nr > length) {
            prefix_nr = length;
        }
        if (prefix_nr > 0) {
            str += name[0];
            for (int i = 1; i < prefix_nr; i++) {
                str += "." + name[i];
            }
        }
        return str;
    }
    /**
     * Retrieve the value as a dotted number String.
     */
    public String toString() {
        String str = new String("");
        if (name.length > 0) {
            str += name[0];
            for (int i = 1; i < name.length; i++) {
                str += "." + name[i];
            }
        }
        return str;
    }
    /**
     * Concatenate an Object identifier.
     */
    public SnmpObjectId concat(SnmpObjectId suffix) {
        int i;
        int j;
        if (length + suffix.length < name.length) {
            long newname[] = new long[length+suffix.length];
            for (i = 0; i < length; i++) {
                newname[i] = name[i];
            }
            for (i = length, j = 0; j < suffix.length; i++, j++) {
                newname[j] = suffix.name[i];
            }
            name = newname;
        }
        for (i = length, j = 0; j < suffix.length; i++, j++) {
            name[j] = suffix.name[i];
        }
        length += suffix.length;
        return this;
    }
    /**
     * Test whether the object identifier starts with provided 'prefix'.
     */
    public boolean startsWith(SnmpObjectId prefix) {
        int max = (prefix.length < name.length) ? prefix.length : name.length;
        for (int i = 0; i < max ; i++) {
            if (name[i] != prefix.name[i]) {
                return false;
            }
        }
        return true;
    }
    /**
     * Retrieve the string of the suffix (rest of the object identifier)
     * starting from the prefix if the object identifier has the
     * prefix.
     */
    public String getSuffix(SnmpObjectId prefix) {
        int max = (prefix.length < name.length) ? prefix.length : name.length;
        String str = new String("");
        if (startsWith(prefix)) {
            if (name.length > max) {
                str += name[max];
                for (int i = max+1; i < name.length; i++) {
                    str += "." + name[i];
                }
            }
        }
        return str;
    }
    /**
     * Retrieve the length of the object ideintifier.
     * This is the number of valid sub identifiers.
     */
    public int getLength() {
        return length;
    }
    /**
     * Retrieve the sub identifier at position.
     */
    public long getOidAt(int i) {
        return name[i];
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        value: Object Identifier: " + toString()); 
    }
}

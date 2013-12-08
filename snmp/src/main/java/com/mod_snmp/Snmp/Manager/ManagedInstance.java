package com.mod_snmp.Snmp.Manager;

import com.mod_snmp.Snmp.Values.SnmpCounter32;
import com.mod_snmp.Snmp.Values.SnmpCounter64;
import com.mod_snmp.Snmp.Values.SnmpInteger;
import com.mod_snmp.Snmp.Values.SnmpIpAddress;
import com.mod_snmp.Snmp.Values.SnmpNsap;
import com.mod_snmp.Snmp.Values.SnmpObjectId;
import com.mod_snmp.Snmp.Values.SnmpOctetStr;
import com.mod_snmp.Snmp.Values.SnmpOpaque;
import com.mod_snmp.Snmp.Values.SnmpTimeTicks;
import com.mod_snmp.Snmp.Values.SnmpUnsigned32;
import com.mod_snmp.Snmp.Values.SnmpValue;
import com.mod_snmp.Snmp.Values.SnmpValueException;
import com.mod_snmp.Snmp.Values.Varbind;

/**
 * The ManagedInstance class represent the name/value pair of 
 * of an instance retrieved from an SNMP agent.
 * The class should be a bridge between a textual representation of 
 * management data and the varbind.
 * The variables used are strings oriented. These are not handy to use
 * in case one wants to make calculations with the retrieved information.
 * Possible, but type conversions would drop performance and in that case
 * it is suggested to use the data types of the com.mod_snmp.Snmp.Value
 * package.
 */
public class ManagedInstance {
    private final String[] types = { "-i",    // INTEGER
                                   "-s",      // OCTET STRING
                                   "-ip",     // IP address
                                    "-c",     // Counter32
                                    "-c32",   // Counter32
                                    "-u",     // Unsigned32
                                    "-t",     // TimeTicks
                                    "-o",     // Opaque
                                    "-nsap",  // NSAP address historic
                                    "-c64" }; // Counter64

    /**
     * The full name (object identifier).
     */
    public String name;
    /**
     * The type of the value.
     */
    public String type;
    /**
     * The full value.
     */
    public String value;
    
    public SnmpObjectId vbName;
    public SnmpValue vbValue;
    /**
     * Constructor from a name value pair.
     * @param n The name/object identifier (in String format).
     */
    public ManagedInstance(String n) throws SnmpValueException {
        name = n;
        vbName = createName(n);
    }
    /**
     * Constructor from a name value pair.
     * @param n The name/object identifier (in String format).
     * @param v The value (in String format).
     */
    public ManagedInstance(String n, String t, String v) throws SnmpValueException {
        this(n);
        addValue(t, v);
    }
    /**
     * Add a value to an managed instance
     * @param t The name/object identifier (in String format).
     * @param v The value (in String format).
     */
    public void addValue(String t, String v) throws SnmpValueException {
        type = t;
        value = v;
        vbValue  = createValue(type, value);
    }
    /**
     * Constructor from a variable binding.
     * @param vb The varbind.
     */
    public ManagedInstance(Varbind vb) {
        vbName = vb.name;
        name = vb.name.toString();
        vbValue = vb.value;
        value = vb.value.toString();
    }
    public String toString() {
        return name + ":" + value;
    }
    public String toNumberedString() {
        return vbName + ":" + value;
    }
    public Varbind createVarbind() throws SnmpValueException {
        if (vbValue != null) {
            return new Varbind(vbName, vbValue);
        }
        return new Varbind(vbName);
    }
    /**
     * Determine the 'string-typed' type of the value
     * @param v The value.
     */
    public String getValueType(SnmpValue v) throws SnmpValueException {
        if (v instanceof SnmpInteger) {
            return "INT32";
        } else if (v instanceof SnmpOctetStr) {
            return "OCT-STR";
        } else if (v instanceof SnmpIpAddress) {
            return "IP-ADDR";
        } else if (v instanceof SnmpCounter32) {
            return "COUNTER32";
        } else if (v instanceof SnmpUnsigned32) {
            return "UINT32";
        } else if (v instanceof SnmpTimeTicks) {
            return "TIMETICKS";
        } else if (v instanceof SnmpOpaque) {
            return "OPAQUE";
        } else if (v instanceof SnmpNsap) {
            return "NSAP";
        } else if (v instanceof SnmpCounter64) {
            return "COUNTER64";
        }
        throw new SnmpValueException("Unknown type");
    }
    /**
     * string-based value to SnmpValue translator.
     * @param t The type of the value.
     * @param v The value.
     */
    public SnmpValue createValue(String t, String v)
                             throws SnmpValueException {
        if (t.equals("-i")) {
            return new SnmpInteger(v);
        } else if (t.equals("-s")) {
            return new SnmpOctetStr(v);
        } else if (t.equals("-i")) {
            return new SnmpIpAddress(v);
        } else if ((t.equals("-c")) || (t.equals("-c32"))) {
            return new SnmpCounter32(v);
        } else if (t.equals("-u")) {
            return new SnmpUnsigned32(v);
        } else if (t.equals("-t")) {
            return new SnmpTimeTicks(v);
        } else if (t.equals("-o")) {
            return new SnmpOpaque(v);
        } else if (t.equals("-nsap")) {
            return new SnmpNsap(v);
        } else if (t.equals("-c64")) {
            return new SnmpCounter64(v);
        }
        throw new SnmpValueException("Not supported type");
    }
    /**
     * Constructor that creates an ObjectId of the value of string.
     * This function should be fixed with respect to name translations
     * done via the MIB modules
     */
    public SnmpObjectId createName(String objid) throws SnmpValueException {
        int count = 0;
        int dot = 0;
        while (dot >= 0) {
           dot = objid.indexOf('.', dot+1);
           count++;
        }
        int length = 0;
        long[] name = new long[count];
        char[] buf = objid.toCharArray();
        int oid_index = 0;
        int start = 0;
        int end = 1;
        if (buf[0] == '.') {
            start = 1;
        }
        try {
            while (end < buf.length) {
                if (buf[ end ] == '.') {
                    name[ oid_index++ ] = Long.parseLong(objid.substring(start, end));
                    start = end + 1;
                }
                end++;
            }
            name[ oid_index ] = Long.parseLong(objid.substring(start, end));
            length = oid_index + 1;
        } catch (NumberFormatException e) {
            throw new SnmpValueException(e.getMessage());
        }
        return new SnmpObjectId(name, length);
    }
}

package com.mod_snmp.Snmp.Values;

/**
 * The object composing a variable binding from an object identifier name 
 * and value.
 * This object is useful, since it is also the API of a managed 
 * instances. A managed instance it a name/value pair.
 * The name identifies the object and may have a potential index.
 */
public class Varbind {
    static final private SnmpNull novalue = new SnmpNull();
    public SnmpObjectId name;
    public SnmpValue value;


    public Varbind() {
        name = null;
        value = new SnmpNull();
    }
    public Varbind(SnmpObjectId id) {
        name = id;
        value = new SnmpNull();
    }
    public Varbind(SnmpObjectId id, SnmpValue val) {
        name = id;
        value = val;
    }
    public Varbind(SnmpObjectId id, int tp, SnmpValue val) {
        name = id;
        value = val;
    }
    /**
     * Is the value and exception.
     * @return true is is exception.
     */
    public boolean isException() {
        return (value instanceof SnmpNull);
    }
    public void setName(SnmpObjectId id) {
        name = id;
    }
    public void setValue(SnmpValue v) {
        value = v;
    }
    public String toString() {
        return name.toString() + ": " + value.toString();
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("      varbind");
        System.out.println("        name : " + name.toString());
        value.dump();
    }
}

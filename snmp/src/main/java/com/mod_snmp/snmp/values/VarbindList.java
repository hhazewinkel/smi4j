package com.mod_snmp.snmp.values;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import java.util.Enumeration;
import java.util.Vector;

/**
 * The list of variable bindings.
 */
public class VarbindList extends Vector {

    public void addVarbindList(VarbindList lst) {
        Enumeration e = varbinds();
        while (e.hasMoreElements()) {
            addVarbind((Varbind)e.nextElement());
        }
    }
    public void addVarbind(Varbind vb) {
        add(vb);
    }
    public Varbind varbindAt(int pos) {
        return (Varbind)elementAt(pos);
    }
    public Enumeration varbinds() {
        return elements();
    }
    public Varbind firstVarbind() {
        return (Varbind)firstElement();
    }
    public void insertVarbindAt(Varbind vb, int pos) {
        insertElementAt(vb, pos);
    }
    public void removeVarbind(int pos) {
        remove(pos);
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("    Varbind list       : " + size() + " varbinds");
        Enumeration e = varbinds();
        while (e.hasMoreElements()) {
            ((Varbind)e.nextElement()).dump();
        }
    }
}

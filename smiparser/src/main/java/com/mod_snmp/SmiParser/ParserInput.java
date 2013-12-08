package com.mod_snmp.SmiParser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import java.util.Enumeration;
import java.util.Vector;
/**
 */
public class ParserInput {
    static final boolean debug = true;
    private Vector modules;
    private String mibdirectory;

    /**
     * 
     */
    public ParserInput(ParserConf conf) {
        mibdirectory = conf.getMibDirectory();
        modules = conf.getMibModules(new Vector());
    }
    /**
     * set the MIB module directrory.
     */
    public void setMibDirectory(String m) {
        mibdirectory = m;
    }
    /**
     * get the MIB module directrory.
     */
    public String getMibDirectory() {
        return mibdirectory;
    }
    public void addMibModule(String m) {
        modules.add(m);
    }
    public Enumeration getModules() {
        return modules.elements();
    }
    /**
     * Creator of list of file names.
     */
    public Enumeration getFiles() {
        Vector v = new Vector(modules.size());
        Enumeration e = getModules();
        String d = getMibDirectory();
        while (e.hasMoreElements()) {
            v.add(d + "/" + e.nextElement());
        }
        return v.elements();
    }
}

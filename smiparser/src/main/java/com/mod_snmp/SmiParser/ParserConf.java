package com.mod_snmp.SmiParser;

import java.util.Vector;

/**
 * the parser configuration.
 */
public class ParserConf {
    public ParserConf() {
    }
    
    private String filename;

    private String mibdirectory = ".";
    /**
     * get the MIB module directrory.
     */
    public String getMibDirectory() {
        return mibdirectory;
    }
    /**
     * set the MIB module directory.
     */
    public void setMibDirectory(String dir) {
        mibdirectory = dir;
    }

    private String[] mibmodules = new String[0];
    /**
     * Get the MIB modules.
     */
    public String[] getMibModules() {
            return mibmodules;
    }
    /**
     * Set the MIB modules.
     */
    public void setMibModules(String[] mod) {
        mibmodules = mod;
    }
    public Vector getMibModules(Vector v) {
        for (int i = 0; i < mibmodules.length; i++) {
            v.add(mibmodules[i]);
        }
        return v;
    }
    public String toString() {
        String str = "dir: " + mibdirectory;
        for (int i = 0; i < mibmodules.length; i++) {
            str += "\nmod: " + mibmodules[i];
        }
        return str;
    }
}

package com.mod_snmp.smiparser.semantics;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.smiparser.errorhandler.CriticalException;
import com.mod_snmp.smiparser.errorhandler.ErrorException;
import com.mod_snmp.smiparser.errorhandler.SmiException;
import com.mod_snmp.smiparser.syntaxtree.ModuleIdentifier;

import java.util.Hashtable;

/**
 * Must be an overall (global/singleton pattern) hash table for MIB module's.
 * The MIB module information stored is done n the Module class.
 * Due to its global character it all functions must be static.
 */
public class ModuleHashTable {
    static public Hashtable hash = new Hashtable();

    static public void insert(String key, SymbolHashTable table)
                                                throws SmiException {
        try {
            if (hash.containsKey(key)) {
                throw new ErrorException("existing key \"" + key + "\"");
            } else {
                hash.put(key, table);
            }
        } catch (NullPointerException e) {
            throw new CriticalException("invalid key \"null\"");
        }
    }
    static public void insert(ModuleIdentifier key, SymbolHashTable table)
                                                throws SmiException {
        insert(key.toString(), table);
    }
    static public SymbolHashTable lookup(String key)
                                                throws SmiException {
        try {
            if (hash.containsKey(key)) {
                return (SymbolHashTable)hash.get(key);
            } else {
                throw new ErrorException("key \"" + key + "\" does not exist");
            }
        } catch (NullPointerException e) {
            throw new CriticalException("invalid key \"null\"");
        }
    }
    static public SymbolHashTable lookup(ModuleIdentifier key)
                                                throws SmiException {
        return lookup(key.toString());
    }
    static public boolean exists(String key) {
        try {
            return hash.containsKey(key);
        } catch (NullPointerException e) {
            return false;
        }
    }
    static public boolean exists(ModuleIdentifier key) {
        return exists(key.toString());
    }
}

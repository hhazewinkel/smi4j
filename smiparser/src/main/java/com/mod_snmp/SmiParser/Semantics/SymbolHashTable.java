package com.mod_snmp.SmiParser.Semantics;

import com.mod_snmp.SmiParser.ErrorHandler.ErrorException;
import com.mod_snmp.SmiParser.SyntaxTree.Assignment;
import com.mod_snmp.SmiParser.SyntaxTree.Identifier;
import com.mod_snmp.SmiParser.SyntaxTree.ModuleDefinition;
import com.mod_snmp.SmiParser.SyntaxTree.ModuleIdentifier;
import com.mod_snmp.SmiParser.SyntaxTree.Node;
import com.mod_snmp.SmiParser.SyntaxTree.Type;

import java.util.Hashtable;

/**
 * An symboltable containing assignments.
 * This class generates ErrorException belonging to the package.
 */
public class SymbolHashTable extends Hashtable {

    /**
     * insert
     * inserts data associated with the key or
     *         throws ErrorException if key exists or is invalid.
     * catches the NullPointerException if (key == null)
     */ 
    public void insert(String key, Node obj)
                                                        throws ErrorException {
        try {
            if (containsKey(key)) {
                throw new ErrorException("exists");
            } else {
                put(key, obj);
            }
        } catch (NullPointerException e) {
            throw new ErrorException("invalid key \"null\"");
        }
    }
    public void insert(Identifier key, Type type)
                                                        throws ErrorException {
        insert(key.toString(), type);
    }
    public void insert(Identifier key, Assignment assignment)
                                                        throws ErrorException {
        insert(key.toString(), assignment);
    }
    public void insert(Identifier key, ModuleIdentifier moduleName)
                                                        throws ErrorException {
        insert(key.toString(), moduleName);
    }
    public void insert(ModuleIdentifier key, ModuleDefinition module)
                                                        throws ErrorException {
        insert(key.toString(), module);
    }
    public void replace(String key, Node obj) throws ErrorException {
        try {
            remove(key);
            put(key, obj);
        } catch (NullPointerException e) {
            throw new ErrorException("invalid key \"null\"");
        }
    }
    public void replace(Identifier key, Type type) throws ErrorException {
        replace(key.toString(), type);
    }
    /**
     * lookup
     * returns data associated with the key or
     *         throws ErrorException.
     * catches the NullPointerException if (key == null)
     */ 
    public Node lookup(String key) throws ErrorException {
        try {
            if (containsKey(key)) {
                return (Node)get(key);
            } else {
                throw new ErrorException("does not exist");
            }
        } catch (NullPointerException e) {
            throw new ErrorException("invalid key \"null\"");
        }
    }
    public Node lookup(Identifier key) throws ErrorException {
        return (Node)lookup(key.toString());
    }
    public ModuleDefinition lookup(ModuleIdentifier key) throws ErrorException {
        return (ModuleDefinition)lookup(key.toString());
    }
    /**
     * exists
     * returns whether the hash table contains a key.
     * catches the NullPointerException if (key == null).
     */
    public boolean exists(String key) {
        try {
            return containsKey(key);
        } catch (NullPointerException e) {
            return false;
        }
    }
    public boolean exists(Identifier key) {
        return exists(key.toString());
    }
    public boolean exists(ModuleIdentifier key) {
        return exists(key.toString());
    }
}

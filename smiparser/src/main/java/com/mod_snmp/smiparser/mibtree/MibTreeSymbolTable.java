package com.mod_snmp.smiparser.mibtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * MibTreeSymbolTable
 * Must be an overall (global/singleton pattern) hash table for MibTreeNode's.
 * Due to its global character it moust all be static.
 * This class generates MibTreeException belonging to the package.
 */


import com.mod_snmp.smiparser.syntaxtree.Identifier;

import java.util.Hashtable;

public class MibTreeSymbolTable extends Hashtable {
    private static boolean debug = false;

    public void insert(MibTreeNode node) throws MibTreeException {
        try {
            if (debug) dump("insert(MibTreeNode node)" + node.getName());
            insert(node.getName(), node);
        } catch (NullPointerException exception) {
            throw new MibTreeException("invalid key \"null\"");
        }
    }
    public void insert(String key, MibTreeNode node) throws MibTreeException {
        try {
            if (debug) dump("insert(String key, MibTreeNode node)" + key);
            if (containsKey(key)) {
                throw new MibTreeException("existing key \"" + key + "\"");
            } else {
                put(key, node);
            }
        } catch (NullPointerException exception) {
            throw new MibTreeException("invalid key \"null\"");
        }
    }
    public MibTreeNode lookup(String key) throws MibTreeException {
        try {
            if (debug) dump("lookup(String key)" + key);
            if (containsKey(key)) {
                return (MibTreeNode)get(key);
            } else {
                throw new MibTreeException("key \"" + key + "\" does not exist");
            }
        } catch (NullPointerException exception) {
            throw new MibTreeException("invalid key \"null\"");
        }
    }
    public MibTreeNode lookup(Identifier key) throws MibTreeException {
        return lookup(key.toString());
    }
    public MibTreeNode retrieve(String key) throws MibTreeException {
        try {
            if (debug) dump("retrieve(String key)" + key);
            if (containsKey(key)) {
                return (MibTreeNode)get(key);
            } else {
                MibTreeNode node = new MibTreeNode(key);
                put(key, node);
                return node;
            }
        } catch (NullPointerException exception) {
            throw new MibTreeException("invalid key \"null\"");
        }
    }
    public boolean exists(String key) {
        try {
            if (debug) dump("exists(String key)" + key);
            return containsKey(key);
        } catch (NullPointerException exception) {
            return false;
        }
    }
    public boolean exists(Identifier key) {
        return exists(key.toString());
    }
    private void dump(String s) {
        System.out.println("MTS:" + s);
    }
}

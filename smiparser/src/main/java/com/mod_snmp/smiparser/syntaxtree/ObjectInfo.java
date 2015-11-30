package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * ObjectInfo
 * Interface class for the detailed information of a MIB tree node.
 */

public interface ObjectInfo extends Node {
    public boolean isConceptualRow();
    public void setIndexLevel(int i);
    public int getIndexLevel();
}

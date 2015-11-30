package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * Type
 * Interface class for the detailed type information.
 */

public interface Type extends Node {
    public void addRestriction(Node n0);
    public boolean restrictionPresent();
    public void setGenericType(Type type);
    public Type getGenericType();
    public boolean isGenericType();
}

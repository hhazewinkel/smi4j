package com.mod_snmp.SmiParser.MibTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

public interface MibTreeObjectVisitor {
    public Object visit(MibTreeNode mtn, Object argu);
}

package com.mod_snmp.smiparser.mibtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

public interface MibTreeVisitor {
    public void visit(MibTreeNode mtn);
}

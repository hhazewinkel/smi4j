package com.mod_snmp.SmiParser.MibTree;

public interface MibTreeObjectVisitor {
    public Object visit(MibTreeNode mtn, Object argu);
}

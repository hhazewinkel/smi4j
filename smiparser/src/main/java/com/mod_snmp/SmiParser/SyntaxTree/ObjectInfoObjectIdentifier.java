package com.mod_snmp.SmiParser.SyntaxTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * </PRE>
 */
public class ObjectInfoObjectIdentifier implements ObjectInfo {
    private Node parent;
    private int level = 0;

    public ObjectInfoObjectIdentifier() {
    }
    public int line() {
        return parent.line();
    }
    public boolean isConceptualRow() {
        return false;
    }
    public void setIndexLevel(int i) {
    }
    public int getIndexLevel() {
        return 0;
    }
    public void accept(com.mod_snmp.SmiParser.Visitor.Visitor v) {
        v.visit(this);
    }
    public Object accept(com.mod_snmp.SmiParser.Visitor.ObjectVisitor v, Object argu) {
        return v.visit(this,argu);
    }
    public void setParent(Node n) { parent = n; }
    public Node getParent()       { return parent; }
}


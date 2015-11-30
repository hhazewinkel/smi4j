package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * JAVACODE
 */
public class MacroBody implements Node {
    private Node parent;
    public MacroBody f0;

    public int line() {
        return f0.line();
    }
    public void accept(com.mod_snmp.smiparser.visitor.Visitor v) {
       v.visit(this);
    }
    public Object accept(com.mod_snmp.smiparser.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this,argu);
    }
    public void setParent(Node n) { parent = n; }
    public Node getParent()       { return parent; }
}


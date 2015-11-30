package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * ValueType
 * Class used to combine a ValueIdentifier with a Types.
 * Mostly used in a SEQUENCE definition.
 */

/**
 * Grammar production:
 * <PRE>
 * valueIdentifier -> ValueIdentifier()
 * type -> Types()
 * </PRE>
 */
public class ValueType implements Node {
    private Node parent;
    public Identifier identifier;
    public Type type;

    public ValueType(Identifier n0, Type n1) {
        identifier = n0;
        if ( identifier != null ) identifier.setParent(this);
        type = n1;
        if ( type != null ) type.setParent(this);
    }
    public int line() {
        return identifier.line();
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


package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * TypeSequenceOf
 * Class holding the TypeSequenceOf identifier.
 */

/**
 * Grammar production:
 * <PRE>
 * -> &lt;SEQUENCE_OF_T&gt;
 * identifier -> TypeIdentifier()
 * </PRE>
 */
public class TypeSequenceOf implements Type {
    private Node parent;
    public TypeIdentifier identifier;

    public TypeSequenceOf(Identifier n0) {
        identifier = new TypeIdentifier(n0);
        if ( identifier != null ) identifier.setParent(this);
    }
    public void addRestriction(Node n0) {
        /* NO-OP, since a SEQUENCE OF does not have a restriction. */
    }
    public boolean restrictionPresent() {
        return false;
    }
    public String toString() {
        return identifier.toString();
    }
    public boolean equals(Identifier id) {
        String idstr = id.toString();
        return idstr.equals(identifier.toString());
    }
    public int line() {
        return identifier.line();
    }
    public void setGenericType(Type type) {
        /* Generic type is 'this'. */
    }
    public Type getGenericType() {
        return this;
    }
    public boolean isGenericType() {
        return true;
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


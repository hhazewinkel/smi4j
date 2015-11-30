package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * TypeSequence
 */

/**
 * Grammar production:
 * <PRE>
 * -> &lt;SEQUENCE_T&gt;
 *    &lt;L_BRACE_T&gt;
 *    ValueType() ( &lt;COMMA_T&gt; ValueType() )*
 *    &lt;R_BRACE_T&gt;
 * vtList -> ( ValueType() )*
 * </PRE>
 */
public class TypeSequence implements Type {
    private Node parent;
    public NodeList vtList;

    public TypeSequence(NodeList n0) {
        vtList = n0;
        if ( vtList != null ) vtList.setParent(this);
    }
    public void addRestriction(Node n0) {
        /* NO-OP, since it may not have a restriction. */
    }
    public boolean restrictionPresent() {
        return false;
    }
    public int line() {
        return vtList.line();
    }
    public void setGenericType(Type type) {
        /* The generic type is 'this'. */
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


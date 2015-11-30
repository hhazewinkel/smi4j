package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * -> &lt;AUGMENTS_T&gt;
 * -> &lt;LEFT_BRACE_T&gt;
 * identifier -> Identifier()
 * -> &lt;RIGHT_BRACE_T&gt; )
 * </PRE>
 */
public class IndexAugments implements IndexPart {
    private Node parent;
    public Identifier identifier;

    public IndexAugments(Identifier n0) {
        identifier = n0;
        if ( identifier != null ) identifier.setParent(this);
    }
    public String toString() {
        return identifier.toString();
    }
    public int line() {
        return identifier.line();
    }
    public boolean present() {
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


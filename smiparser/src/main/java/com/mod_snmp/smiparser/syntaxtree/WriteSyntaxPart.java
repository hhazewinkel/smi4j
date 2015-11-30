package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Grammar production:
 * <PRE>
 * -> [ &lt;SYNTAX_T&gt; Types()
 * type -> Types()
 * </PRE>
 */
public class WriteSyntaxPart implements Node {
    private Node parent;
    public Type type;

    public WriteSyntaxPart(Type n0) {
        type = n0;
        if ( type != null ) type.setParent(this);
    }
    public int line() {
        return type.line();
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


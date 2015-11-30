package com.mod_snmp.SmiParser.SyntaxTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * -> &lt;LEFT_BRACE_T&gt;
 * oidValue -> OidValue() ( OidValue() )+
 * -> &lt;RIGHT_BRACE_T&gt;
 * </PRE>
 */
public class AssignedValue implements Node {
    private Node parent;
    public NodeList oidValue;

    public AssignedValue(NodeList n0) {
        oidValue = n0;
        if ( oidValue != null ) oidValue.setParent(this);
    }
    public int line() {
        return oidValue.line();
    }
    public String toString() {
        return oidValue.toString();
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


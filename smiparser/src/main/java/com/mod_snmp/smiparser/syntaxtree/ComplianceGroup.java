package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * <PRE>
 * Grammar production: &lt;GROUP_T&gt; ValueIdentifier() DescriptionPart()
 * identifier -> ValueIdentifier()
 * description -> DescriptionPart()
 * </PRE>
 */
public class ComplianceGroup implements CompliancePart {
    private Node parent;
    public Identifier identifier;
    public DescriptionPart description;

    public ComplianceGroup(Identifier n0, DescriptionPart n1) {
        identifier = n0;
        if ( identifier != null ) identifier.setParent(this);
        description = n1;
        if ( description != null ) description.setParent(this);
    }
    public int line() {
        return identifier.line();
    }
    public String toString() {
        return identifier.toString();
    }
    public Identifier getIdentifier() {
        return identifier;
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


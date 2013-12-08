package com.mod_snmp.SmiParser.SyntaxTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * -> &lt;TEXTUAL_CONVENTION_T&gt;
 * displayhint -> DisplayHintPart();
 * status -> StatusPart();
 * description -> DescriptionPart();
 * reference -> ReferencePart();
 * syntax -> SyntaxPart();
 * </PRE>
 */
public class TypeTextualConvention implements Type {
    private Node parent;
    public DisplayHintPart displayhint;
    public StatusPart status;
    public DescriptionPart description;
    public ReferencePart reference;
    public SyntaxPart syntax;

    public TypeTextualConvention(DisplayHintPart n1, StatusPart n2, DescriptionPart n3, ReferencePart n4, SyntaxPart n5) {
        displayhint = n1;
        if ( displayhint != null ) displayhint.setParent(this);
        status = n2;
        if ( status != null ) status.setParent(this);
        description = n3;
        if ( description != null ) description.setParent(this);
        reference = n4;
        if ( reference != null ) reference.setParent(this);
        syntax = n5;
        if ( syntax != null ) syntax.setParent(this);
    }
    public int line() {
        return displayhint.line();
    }   
    public void addRestriction(Node n0) {
        /* NO-OP, since it may not have a restriction. */
    }
    public boolean restrictionPresent() {
        return syntax.restrictionPresent();
    }
    public void setGenericType(Type type) {
        syntax.setGenericType(type);
    }
    public Type getGenericType() {
        return syntax.getGenericType();
    }
    public boolean isGenericType() {
        return syntax.isGenericType();
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


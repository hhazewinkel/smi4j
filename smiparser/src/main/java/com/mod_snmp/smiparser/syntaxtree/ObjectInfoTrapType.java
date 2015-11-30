package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * enterprisePart -> EnterprisePart()
 * variablesPart -> VariablesPart()
 * descriptionPart -> DescriptionPart()
 * number -> NumericValue()
 * </PRE>
 */
public class ObjectInfoTrapType implements ObjectInfo {
    private Node parent;
    private int level = 0;
    public EnterprisePart enterprisePart;
    public NodeList variablesPart;
    public DescriptionPart descriptionPart;
    public NumericValue number;

    public ObjectInfoTrapType(EnterprisePart n1,
                                                NodeList n2,
                                                DescriptionPart n3,
                                                NumericValue n4) {
        enterprisePart = n1;
        if ( enterprisePart != null ) enterprisePart.setParent(this);
        variablesPart = n2;
        if ( variablesPart != null ) variablesPart.setParent(this);
        descriptionPart = n3;
        if ( descriptionPart != null ) descriptionPart.setParent(this);
        number = n4;
        if ( number != null ) number.setParent(this);
    }
    public int line() {
        return enterprisePart.line();
    }
    public boolean isConceptualRow() {
        return false;
    }
    public void setIndexLevel(int i) {
    }
    public int getIndexLevel() {
        return 0;
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


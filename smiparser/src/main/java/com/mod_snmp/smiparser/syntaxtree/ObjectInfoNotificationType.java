package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * objectsPart -> ObjectsPart()
 * statusPart -> StatusPart()
 * descriptionPart -> DescriptionPart()
 * referencePart -> ReferencePart()
 * </PRE>
 */
public class ObjectInfoNotificationType implements ObjectInfo {
    private Node parent;
    private int level = 0;
    public NodeList objectsPart;
    public StatusPart statusPart;
    public DescriptionPart descriptionPart;
    public ReferencePart referencePart;

    public ObjectInfoNotificationType(NodeList n1,
                                                StatusPart n2,
                                                DescriptionPart n3,
                                                ReferencePart n4) {
        objectsPart = n1;
        if ( objectsPart != null ) objectsPart.setParent(this);
        statusPart = n2;
        if ( statusPart != null ) statusPart.setParent(this);
        descriptionPart = n3;
        if ( descriptionPart != null ) descriptionPart.setParent(this);
        referencePart = n4;
        if ( referencePart != null ) referencePart.setParent(this);
    }
    public int line() {
        return objectsPart.line();
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


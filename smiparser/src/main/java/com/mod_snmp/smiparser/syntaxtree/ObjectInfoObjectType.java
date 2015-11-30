package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * syntaxPart -> SyntaxPart()
 * unitsPart -> UnitsPart()
 * max_access -> AccessPart()
 * statusPart -> StatusPart()
 * descriptionPart -> DescriptionPart()
 * referencePart -> ReferencePart()
 * indexPart -> IndexPart()
 * defValPart -> DefValPart()
 * </PRE>
 */
public class ObjectInfoObjectType implements ObjectInfo {
    private Node parent;
    private int level = 0;
    public SyntaxPart syntaxPart;
    public UnitsPart unitsPart;
    public AccessPart max_access;
    public StatusPart statusPart;
    public DescriptionPart descriptionPart;
    public ReferencePart referencePart;
    public IndexPart indexPart;
    public DefValPart defValPart;

    public ObjectInfoObjectType(SyntaxPart n1,
                                                UnitsPart n2,
                                                AccessPart n3,
                                                StatusPart n4,
                                                DescriptionPart n5,
                                                ReferencePart n6,
                                                IndexPart n7,
                                                DefValPart n8) {
        syntaxPart = n1;
        if ( syntaxPart != null ) syntaxPart.setParent(this);
        unitsPart = n2;
        if ( unitsPart != null ) unitsPart.setParent(this);
        max_access = n3;
        if ( max_access != null ) max_access.setParent(this);
        statusPart = n4;
        if ( statusPart != null ) statusPart.setParent(this);
        descriptionPart = n5;
        if ( descriptionPart != null ) descriptionPart.setParent(this);
        referencePart = n6;
        if ( referencePart != null ) referencePart.setParent(this);
        indexPart = n7;
        if ( indexPart != null ) indexPart.setParent(this);
        defValPart = n8;
        if ( defValPart != null ) defValPart.setParent(this);
    }
    public int line() {
        return syntaxPart.line();
    }
    public boolean isConceptualRow() {
        return indexPart.present();
    }
    public void setIndexLevel(int i) {
        if (i > level) {
            level = i;
        }
    }
    public int getIndexLevel() {
        return level;
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


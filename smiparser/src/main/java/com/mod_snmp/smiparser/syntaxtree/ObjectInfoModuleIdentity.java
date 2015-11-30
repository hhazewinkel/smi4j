package com.mod_snmp.SmiParser.SyntaxTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * lastUpdatedPart -> LastUpdatedPart()
 * organizationPart -> OrganizationPart()
 * contactInfoPart -> ContactInfoPart()
 * descriptionPart -> DescriptionPart()
 * revisions -> ( Revisions() )*
 * </PRE>
 */
public class ObjectInfoModuleIdentity implements ObjectInfo {
    private Node parent;
    private int level = 0;
    public LastUpdatedPart lastUpdatedPart;
    public OrganizationPart organizationPart;
    public ContactInfoPart contactInfoPart;
    public DescriptionPart descriptionPart;
    public NodeList revisions;

    public ObjectInfoModuleIdentity(LastUpdatedPart n1,
                                                OrganizationPart n2,
                                                ContactInfoPart n3,
                                                DescriptionPart n4,
                                                NodeList n5) {
        lastUpdatedPart = n1;
        if ( lastUpdatedPart != null ) lastUpdatedPart.setParent(this);
        organizationPart = n2;
        if ( organizationPart != null ) organizationPart.setParent(this);
        contactInfoPart = n3;
        if ( contactInfoPart != null ) contactInfoPart.setParent(this);
        descriptionPart = n4;
        if ( descriptionPart != null ) descriptionPart.setParent(this);
        revisions = n5;
        if ( revisions != null ) revisions.setParent(this);
    }
    public int line() {
        return lastUpdatedPart.line();
    }
    public boolean isConceptualRow() {
        return false;
    }
    public void setIndexLevel(int i) {
    }
    public int getIndexLevel() {
        return 0;
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


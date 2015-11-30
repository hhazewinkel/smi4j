package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * TypeIdentifier
 * Mediator class between the TypeSmi and the Identifier.
 */

/**
 * Grammar production:
 * <PRE>
 * identifier -> &lt;TYPE_IDENTIFIER_T&gt;
 * restriction -> ( RestrictionRange()
 *                | RestrictionNamedNumberList()
 *                | RestrictionSize()
 *                )
 * </PRE>
 */
public class TypeIdentifier implements Type {
    private Node parent;
    public Identifier identifier;
    public NodeOptional restriction;
    public Type genericType;
    
    public TypeIdentifier() {
        identifier = Identifier.Type(new NodeToken(""));
        if ( identifier != null ) identifier.setParent(this);
        restriction = new NodeOptional();
        genericType = null;
    }
    public TypeIdentifier(Identifier n0) {
        identifier = n0;
        if ( identifier != null ) identifier.setParent(this);
        restriction = new NodeOptional();
        genericType = null;
    }
    public void addRestriction(Node n0) {
        restriction.addNode(n0);
    }
    public boolean restrictionPresent() {
        return restriction.present();
    }
    public String toString() {
        return identifier.toString();
    }
    public boolean equals(Identifier id) {
        String idstr = id.toString();
        return idstr.equals(identifier.toString());
    }
    public int line() {
        return identifier.line();
    }
    public void setGenericType(Type type) {
        genericType = type;
    }
    public Type getGenericType() {
        return genericType;
    }
    public boolean isGenericType() {
        return genericType != null;
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


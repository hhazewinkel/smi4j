
package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * Grammar production:
 * <PRE>
 * statusPart -> StatusPart()
 * descriptionPart -> DescriptionPart()
 * referencePart -> ReferencePart()
 * </PRE>
 */
public class ObjectInfoObjectIdentity implements ObjectInfo {
    private Node parent;
    private int level = 0;
    public StatusPart statusPart;
    public DescriptionPart descriptionPart;
    public ReferencePart referencePart;

    public ObjectInfoObjectIdentity(StatusPart n1,
                                                DescriptionPart n2,
                                                ReferencePart n3) {
        statusPart = n1;
        if ( statusPart != null ) statusPart.setParent(this);
        descriptionPart = n2;
        if ( descriptionPart != null ) descriptionPart.setParent(this);
        referencePart = n3;
        if ( referencePart != null ) referencePart.setParent(this);
    }
    public int line() {
        return statusPart.line();
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


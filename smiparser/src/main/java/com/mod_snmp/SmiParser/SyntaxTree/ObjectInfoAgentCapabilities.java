
package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * Grammar production:
 * <PRE>
 * productReleasePart -> ProductReleasePart()
 * statusPart -> StatusPart()
 * descriptionPart -> DescriptionPart()
 * referencePart -> ReferencePart()
 * moduleCapabilitiesPart -> ModuleCapabilitiesPart()
 * </PRE>
 */
public class ObjectInfoAgentCapabilities implements ObjectInfo {
    private Node parent;
    private int level = 0;
    public ProductReleasePart productReleasePart;
    public StatusPart statusPart;
    public DescriptionPart descriptionPart;
    public ReferencePart referencePart;
    public NodeList moduleCapabilitiesPart;

    public ObjectInfoAgentCapabilities(ProductReleasePart n1,
                                                StatusPart n2,
                                                DescriptionPart n3,
                                                ReferencePart n4,
                                                NodeList n5) {
        productReleasePart = n1;
        if ( productReleasePart != null ) productReleasePart.setParent(this);
        statusPart = n2;
        if ( statusPart != null ) statusPart.setParent(this);
        descriptionPart = n3;
        if ( descriptionPart != null ) descriptionPart.setParent(this);
        referencePart = n4;
        if ( referencePart != null ) referencePart.setParent(this);
        moduleCapabilitiesPart = n5;
        if ( moduleCapabilitiesPart != null ) moduleCapabilitiesPart.setParent(this);
    }
    public int line() {
        return productReleasePart.line();
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


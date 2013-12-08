package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * <PRE>
 * Grammar production: &lt;OBJECT_T&gt; ValueIdentifier()
 *                         [ SyntaxPart() ]
 *                         [ WriteSyntaxPart() ]
 *                         [ Access() ]
 *                         DescriptionPart()
 * identifier -> ValueIdentifier()
 * syntax -> SyntaxPart()
 * write_syntax -> Types()
 * min_access -> AccessPart()
 * description -> DescriptionPart()
 * </PRE>
 */
public class ComplianceObject implements CompliancePart {
    private Node parent;
    public Identifier identifier;
    public NodeOptional syntax;
    public NodeOptional write_syntax;
    public NodeOptional min_access;
    public DescriptionPart description;

    public ComplianceObject() {
        syntax = new NodeOptional();
        write_syntax = new NodeOptional();
        min_access = new NodeOptional();
    }
    public ComplianceObject(Identifier n0) {
        this();
        identifier = n0;
        if ( identifier != null ) identifier.setParent(this);
    }
    public ComplianceObject(Identifier n0,
                                        SyntaxPart n1,
                                        WriteSyntaxPart n2,
                                        AccessPart n3,
                                        DescriptionPart n4) {
        identifier = n0;
        if ( identifier != null ) identifier.setParent(this);
        syntax.addNode(n1);
        write_syntax.addNode(n2);
        min_access.addNode(n3);
        description = n4;
        if ( description != null ) description.setParent(this);
    }
    public void addNode(SyntaxPart n0) {
        syntax.addNode(n0);
    }
    public void addNode(WriteSyntaxPart n0) {
        write_syntax.addNode(n0);
    }
    public void addNode(AccessPart n0) {
        min_access.addNode(n0);
    }
    public void addNode(DescriptionPart n0) {
        description = n0;
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
    public void accept(com.mod_snmp.SmiParser.Visitor.Visitor v) {
        v.visit(this);
    }
    public Object accept(com.mod_snmp.SmiParser.Visitor.ObjectVisitor v, Object argu) {
        return v.visit(this,argu);
    }
    public void setParent(Node n) { parent = n; }
    public Node getParent()       { return parent; }
}


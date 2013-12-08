package com.mod_snmp.SmiParser.SyntaxTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * -> &lt;MODULE_T&gt;
 * module_id -> [ ModuleIdentifier() ]
 * mandatory_part -> [ MandatoryPart() ]
 * compliance_part -> ( CompliancePart() )* 
 * </PRE>
 */
public class ModuleCompliance implements Node {
    private Node parent;
    public NodeOptional module_id;
    public NodeOptional mandatory_part;
    public NodeList compliance_part;

    public ModuleCompliance() {
        module_id = new NodeOptional();
        mandatory_part = new NodeOptional();
        compliance_part = new NodeList();
    }

    public void addModuleIdentifier(ModuleIdentifier n0) {
        module_id.addNode(n0);
    }
    public void addMandatoryPart(NodeList n0) {
        mandatory_part.addNode(n0);
    }
    public void addCompliancePart(CompliancePart n0) {
        compliance_part.addNode(n0);
    }
    public int line() {
        return module_id.line();
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


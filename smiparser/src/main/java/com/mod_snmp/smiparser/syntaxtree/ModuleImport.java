package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * symbolList -> Symbol() ( &lt;COMMA_T&gt; Symbol() )*
 * -> &lt;FROM_T&gt;
 * moduleIdentifier -> ModuleIdentifier()
 * </PRE>
 */
public class ModuleImport implements Node {
    private Node parent;
    public NodeList symbolList;
    public ModuleIdentifier moduleIdentifier;

    public ModuleImport(NodeList n0, ModuleIdentifier n1) {
        symbolList = n0;
        if ( symbolList != null ) symbolList.setParent(this);
        moduleIdentifier = n1;
        if ( moduleIdentifier != null ) moduleIdentifier.setParent(this);
    }
    public int line() {
        return symbolList.line();
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


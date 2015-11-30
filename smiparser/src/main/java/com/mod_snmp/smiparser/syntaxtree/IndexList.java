package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Grammar production:
 * <PRE>
 * list -> Index() ( &lt;COMMA_T&gt; Index() )*
 * </PRE>
 */
public class IndexList implements IndexPart {
    private Node parent;
    public NodeList list;

    public IndexList() {
        list = new NodeList();
        if ( list != null ) list.setParent(this);
    }
    public IndexList(NodeList n0) {
        list = n0;
        if ( list != null ) list.setParent(this);
    }
    public int line() {
        return list.line();
    }
    public boolean present() {
        return list.present();
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


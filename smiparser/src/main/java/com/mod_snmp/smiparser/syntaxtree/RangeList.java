package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * Grammar production:
 * <PRE>
 * -> RangeItem() ( &lt;OR_T&gt; RangeItem() )*
 * list -> ( RangeItem() )+
 * </PRE>
 */
public class RangeList implements Node {
    private Node parent;
    public NodeList list;

    public RangeList() {
        list = new NodeList();
    }
    public RangeList(RangeItem n0) {
        this();
        list.addNode(n0);
    }
    public void addNode(RangeItem n0) {
        list.addNode(n0);
    }
    public int line() {
        return list.line();
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


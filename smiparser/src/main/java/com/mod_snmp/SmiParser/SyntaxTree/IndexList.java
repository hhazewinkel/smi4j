package com.mod_snmp.SmiParser.SyntaxTree;

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
    public void accept(com.mod_snmp.SmiParser.Visitor.Visitor v) {
        v.visit(this);
    }
    public Object accept(com.mod_snmp.SmiParser.Visitor.ObjectVisitor v, Object argu) {
        return v.visit(this,argu);
    }
    public void setParent(Node n) { parent = n; }
    public Node getParent()       { return parent; }
}


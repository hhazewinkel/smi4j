package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;LEFT_BRACE_T&gt;
 * list -> NamedNumber() ( &lt;COMMA_T&gt; NamedNumber() )*
 * nodeToken1 -> &lt;RIGHT_BRACE_T&gt;
 * </PRE>
 */
public class RestrictionNamedNumberList implements Restriction {
    private Node parent;
    public NodeList list;

    public RestrictionNamedNumberList(NodeList n0) {
        list = n0;
        if ( list != null ) list.setParent(this);
    }
    public int line() {
        return list.line();
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


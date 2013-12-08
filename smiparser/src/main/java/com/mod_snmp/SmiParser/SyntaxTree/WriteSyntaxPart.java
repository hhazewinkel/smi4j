package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * Grammar production:
 * <PRE>
 * -> [ &lt;SYNTAX_T&gt; Types()
 * type -> Types()
 * </PRE>
 */
public class WriteSyntaxPart implements Node {
    private Node parent;
    public Type type;

    public WriteSyntaxPart(Type n0) {
        type = n0;
        if ( type != null ) type.setParent(this);
    }
    public int line() {
        return type.line();
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


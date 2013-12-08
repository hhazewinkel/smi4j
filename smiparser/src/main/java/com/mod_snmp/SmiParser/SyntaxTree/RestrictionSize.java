
package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * Grammar production:
 * <PRE>
 * -> &lt;LEFT_PAREN_T&gt;
 * -> &lt;SIZE_T&gt;
 * -> &lt;LEFT_PAREN_T&gt;
 * range -> RangeList()
 * -> &lt;RIGHT_PAREN_T&gt;
 * -> &lt;RIGHT_PAREN_T&gt;
 * </PRE>
 */
public class RestrictionSize implements Restriction {
    private Node parent;
    public RangeList range;

    public RestrictionSize(RangeList n0) {
        range = n0;
        if ( range != null ) range.setParent(this);
    }
    public int line() {
        return range.line();
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


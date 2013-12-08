package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * Grammar production:
 * <PRE>
 * display_hint -> [ &lt;DISPLAY_HINT_T&gt; &lt;TEXT_T&gt; ]
 * </PRE>
 */
public class DisplayHintPart implements Node {
    private Node parent;
    public NodeOptional display_hint;

    public DisplayHintPart(NodeOptional n0) {
        display_hint = n0;
        if ( display_hint != null ) display_hint.setParent(this);
    }
    public boolean present() {
        return display_hint.present();
    }
    public int line() {
        return display_hint.line();
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


//
// Generated by JTB 1.2.2++
//

package com.mod_snmp.smiparser.syntaxtree;
import com.mod_snmp.smiparser.grammar.SmiGrammarConstants;

/**
 * Grammar production:
 * <PRE>
 * key -> ( &lt;ACCESS_T&gt; | &lt;MAX_ACCESS_T&gt; | &lt;MIN_ACCESS_T&gt; )
 * value -> ( &lt;NOT_ACCESSIBLE_T&gt; | &lt;ACCESSIBLE_FOR_NOTIFY_T&gt; | &lt;READ_ONLY_T&gt; | &lt;READ_WRITE_T&gt; | &lt;READ_CREATE_T&gt; | &lt;WRITE_ONLY_T&gt; | &lt;NOT_IMPLEMENTED_T&gt; )
 * </PRE>
 */
public class AccessPart implements Node {
    /* Access types */
    public final static int ACCESS = SmiGrammarConstants.ACCESS_T;
    public final static int MAX_ACCESS = SmiGrammarConstants.MAX_ACCESS_T;
    public final static int MIN_ACCESS = SmiGrammarConstants.MIN_ACCESS_T;
    /* Access values */
    public final static int NOT_ACCESSIBLE = SmiGrammarConstants.NOT_ACCESSIBLE_T;
    public final static int ACCESSIBLE_FOR_NOTIFY = SmiGrammarConstants.ACCESSIBLE_FOR_NOTIFY_T;
    public final static int READ_ONLY = SmiGrammarConstants.READ_ONLY_T;
    public final static int READ_WRITE = SmiGrammarConstants.READ_WRITE_T;
    public final static int READ_CREATE = SmiGrammarConstants.READ_CREATE_T;
    public final static int WRITE_ONLY = SmiGrammarConstants.WRITE_ONLY_T;
    public final static int NOT_IMPLEMENTED = SmiGrammarConstants.NOT_IMPLEMENTED_T;
    private Node parent;
    public NodeToken key;
    public NodeToken value;

    public AccessPart() {
        key = new NodeToken("ACCESS?");
        key.kind = ACCESS;
        value = new NodeToken("NOT-ACCESSIBLE");
        value.kind = NOT_ACCESSIBLE;
    }
    public AccessPart(NodeToken n0, NodeToken n1) {
        key = n0;
        if ( key != null ) key.setParent(this);
        value = n1;
        if ( value != null ) value.setParent(this);
    }
    public int line() {
        return key.beginLine;
    }
    public String toString() {
        return value.toString();
    }
    public int getAccessType() {
        return key.kind;
    }
    public int getAccessValue() {
        return value.kind;
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

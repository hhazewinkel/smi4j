
package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * Grammar production:
 * <PRE>
 * -> [ 
 *     -> &lt;LEFT_SQUARE_T&gt;
 *     sort -> &lt;APPLICATION_T&gt; | &lt;UNIVERSAL_T&gt; | &lt;PRIVATE_T&gt;
 *     number -> &lt;DEC_NUMBER_T&gt;
 *     -> &lt;RIGHT_SQUARE_T&gt;
 *     kind -> [ &lt;EXPLICIT_T&gt; | &lt;IMPLICIT_T&gt; ]
 *     ]
 * type -> Types() | Asn1Types()
 * </PRE>
 */
public class TypeTag implements Type {
    private Node parent;
    public NodeToken sort;
    public static int APPLICATION;
    public static int UNIVERSAL;
    public static int PRIVATE;
    public NumericValue number;
    public NodeToken kind;
    public static int IMPLICIT;
    public static int EXPLICIT;
    public TypeSmi type;

    public TypeTag() {
        sort = null;
        number = null;
        kind = new NodeToken("IMPLICIT");
        type = null;
    }
    public void setSort(NodeToken n0) {
        sort = n0;
        if (sort != null) { sort.setParent(this); }
    }
    public void setNumber(NumericValue n0) {
        number = n0;
        if (number != null) { number.setParent(this); }
    }
    public void setKind(NodeToken n0) {
        kind = n0;
        if (kind != null) { kind.setParent(this); }
    }
    public void setType(TypeSmi n0) {
        type = n0;
        if (type != null) { type.setParent(this); }
    }
    public void addRestriction(Node n0) {
        /* NO-OP, since the restriction is included in 'type'. */
    }
    public boolean restrictionPresent() {
        return false;
    }
    public int line() {
        return sort.line();
    }
    public void setGenericType(Type type) {
        /* The grammar prevents this from being not a generic type. */
    }
    public Type getGenericType() {
        return type.getGenericType();
    }
    public boolean isGenericType() {
        return true;
    }
    public boolean present() {
        return (sort != null) && (number != null);
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


package com.mod_snmp.SmiParser.SyntaxTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * Identifier
 * Class implementation as factory class.
 * The TypeIdentifier, ValueIdentifier, KeywordIdentifier create the object instance.
 */

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> ( ModuleIdentifier() &lt;DOT_T&gt; &lt;TYPE_IDENTIFIER_T&gt; | &lt;TYPE_IDENTIFIER_T&gt; )
 * nodeChoice -> ( ModuleIdentifier() &lt;DOT_T&gt; &lt;VALUE_IDENTIFIER_T&gt; | &lt;VALUE_IDENTIFIER_T&gt; )
 * </PRE>
 */
public class Identifier implements Node {
    private Node parent;
    public final static int MODULE = -3;
    public final static int TYPE = -2;
    public final static int VALUE = -1;
    public final static int KEYWORD = 0;
    private int kind;
    public ModuleIdentifier module_identifier;
    public NodeToken identifier;

    private Identifier(ModuleIdentifier n0, NodeToken n1) {
        module_identifier = n0;
        if ( module_identifier != null ) module_identifier.setParent(this);
        identifier = n1;
        if ( identifier != null ) identifier.setParent(this);
    }
    private Identifier(NodeToken n0) {
        identifier = n0;
        if ( identifier != null ) identifier.setParent(this);
    }
    /* The factory functions. */
    public static Identifier Keyword(NodeToken n0) {
        Identifier id = new Identifier(n0);
        id.kind = KEYWORD;
        return id;
    }
    public static Identifier Keyword(NodeToken n0, int key_kind) {
        Identifier id = new Identifier(n0);
        id.kind = key_kind;
        return id;
    }
    public static Identifier Value(ModuleIdentifier n0, NodeToken n1) {
        Identifier id = new Identifier(n0, n1);
        id.kind = VALUE;
        return id;
    }
    public static Identifier Value(NodeToken n0) {
        Identifier id = new Identifier(n0);
        id.kind = VALUE;
        return id;
    }
    public static Identifier Type(ModuleIdentifier n0, NodeToken n1) {
        Identifier id = new Identifier(n0, n1);
        id.kind = TYPE;
        return id;
    }
    public static Identifier Type(NodeToken n0) {
        Identifier id = new Identifier(n0);
        id.kind = TYPE;
        return id;
    }
    public String toString() {
        return identifier.toString();
    }
    public boolean equals(Identifier id) {
        String idstr = id.toString();
        return idstr.equals(identifier.toString());
    }
    public int line() {
        return identifier.beginLine;
    }
    public int getKind() {
        if (kind < 0) {
            return kind;
        }
        return identifier.kind;
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


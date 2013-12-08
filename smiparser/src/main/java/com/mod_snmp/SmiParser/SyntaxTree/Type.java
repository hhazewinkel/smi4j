/**
 * Type
 * Interface class for the detailed type information.
 */
package com.mod_snmp.SmiParser.SyntaxTree;

public interface Type extends Node {
    public void addRestriction(Node n0);
    public boolean restrictionPresent();
    public void setGenericType(Type type);
    public Type getGenericType();
    public boolean isGenericType();
}

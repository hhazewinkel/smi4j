/**
 * ObjectInfo
 * Interface class for the detailed information of a MIB tree node.
 */
package com.mod_snmp.SmiParser.SyntaxTree;

public interface ObjectInfo extends Node {
    public boolean isConceptualRow();
    public void setIndexLevel(int i);
    public int getIndexLevel();
}

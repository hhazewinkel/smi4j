package com.mod_snmp.SmiParser.SyntaxTree;

/**
 * The interface which all CompliancePart classes must implement.
 * -> ComplianceGroup() | ComplianceObject()
 */
public interface CompliancePart extends Node {
    public Identifier getIdentifier();
}


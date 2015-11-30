package com.mod_snmp.smiparser.syntaxtree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * The interface which all CompliancePart classes must implement.
 * -> ComplianceGroup() | ComplianceObject()
 */
public interface CompliancePart extends Node {
    public Identifier getIdentifier();
}


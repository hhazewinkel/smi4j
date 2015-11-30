package com.mod_snmp.SmiParser.Semantics;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * Semantics
 * Class that combines the SemanticsSetup and SemanticsCheck
 * classes into a single static class.
 */
import com.mod_snmp.SmiParser.SyntaxTree.NodeList;

public class Semantics {

    public static void test(NodeList mdl) {
        mdl.accept(new SemanticsSetup());
        mdl.accept(new SemanticsCheck());
    }
}

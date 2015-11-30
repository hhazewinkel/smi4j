package com.mod_snmp.smiparser.semantics;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * Semantics
 * Class that combines the SemanticsSetup and SemanticsCheck
 * classes into a single static class.
 */
import com.mod_snmp.smiparser.syntaxtree.NodeList;

public class Semantics {

    public static void test(NodeList mdl) {
        mdl.accept(new SemanticsSetup());
        mdl.accept(new SemanticsCheck());
    }
}

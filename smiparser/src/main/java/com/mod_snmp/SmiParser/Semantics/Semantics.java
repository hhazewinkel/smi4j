/**
 * Semantics
 * Class that combines the SemanticsSetup and SemanticsCheck
 * classes into a single static class.
 */
package com.mod_snmp.SmiParser.Semantics;
import com.mod_snmp.SmiParser.SyntaxTree.NodeList;

public class Semantics {

    public static void test(NodeList mdl) {
        mdl.accept(new SemanticsSetup());
        mdl.accept(new SemanticsCheck());
    }
}

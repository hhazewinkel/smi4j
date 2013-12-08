package com.mod_snmp.SmiTools.Generators.Tree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.SmiParser.SyntaxTree.NodeList;
import com.mod_snmp.SmiTools.Generators.Common.GeneratorInterface;
import com.mod_snmp.SmiTools.Generators.Generator;
import net.lisanza.CliArgs.ArgsException;

/**
 * Handler for the CLI options that are added for tasks.
 */
public class TreeGenerator implements GeneratorInterface {
    private Generator caller;

    public TreeGenerator() {
    }
    public TreeGenerator(Generator top) throws ArgsException {
        caller = top;
    }
    /**
     * HTML argument parser.
     */
    public int parse(String args[], int idx) throws ArgsException {
        if (caller.generator == null) {
            throw new ArgsException("Has already a generator");
        }
        caller.generator = this;
        idx++;
        return idx;
    }
    public String argument() {
        return "-tree";
    }
    public String usage() {
        return "";
    }
    /**
     * The generator.
     */
    public void generate(NodeList mdl) {
    }
}

package com.mod_snmp.smitools.generators.tree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.smiparser.syntaxtree.NodeList;
import com.mod_snmp.smitools.generators.common.GeneratorInterface;
import com.mod_snmp.smitools.generators.Generator;
import net.lisanza.cliargs.ArgsException;

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

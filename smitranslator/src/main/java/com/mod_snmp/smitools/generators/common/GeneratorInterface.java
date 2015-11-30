package com.mod_snmp.smitools.generators.common;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.smiparser.syntaxtree.NodeList;
import net.lisanza.CliArgs.ArgsHandlerInterface;

/**
 *
 */
public interface GeneratorInterface extends ArgsHandlerInterface {
    public void generate(NodeList mdl);
}

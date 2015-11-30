package com.mod_snmp.SmiTools.Generators.Common;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.SmiParser.SyntaxTree.NodeList;
import net.lisanza.CliArgs.ArgsHandlerInterface;

/**
 *
 */
public interface GeneratorInterface extends ArgsHandlerInterface {
    public void generate(NodeList mdl);
}

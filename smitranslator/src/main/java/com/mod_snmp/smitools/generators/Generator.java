package com.mod_snmp.smitools.generators;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.smiparser.ParserInputArgs;
import com.mod_snmp.smitools.generators.common.GeneratorInterface;
import com.mod_snmp.smitools.generators.html.HtmlGenerator;
import com.mod_snmp.smitools.generators.manappl.ManApplGenerator;
import com.mod_snmp.smitools.generators.tree.TreeGenerator;
import net.lisanza.CliArgs.ArgsException;


public class Generator extends ParserInputArgs {
    public GeneratorInterface generator;

    public Generator(GeneratorConf conf) throws ArgsException {
        super();
        generator = null;
        
        addHandler(new HtmlGenerator(this));
        addHandler(new ManApplGenerator(this));
        addHandler(new TreeGenerator(this));
    }

    public static void main(String[] args) {
        try {
            //Parser parser = new Parser();
            Generator input = new Generator(GeneratorConf.getDefaults());
            //input.parse(args);
            //parser.parseFiles(input.getFiles());
            //input.generator.generate(parser.moduleList);
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
        //} catch (SmiException e) {
            //e.print();
        }
    }
}

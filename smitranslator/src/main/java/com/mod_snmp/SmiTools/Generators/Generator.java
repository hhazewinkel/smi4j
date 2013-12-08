package com.mod_snmp.SmiTools.Generators;

import com.mod_snmp.SmiParser.ParserInputArgs;
import com.mod_snmp.SmiTools.Generators.Common.GeneratorInterface;
import com.mod_snmp.SmiTools.Generators.Html.HtmlGenerator;
import com.mod_snmp.SmiTools.Generators.ManAppl.ManApplGenerator;
import com.mod_snmp.SmiTools.Generators.Tree.TreeGenerator;
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

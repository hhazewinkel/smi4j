package com.mod_snmp.SmiTools.Generators.Html;

import com.mod_snmp.SmiParser.SyntaxTree.ModuleDefinition;
import com.mod_snmp.SmiParser.SyntaxTree.NodeList;
import com.mod_snmp.SmiTools.Generators.Common.GeneratorInterface;
import com.mod_snmp.SmiTools.Generators.Generator;
import net.lisanza.CliArgs.ArgsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Enumeration;

/**
 * Handler for the CLI options that are added for tasks.
 */
public class HtmlGenerator implements GeneratorInterface {
    private Generator caller;
    private String pkgName;
    private String fileName;

    public HtmlGenerator() {
    }
    public HtmlGenerator(Generator top) throws ArgsException {
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
        pkgName = args[idx++];
	if ((idx < args.length) && (args[idx].charAt(0) != '-')) {
             fileName = args[idx++];
        }
        return idx;
    }
    public String argument() {
        return "-html";
    }
    public String usage() {
        return "<html-output-directory> [ <html-output-file-name> ]";
    }
    /**
     * The generator.
     */
    public void generate(NodeList mdl) {
        File dir = new File(pkgName);
        dir.mkdirs();
        for ( Enumeration e = mdl.elements(); e.hasMoreElements(); ) {
            ModuleDefinition md = (ModuleDefinition)e.nextElement();
            try {
                FileOutputStream fo = new FileOutputStream(
                                pkgName + "/" + md.moduleIdentifier.toString());
                new HtmlPrinter(pkgName, md);
            } catch (FileNotFoundException exception) {
            } catch (NullPointerException exception) {
            }
        }
    }
}

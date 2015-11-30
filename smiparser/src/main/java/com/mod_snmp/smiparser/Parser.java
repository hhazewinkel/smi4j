package com.mod_snmp.smiparser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.smiparser.errorhandler.MessageList;
import com.mod_snmp.smiparser.errorhandler.SmiException;
import com.mod_snmp.smiparser.grammar.ParseException;
import com.mod_snmp.smiparser.grammar.SmiGrammar;
import com.mod_snmp.smiparser.mibtree.MibTree;
import com.mod_snmp.smiparser.semantics.SemanticsCheck;
import com.mod_snmp.smiparser.semantics.SemanticsSetup;
import com.mod_snmp.smiparser.syntaxtree.NodeList;
import net.lisanza.CliArgs.ArgsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;

public class Parser {
    static private boolean debug = true;

    public NodeList moduleList;
    /**
     * Default consturctor that is required to initialize the parser.
     */
    public Parser() {
        /* Initialize the Grammar. (is needed if it becomes a static one) */
    }
    /**
     * A wrapper that parses a list (Enumeration) of files.
     */
    static public NodeList parseFiles(Enumeration e) throws SmiException {
        String filename;
        try {
            NodeList result = new NodeList();
            while (e.hasMoreElements()) {
                filename = (String)e.nextElement();
                if (debug) dump("File: " + filename);
                if (!(new File(filename)).exists()) {
                    filename = filename + ".txt";
                }
                result.addNode(parseFile(filename));
            }
            return result;
        } catch (FileNotFoundException exception) {
            throw new SmiException(exception.getMessage());
        } catch (NullPointerException exception) {
            throw new SmiException("input file: <>");
        } 
    }
    /**
     * A API wrapper that loads one or more MIB-modules in a file.
     * (This may be one or more MIB modules.)
     * Each MIB module is only loaded in the MIB-tree when that
     * MIB module contains not unrecoverable errors and is
     * not dependant on a MIB module not loaded (even though if that
     * MIB module is in the same file later).
     */
    static public NodeList parseFile(String name)
                                         throws FileNotFoundException,
                                                SmiException {
        if (debug) dump("Parser: " + name);
        return parseFile(new FileInputStream(new File(name)));
    }
    static public NodeList parseFile(FileInputStream file) throws SmiException {
        try {
            if (debug) dump("Parser: starting");
            SmiGrammar grammar = new SmiGrammar(file);
            NodeList grammarroot = grammar.ModuleDefinitionList();
            if (debug) dump("Parser: parsing done");
            if (MessageList.list.hasErrors()) {
                throw new SmiException(
                                "The SMI file; unrecoverable syntaxtical errors");
            }
            grammarroot.accept(new SemanticsSetup());
            grammarroot.accept(new SemanticsCheck());
            if (debug) dump("Parser: semantics done");
            if (MessageList.list.hasErrors()) {
                throw new SmiException(
                                "The SMI file; unrecoverable semantical errors");
            }
            grammarroot.accept(new MibTree());
            if (debug) dump("Parser: mibtree done");
            if (MessageList.list.hasErrors()) {
                throw new SmiException(
                                "The SMI file; unrecoverable mib tree errors");
            }
            if (debug) dump("Parser: done");
            return grammarroot;
        } catch (ParseException e) {
            throw new SmiException(
                                "The SMI file; unrecoverable error");
        }
    }
    static private void dump(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
        try {
            Parser parser = new Parser();
            ParserInputArgs input = new ParserInputArgs();
            input.parse(args);
            parser.parseFiles(input.getFiles());
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
        } catch (SmiException e) {
            System.out.println(e.getMessage());
            e.print();
        }
    }
}

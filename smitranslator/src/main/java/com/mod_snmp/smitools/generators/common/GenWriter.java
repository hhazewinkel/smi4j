package com.mod_snmp.smitools.generators.common;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.smiparser.syntaxtree.Identifier;
import com.mod_snmp.smiparser.syntaxtree.ModuleIdentifier;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Extends PrintWriter with additional functions that
 * print out SmiGrammar elements.
 */
public class GenWriter extends PrintWriter {
    private static String indent_str = "    ";

    public GenWriter(OutputStream o) {
        super(o, true);
    }
    public GenWriter(Writer w) {
        super(w, true);
    }

    public static String toUpperCase(String str) {
        str = str.toUpperCase();
        str = str.replace('-', '_');
        return str.replace('.', '_');
    }
    public static String toLowerCase(String str) {
        str = str.toLowerCase();
        str = str.replace('-', '_');
        return str.replace('.', '_');
    }
    public static String toUpperCase(Identifier id) {
        return toUpperCase(id.toString());
    }
    public static String toLowerCase(Identifier id) {
        return toLowerCase(id.toString());
    }
    public static String toUpperCase(ModuleIdentifier module) {
        return toUpperCase(module.toString());
    }
    public static String toLowerCase(ModuleIdentifier module) {
        return toUpperCase(module.toString());
    }
    public void print(int indent, String str) {
        while (indent > 0) {
            print(indent_str);
            indent--;
        }
        print(str);
    }
    public void println(int indent, String str) {
        while (indent > 0) {
            print(indent_str);
            indent--;
        }
        println(str); 
    }
    public void printStripped(String str) {
        printStripped(0, str);
    }
    public void printStripped(int indent, String str) {
        char[] buf = str.toCharArray();
        int shortest_space = Integer.MAX_VALUE;
        for (int i = 0; i < buf.length; i++) {
            if ((buf[i] == '\n') || (buf[i] == '\r')) {
                i++;
                int j = 0;
                while ((i < buf.length) && (buf[i] == ' ')) {
                     i++;
                     j++;
                }
                if ((j < shortest_space) && (j > 0)) {
                    shortest_space = j;
                }
            }
        }
        if (shortest_space == Integer.MAX_VALUE) {
            shortest_space = 0;
        }
        print(indent, " * ");
        for (int i = 0; i < buf.length; i++) {
             if (buf[i] == '\n') {
                println();
                print(indent, " * ");
                i += shortest_space;
             } else if (buf[i] != '\"') {
                print(buf[i]);
             }
        }
        println();
    }
}

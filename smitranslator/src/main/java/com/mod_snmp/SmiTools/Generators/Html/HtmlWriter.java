/**
 * HtmlWriter
 * Extends PrintWriter with additional functions that
 * print out SmiGrammar elements.
 */

package com.mod_snmp.SmiTools.Generators.Html;

import com.mod_snmp.SmiParser.SyntaxTree.Identifier;
import com.mod_snmp.SmiParser.SyntaxTree.ModuleIdentifier;
import com.mod_snmp.SmiParser.SyntaxTree.NodeList;
import com.mod_snmp.SmiParser.SyntaxTree.NodeToken;
import com.mod_snmp.SmiParser.SyntaxTree.NumericValue;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

public class HtmlWriter extends PrintWriter {

    public HtmlWriter(OutputStream o) {
        super(o);
    }

    public void printKeyword(String keyword) {
        println(" <B>" + keyword + "</B> ");
    }
    public void printIdentifierList(NodeList list) {
        Enumeration e = list.elements();  
        if (e.hasMoreElements()) {
            printIdentifier((Identifier)e.nextElement());
        }
        while (e.hasMoreElements()) {
            printComma();
            printIdentifier((Identifier)e.nextElement());
        }
    }
    public void printSymbol(ModuleIdentifier module,  String symbol) {
        println("<A HREF=\"" + module.toString() + "#" + symbol + "\">" + symbol + "</A>");
    }
    public void printIdentifier(Identifier id) {
        if (id.getKind() == Identifier.KEYWORD) {
            printKeyword(id.toString());
        } else {
            if (id.module_identifier != null) {
                print("<A HREF=\"" + id.module_identifier + "#" + id.identifier + "\">"
                                 + id.module_identifier + "." + id.identifier + "</A>");
            } else {
                print("<A HREF=\"#" + id.identifier + "\">" + id.identifier + "</A>");
            }
        }
    }
    public void printModuleIdentifier(ModuleIdentifier module) {
        String modStr = module.toString();
        print("<A HREF=\"" + modStr + "\">" + modStr + "</A>");
    }
    public void printNumber(NumericValue nr) {
        print("<FONT COLOR=\"RED\">" + nr + "</FONT>");
    }
    public void printText(NodeToken text) {
        print(text.toString());
    }
    public void printSpace()        { print(" "); }
    /* Including some spacing */
    public void printComma()        { print("<B>,</B> "); }
    public void printOr()           { print("<B>|</B> "); }
    public void printUntil()        { print("<B>..</B> "); }
    public void printLBrace()       { print(" <B>{</B> "); }
    public void printRBrace()       { print(" <B>}</B> "); }
    public void printLSquare()      { print(" <B>[</B>"); }
    public void printRSquare()      { print(" <B>]</B> "); }
    /* Excluding spacing */
    public void printLParen()       { print("<B>(</B>"); }
    public void printRParen()       { print("<B>)</B>"); }
    /* Format/page layout */
    public void printSectionBegin() { println("<P>"); }
    public void printSectionEnd()   { println("</P>"); }
    public void printBreak()        { println("<BR>"); }
    public void printIndent(int nr) { 
        if (nr > 0) { println("<UL>"); }
        if (nr < 0) { println("</UL>"); }
    }
}

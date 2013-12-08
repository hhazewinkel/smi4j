/**
 * Message
 */
package com.mod_snmp.SmiParser.ErrorHandler;
import com.mod_snmp.SmiParser.Grammar.Token;
import com.mod_snmp.SmiParser.SyntaxTree.Node;

public class Message {
    /**
     * The level of the message.
     */
    private static final int EMERG   = 0;
    private static final int ALERT   = 1;
    private static final int CRIT    = 2;
    private static final int ERROR   = 3;
    private static final int WARNING = 4;
    private static final int NOTICE  = 5;
    private static final int INFO    = 6;
    private static final int DEBUG   = 7;
    /**
     * Message parameters.
     */
    protected int level;
    protected int line;
    protected String msg;

    public Message(int linenr, String message) {
        this.level = ERROR;
        this.line = linenr;
        this.msg = message;
    }
    public Message(int level, int linenr, String message) {
        this.level = level;
        this.line = linenr;
        this.msg = message;
    }
    static public Message emerg(Node item, String message) {
        return emerg(item.line(), "'" + item + "' " + message);
    }
    static public Message emerg(Token t, String item, String message) {
        return emerg(t.beginLine, "'" + item + "' " + message);
    }
    static public Message emerg(int linenr, String message) {
        return new Message(EMERG, linenr, message);
    }
    static public Message alert(Node item, String message) {
        return alert(item.line(), "'" + item + "' " + message); 
    }
    static public Message alert(Token t, String item, String message) {
        return alert(t.beginLine, "'" + item + "' " + message); 
    }
    static public Message alert(int linenr, String message) {
        return new Message(ALERT, linenr, message);
    }
    static public Message critical(Node item, String message) {
        return critical(item.line(), "'" + item + "' " + message); 
    }
    static public Message critical(Token t, String item, String message) {
        return critical(t.beginLine, "'" + item + "' " + message);
    }
    static public Message critical(int linenr, String message) {
        return new Message(CRIT, linenr, message);
    }
    static public Message error(Node item, String message) {
        return error(item.line(), "'" + item + "' " + message); 
    }
    static public Message error(Token t, String item, String message) {
        return error(t.beginLine, "'" + item + "' " + message);
    }
    static public Message error(int linenr, String message) {
        return new Message(ERROR, linenr, message);
    }
    static public Message warning(Node item, String message) {
        return warning(item.line(), "'" + item + "' " + message); 
    }
    static public Message warning(Token t, String item, String message) {
        return warning(t.beginLine, "'" + item + "' " + message); 
    }
    static public Message warning(int linenr, String message) {
        return new Message(WARNING, linenr, message);
    }
    static public Message notice(Node item, String message) {
        return notice(item.line(), "'" + item + "' " + message); 
    }
    static public Message notice(Token t, String item, String message) {
        return notice(t.beginLine, "'" + item + "' " + message); 
    }
    static public Message notice(int linenr, String message) {
        return new Message(NOTICE, linenr, message);
    }
    static public Message debug(Node item, String message) {
        return debug(item.line(), "'" + item + "' " + message); 
    }
    static public Message debug(Token t, String item, String message) {
        return debug(t.beginLine, "'" + item + "' " + message); 
    }
    static public Message debug(int linenr, String message) {
        return new Message(DEBUG, linenr, message);
    }

    public void print() {
        System.err.println(level +" : line " + line + ": " + msg);
    }
}

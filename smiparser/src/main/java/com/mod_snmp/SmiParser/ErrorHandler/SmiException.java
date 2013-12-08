package com.mod_snmp.SmiParser.ErrorHandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.SmiParser.SyntaxTree.Node;

public class SmiException extends Exception implements SmiExceptionLevel {
    /**
     * Default constructor.
     */
    public SmiException() {
        super();
    }
    public SmiException(String s) {
        super(s);
    }
    public SmiException(int linenr, String s) {
        MessageList.list.add(new Message(ERROR, linenr, s));
    }
    public SmiException(Node item, String s) {
        this(item.line(), "'" + item + "' " + s);
    }
    public SmiException(int level, int linenr, String s) {
        MessageList.list.add(new Message(level, linenr, s));
    }
    public SmiException(int level, Node item, String s) {
        this(level, item.line(), "'" + item + "' " + s);
    }
    public void print() {
        MessageList.list.print();
    }
}

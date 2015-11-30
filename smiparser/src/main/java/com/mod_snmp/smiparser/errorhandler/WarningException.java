package com.mod_snmp.SmiParser.ErrorHandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.SmiParser.SyntaxTree.Node;
/**
 * Error Exception handler.
 */
public class WarningException extends SmiException {
    public WarningException() {
        super();
    }
    public WarningException(String s) {
        super(s);
    }
    public WarningException(int linenr, String s) {
        super(linenr, s);
    }
    public WarningException(Node item, String s) {
        super(item,s );
    }
    public WarningException(int level, int linenr, String s) {
        super(level, linenr,s );
    }
    public WarningException(int level, Node item, String s) {
        super(level, item,s );
    }
}

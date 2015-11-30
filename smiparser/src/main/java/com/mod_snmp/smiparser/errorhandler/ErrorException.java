package com.mod_snmp.smiparser.errorhandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.smiparser.syntaxtree.Node;
/**
 * Error Exception handler.
 */
public class ErrorException extends SmiException {
    public ErrorException() {
        super();
    }
    public ErrorException(String s) {
        super(s);
    }
    public ErrorException(int linenr, String s) {
        super(linenr, s);
    }
    public ErrorException(Node item, String s) {
        super(item,s );
    }
    public ErrorException(int level, int linenr, String s) {
        super(level, linenr,s );
    }
    public ErrorException(int level, Node item, String s) {
        super(level, item,s );
    }
}

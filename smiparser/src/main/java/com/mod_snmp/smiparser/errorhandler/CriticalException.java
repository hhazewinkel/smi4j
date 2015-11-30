package com.mod_snmp.smiparser.errorhandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.smiparser.syntaxtree.Node;
/**
 * Error Exception handler.
 */
public class CriticalException extends SmiException {
    public CriticalException() {
        super();
    }
    public CriticalException(String s) {
        super(s);
    }
    public CriticalException(int linenr, String s) {
        super(linenr, s);
    }
    public CriticalException(Node item, String s) {
        super(item,s );
    }
    public CriticalException(int level, int linenr, String s) {
        super(level, linenr,s );
    }
    public CriticalException(int level, Node item, String s) {
        super(level, item,s );
    }
}

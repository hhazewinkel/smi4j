package com.mod_snmp.smiparser.errorhandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.smiparser.syntaxtree.Node;
/**
 * Error Exception handler.
 */
public class AlertException extends SmiException {
    public AlertException() {
        super();
    }
    public AlertException(String s) {
        super(s);
    }
    public AlertException(int linenr, String s) {
        super(linenr, s);
    }
    public AlertException(Node item, String s) {
        super(item,s );
    }
    public AlertException(int level, int linenr, String s) {
        super(level, linenr,s );
    }
    public AlertException(int level, Node item, String s) {
        super(level, item,s );
    }
}

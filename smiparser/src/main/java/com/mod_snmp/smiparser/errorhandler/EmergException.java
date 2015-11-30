package com.mod_snmp.smiparser.errorhandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.smiparser.syntaxtree.Node;
/**
 * Error Exception handler.
 */
public class EmergException extends SmiException {
    public EmergException() {
        super();
    }
    public EmergException(String s) {
        super(s);
    }
    public EmergException(int linenr, String s) {
        super(linenr, s);
    }
    public EmergException(Node item, String s) {
        super(item,s );
    }
    public EmergException(int level, int linenr, String s) {
        super(level, linenr,s );
    }
    public EmergException(int level, Node item, String s) {
        super(level, item,s );
    }
}

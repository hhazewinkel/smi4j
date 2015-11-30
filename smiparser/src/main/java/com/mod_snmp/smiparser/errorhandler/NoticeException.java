package com.mod_snmp.smiparser.errorhandler;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.smiparser.syntaxtree.Node;
/**
 * Error Exception handler.
 */
public class NoticeException extends SmiException {
    public NoticeException() {
        super();
    }
    public NoticeException(String s) {
        super(s);
    }
    public NoticeException(int linenr, String s) {
        super(linenr, s);
    }
    public NoticeException(Node item, String s) {
        super(item,s );
    }
    public NoticeException(int level, int linenr, String s) {
        super(level, linenr,s );
    }
    public NoticeException(int level, Node item, String s) {
        super(level, item,s );
    }
}

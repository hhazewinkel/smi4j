package com.mod_snmp.SmiParser.ErrorHandler;
import com.mod_snmp.SmiParser.SyntaxTree.Node;
/**
 * Error Exception handler.
 */
public class InfoException extends SmiException {
    public InfoException() {
        super();
    }
    public InfoException(String s) {
        super(s);
    }
    public InfoException(int linenr, String s) {
        super(linenr, s);
    }
    public InfoException(Node item, String s) {
        super(item,s );
    }
    public InfoException(int level, int linenr, String s) {
        super(level, linenr,s );
    }
    public InfoException(int level, Node item, String s) {
        super(level, item,s );
    }
}

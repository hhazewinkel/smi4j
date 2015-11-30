package com.mod_snmp.smitools.generators.common;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import java.util.Hashtable;

/**
 * A hash oriented class that is used as the attribute 
 * container for walking the grammar tree of the ObjectDepthFirst class.
 */
public class TreeAttr extends Hashtable {


     public TreeAttr(String tp) {
        put(new String("type"), tp);
     }
     public boolean isType(String tp) {
        String type = (String)get("type");
        return type.equals(tp);
     }
     public String getType() {
         return (String)get("type");
     }
     public void indent(int nr) {
        put("indent", new Integer(nr));
     }
     public int getIndent() {
        Integer nr = (Integer)get("indent");
        return nr.intValue();
     }
     public void setAttribute(String tp, Object value) {
        if (!(tp.equals("type") || tp.equals("indent"))) {
            put(tp, value);
        }
     }
     public Object getAttribute(String tp) {
        return get(tp);
     }
        
}


package com.mod_snmp.smitools.generators.manappl;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.smiparser.mibtree.MibTreeNode;

/**
 * ManApplMibTreeNode
 */
public class ManApplMibTreeNode {

    public static String getDottedObjectIdString(MibTreeNode mtn) {
        Object path[] = mtn.getPath();
        String oidstr = "\"" + ((MibTreeNode)path[0]).getNumber();
        for (int i = 1 ; i < path.length ; i++) {
            oidstr += "." + ((MibTreeNode)path[i]).getNumber();
        }
        return oidstr + "\"";
    }
}

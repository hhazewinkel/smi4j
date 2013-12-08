package com.mod_snmp.SmiParser.MibTree;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import javax.swing.tree.DefaultTreeModel;

public class MibTreeModel extends DefaultTreeModel {
    static public MibTreeModel tm;

     public MibTreeModel(MibTreeNode mtn) {
         super(mtn);
     }
}

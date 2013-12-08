package com.mod_snmp.SmiParser.MibTree;

import javax.swing.tree.DefaultTreeModel;

public class MibTreeModel extends DefaultTreeModel {
    static public MibTreeModel tm;

     public MibTreeModel(MibTreeNode mtn) {
         super(mtn);
     }
}

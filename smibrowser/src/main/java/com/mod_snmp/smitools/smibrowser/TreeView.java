package com.mod_snmp.SmiTools.SmiBrowser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.SmiParser.MibTree.MibTree;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import java.awt.Dimension;

/**
 * The class that provides the view of the MIB tree.
 */
public class TreeView extends JScrollPane {
    public JTree tree;

    public TreeView() {
        super();
        setPreferredSize(new Dimension(300, 600));
        setMibTree();
    }

    public void setMibTree() {
        MibTree.model.addTreeModelListener(new TreeViewListener());
        tree = new JTree(MibTree.model);
    }

    private class TreeViewListener implements TreeModelListener {
        public void treeNodesChanged(javax.swing.event.TreeModelEvent e) {
            //System.out.println("treeNodesChanged");
        }
        public void treeNodesInserted(javax.swing.event.TreeModelEvent e) {
            //System.out.println("treeNodesInserted");
        }
        public void treeNodesRemoved(javax.swing.event.TreeModelEvent e) {
            //System.out.println("treeNodesRemoved");
        }
        public void treeStructureChanged(javax.swing.event.TreeModelEvent e) {
            //System.out.println("treeStructureChanged");
        }
    }
}

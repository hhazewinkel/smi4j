package com.mod_snmp.smitools.smibrowser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * TabbedView
 * The class that provides a tabular based information window of the tree view.
 */


import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;

public class TabbedView extends JTabbedPane {
    ObjectInfoView objectInfo;
    static String objectInfoTitle = "object info";
    private JTree tree;

    public TabbedView() {
        super();
        objectInfo = new ObjectInfoView();
        insertTab(objectInfoTitle, null, objectInfo, null, 0);
    }
    public TabbedView(TreeView t) {
        this();
        tree = t.tree;
    }

    public void treeValueChanged(TreeSelectionEvent e) {
        remove(objectInfo);
        objectInfo = new ObjectInfoView(tree.getSelectionPath().getPath());
        insertTab(objectInfoTitle, null, objectInfo, null, 0);
    }

}

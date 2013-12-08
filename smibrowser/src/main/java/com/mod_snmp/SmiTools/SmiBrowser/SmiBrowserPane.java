/**
 * SmiBrowserPane.java
 * The top-level window panel from which all view and gui interactions
 * are provided.
 */
package com.mod_snmp.SmiTools.SmiBrowser;

import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class SmiBrowserPane extends JSplitPane {
    /**
     * The tree view provides the interface of the MIB tree.
     */
    TreeView treeView;
    /**
     * The tabbed view provides specific information of the MIB object
     * selected.
     */
    TabbedView tabbedView;

    public SmiBrowserPane() {
        super();
        try {
            treeView = new TreeView();
            add(treeView, JSplitPane.LEFT);
            tabbedView = new TabbedView(treeView);
            add(tabbedView, JSplitPane.RIGHT);
            setUpSelectionListener();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void setUpSelectionListener() {
        treeView.getViewport().add(treeView.tree, null);
        add(tabbedView, JSplitPane.RIGHT);
        treeView.tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                treeValueChanged(e);
            }
        });
    }
    protected void update() {
        treeView.getViewport().add(treeView.tree, null);
    }
    protected void treeValueChanged(TreeSelectionEvent e) {
        tabbedView.treeValueChanged(e);
    }
}

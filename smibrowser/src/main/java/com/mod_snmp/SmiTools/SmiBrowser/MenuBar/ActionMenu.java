/**
 * ActionMenu
 * The class that defines the module menu.
 */
package com.mod_snmp.SmiTools.SmiBrowser.MenuBar;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionMenu extends JMenu implements ActionListener {
    private static JMenuItem items[] = { new JMenuItem("XML"),
                                         new JMenuItem("HTML"),
                                         new JMenuItem("Net-SNMP") };

    public ActionMenu() {
        super("Action");
        for (int i = 0; i < items.length ; i++) {
            items[i].addActionListener(this);
            add(items[i]);
        }
    }

    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        System.err.println("The ActionMenu action " + action);
    }
}

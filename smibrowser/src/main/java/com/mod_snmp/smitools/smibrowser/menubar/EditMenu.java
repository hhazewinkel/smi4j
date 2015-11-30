package com.mod_snmp.SmiTools.SmiBrowser.MenuBar;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that defines the edit menu.
 */
public class EditMenu extends JMenu implements ActionListener {
    
    private static JMenuItem items[] = { new JMenuItem("Copy"),
                                         new JMenuItem("Cut"),
                                         new JMenuItem("Paste"),
                                         new JMenuItem("Preferences") };

    public EditMenu() {
	super("Edit");
        for (int i = 0; i < items.length ; i++) {
            items[i].addActionListener(this);
            add(items[i]);
        }
    }

    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        //System.err.println("The EditMenu action " + action);
        if (action.equals("Copy")) {
        } else if (action.equals("Cut")) {
        } else if (action.equals("Paste")) {
        } else if (action.equals("Preferences")) {
           Preferences preferences = new Preferences();
        } else {
        }
    }
}

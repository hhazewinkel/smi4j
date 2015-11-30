package com.mod_snmp.smitools.smibrowser.menubar;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that defines the edit menu.
 */
public class HelpMenu extends JMenu implements ActionListener {
    
    private static JMenuItem items[] = { new JMenuItem("About") };

    public HelpMenu() {
	super("Help");
        for (int i = 0; i < items.length ; i++) {
            items[i].addActionListener(this);
            add(items[i]);
        }
    }

    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        System.err.println("The HelpMenu action " + action);
        if (action.equals("About")) {
           JOptionPane.showMessageDialog(new JFrame(),
                         "A MIB definition tree browser\n" +
                                 "Author: H. Hazewinkel\n" +
                                 "version: 0.0",
                         "About SMI browser", JOptionPane.PLAIN_MESSAGE);
        } else {
        }
    }

    //private class AboutActionListener implements ActionListener {
        //public void actionPerformed(ActionEvent event) {
            //String action = event.getActionCommand();
            //System.err.println("The HelpMenu AboutActionListener " + action);
        //}
    //}
}

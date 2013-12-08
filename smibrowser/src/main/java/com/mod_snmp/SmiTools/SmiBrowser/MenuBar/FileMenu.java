package com.mod_snmp.SmiTools.SmiBrowser.MenuBar;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * FileMenu
 * The class that defines the file menu.
 */

import com.mod_snmp.SmiParser.ErrorHandler.SmiException;
import com.mod_snmp.SmiParser.Parser;
import com.mod_snmp.SmiTools.SmiBrowser.Common.RootFrame;
import com.mod_snmp.SmiTools.SmiBrowser.SmiBrowser;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class FileMenu extends JMenu implements ActionListener {
    private static String items[] = { "Open", "Save" };

    public FileMenu() {
        super("File");
        for (int i = 0; i < items.length ; i++) {
            JMenuItem item = new JMenuItem(items[i]);
            item.addActionListener(this);
            add(item);
        }
    }

    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        System.err.println("The FileMenu action " + action);
        if (action.equals("Open")) {
            String module = JOptionPane.showInputDialog(RootFrame.get(),
                                "Please enter the MIB module name", "Open MIB module",
                                JOptionPane.QUESTION_MESSAGE);
            if (module != null) {
                System.err.println("The FileMenu action " + action + ":" + module);
                try {
                    Parser.parseFile(SmiBrowser.input.getMibDirectory()
                                             + "/" + module);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(RootFrame.get(),
                                e.getMessage(), "Error",
                                JOptionPane.INFORMATION_MESSAGE, null);
                } catch (SmiException e) {
                    JOptionPane.showMessageDialog(RootFrame.get(),
                                "Error in MIB module", "Error",
                                JOptionPane.INFORMATION_MESSAGE, null);
                }
            } else {
                JOptionPane.showMessageDialog(RootFrame.get(),
                                "No MIB module was given", "Error",
                                JOptionPane.INFORMATION_MESSAGE, null);
            }
        } else if (action.equals("Save")) {
        }
        System.err.println("The FileMenu action " + event.getActionCommand() + " done");
    }
}

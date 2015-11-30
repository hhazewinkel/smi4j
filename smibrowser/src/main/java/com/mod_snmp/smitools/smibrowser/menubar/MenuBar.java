package com.mod_snmp.smitools.smibrowser.menubar;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * ApplMenuBar
 * The class that defines the menu bar.
 */

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {
    public MenuBar() {
        add(new FileMenu());
        add(new EditMenu());
        add(new ActionMenu());
        add(new HelpMenu());
    }

}

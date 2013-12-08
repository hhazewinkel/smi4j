/**
 * ApplMenuBar
 * The class that defines the menu bar.
 */
package com.mod_snmp.SmiTools.SmiBrowser.MenuBar;

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {
    public MenuBar() {
        add(new FileMenu());
        add(new EditMenu());
        add(new ActionMenu());
        add(new HelpMenu());
    }

}

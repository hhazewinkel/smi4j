package com.mod_snmp.SmiTools.SmiBrowser.Common;
import javax.swing.JFrame;

public class RootFrame {
    private static JFrame rootFrame;

    public static void set(JFrame rf) {
        rootFrame = rf;
    }
    public static JFrame get() {
        return rootFrame;
    }
}

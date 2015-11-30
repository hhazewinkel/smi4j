package com.mod_snmp.SmiTools.SmiBrowser.Common;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
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

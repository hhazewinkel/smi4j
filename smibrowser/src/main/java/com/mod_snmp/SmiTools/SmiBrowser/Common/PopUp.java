package com.mod_snmp.SmiTools.SmiBrowser.Common;

import javax.swing.JFrame;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

/**
 * The top-level window from which all view and gui interactions are provided.
 */
public class PopUp extends JFrame {

    public PopUp() {
        this("");
    }

    public PopUp(String name) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            setTitle(name);
            setSize(new Dimension(100, 150));
            setVisible(true);
            validate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            return;
        }
    }


}

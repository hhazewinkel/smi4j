package com.mod_snmp.SmiTools.SmiBrowser.MenuBar;

import com.mod_snmp.SmiTools.SmiBrowser.Common.RootFrame;
import com.mod_snmp.SmiTools.SmiBrowser.SmiBrowser;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Preferences extends JDialog {
    private JTextField mibDirField;

    /**
     * Creator for the Preferences Dialog frame.
     */
    public Preferences() {
        super(RootFrame.get(), "Preferences SMI browser", true);
        fixSize();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        JPanel centerBox = new JPanel();
        centerBox.setLayout(new GridLayout(1, 1, 10, 10));
        centerBox.add(new JLabel("MIB module directory"));
        centerBox.add(createMibDirField());
        contentPane.add(centerBox, BorderLayout.CENTER);
	JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(1, 1, 10, 10));
	buttonPane.add(createSaveButton());
        buttonPane.add(createCloseButton());
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JTextField createMibDirField() {
        mibDirField = new JTextField(SmiBrowser.input.getMibDirectory());
        mibDirField.addActionListener(new MibDirActionListener());
        return mibDirField;
    }
    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new saveActionListener());
        return saveButton;
    }
    private JButton createCloseButton() {
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new CloseActionListener());
        return closeButton;
    }

    public void fixSize() {
	/* Set up the location and size */
        Dimension screenSize = getToolkit().getDefaultToolkit().getScreenSize();
        int windowWidth = 500;
        int windowHeight = 300;
        int windowLocationX = (screenSize.width - windowWidth) / 2;
        int windowLocationY = (screenSize.height - windowHeight) / 2;
        setLocation(windowLocationX, windowLocationY);
        setSize(windowWidth, windowHeight);
        setResizable(true);
    }

    /**
     * An internal class implementing a listener.
     */
    private class MibDirActionListener implements ActionListener {
         public void actionPerformed(ActionEvent event) {
        }
    }

    /**
     * An internal class implementing a listener.
     */
    private class saveActionListener implements ActionListener {
         public void actionPerformed(ActionEvent event) {
            SmiBrowser.input.setMibDirectory(mibDirField.getText());
            Preferences.this.setVisible(false);
        }
    }

    /**
     * An internal class implementing a listener.
     */
    private class CloseActionListener implements ActionListener {
         public void actionPerformed(ActionEvent event) {
            Preferences.this.setVisible(false);
        }
    }
}

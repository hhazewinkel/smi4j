package com.mod_snmp.SmiTools.SmiBrowser.MenuBar;

import com.mod_snmp.SmiTools.SmiBrowser.Common.RootFrame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class About extends JDialog {
    /**
     * Creator for the About Dialog frame.
     */
    public About() {
        super(RootFrame.get(), "About SMI browser", true);
        fixSize();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        JPanel centerBox = new JPanel();
        centerBox.setLayout(new GridLayout(0, 1, 10, 10));
        centerBox.add(new JLabel("A MIB definition tree browser",
                                         JLabel.CENTER),
                                                BorderLayout.CENTER);
        centerBox.add(new JLabel("version: 0.0",
                                         JLabel.CENTER),
                                                BorderLayout.CENTER);
        centerBox.add(new JLabel("Author: H.Hazewinkel <harrie@mod-snmp.com>",
                                         JLabel.CENTER),
                                                BorderLayout.CENTER);
        contentPane.add(centerBox);
        contentPane.add(createCloseButton(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private void fixSize() {
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

    private JButton createCloseButton() {
        JButton closeButton = new JButton("Close");
        //closeButton.setForeground(Color.white);
        //closeButton.setBackground(UIManager.getColor("pmdBlue"));
        closeButton.addActionListener(new CloseActionListener());
        return closeButton;
    }

    /**
     * An internal class implementing a listener.
     */
    private class CloseActionListener implements ActionListener {
         public void actionPerformed(ActionEvent event) {
            About.this.setVisible(false);
        }
    }

}

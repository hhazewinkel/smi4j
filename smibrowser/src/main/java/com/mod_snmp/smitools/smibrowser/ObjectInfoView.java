package com.mod_snmp.SmiTools.SmiBrowser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * ObjectInfoView
 * The class that provides the detailed information of the
 * selected object of the tree view.
 */

import com.mod_snmp.SmiParser.MibTree.MibTreeNode;
import com.mod_snmp.SmiParser.SyntaxTree.ObjectInfo;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;

public class ObjectInfoView extends JPanel {
    
    public ObjectInfoView() {
    }

    public ObjectInfoView(Object path[]) {
        String name =  new String(((MibTreeNode)path[0]).getName());
        String oidstr = new String("" + ((MibTreeNode)path[0]).getNumber());
        for (int i = 1 ; i < path.length ; i++) {
             name += "." + ((MibTreeNode)path[i]).getName();
             oidstr += "." + ((MibTreeNode)path[i]).getNumber();
        }
        addHeaderLayout(name, oidstr);
        ObjectInfo info = (ObjectInfo)((MibTreeNode)path[path.length-1]).getUserObject();
        info.accept(new ObjectInfoVisitor(this));
    }

    private void addHeaderLayout(String fullName, String oidstr) {
        addLabel("Name:", JLabel.LEFT);
        JTextField textField = new JTextField(fullName, 50);
        textField.setBorder(BorderFactory.createLineBorder(Color.black));
        textField.setVisible(true);
        add(textField);
        addLabel("Object Id.:", JLabel.LEFT);
        textField = new JTextField(oidstr, 50);
        textField.setBorder(BorderFactory.createLineBorder(Color.black));
        textField.setVisible(true);
        add(textField);
    }

    public void addLabel(String labelStr) {
        addLabel(labelStr, JLabel.LEFT);
    }
    public void addLabel(String labelStr, int alignment) {
        JLabel label = new JLabel(labelStr, alignment);
        label.setVisible(true);
        add(label);
    }
    public void addTextField(String valueStr) {
        addTextField(valueStr, 20);
    }   
    public void addTextField(String valueStr, int size) {
        JTextField field = new JTextField(valueStr, size);
        field.setVisible(true);
        add(field);
    }

}

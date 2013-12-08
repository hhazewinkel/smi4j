package com.mod_snmp.SmiParser.MibTree;

import com.mod_snmp.SmiParser.ErrorHandler.Message;
import com.mod_snmp.SmiParser.SyntaxTree.AssignedValue;
import com.mod_snmp.SmiParser.SyntaxTree.AssignmentObject;
import com.mod_snmp.SmiParser.SyntaxTree.NodeList;
import com.mod_snmp.SmiParser.SyntaxTree.ObjectInfoObjectIdentifier;
import com.mod_snmp.SmiParser.SyntaxTree.OidValue;
import com.mod_snmp.SmiParser.Visitor.DepthFirstVisitor;
import com.mod_snmp.SmiParser.Visitor.Visitor;

public class MibTree extends DepthFirstVisitor implements Visitor {
    static private boolean debug = false;
    static public MibTreeModel model;
    static public MibTreeSymbolTable symbolTable;
    static public MibTreeNode root_ccitt;
    static public MibTreeNode root_iso;
    static public MibTreeNode root_iso_ccitt;

    static {
        symbolTable = new MibTreeSymbolTable();
        try {
            root_ccitt = new MibTreeNode("ccitt", 0);
            root_ccitt.setUserObject(new ObjectInfoObjectIdentifier());
            symbolTable.insert(root_ccitt);
        } catch (MibTreeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        try {
            root_iso = new MibTreeNode("iso", 1);
            root_iso.setUserObject(new ObjectInfoObjectIdentifier());
            symbolTable.insert(root_iso);
        } catch (MibTreeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        try {
            root_iso_ccitt = new MibTreeNode("joint-iso-ccitt", 2);
            root_iso_ccitt.setUserObject(new ObjectInfoObjectIdentifier());
            symbolTable.insert(root_iso_ccitt);
        } catch (MibTreeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        model = new MibTreeModel(root_iso);
    }

    /**
     * <PRE>
     * identifier -> ValueIdentifier()
     * info -> ObjectInfo()
     * assign_token -> &lt;ASSIGN_T&gt;
     * assignedValue -> AssignedValue()
     * </PRE>
     */
    public void visit(AssignmentObject n) {
        try {
            MibTreeNode mtn = buildMibTreeHierarchy(n.assignedValue);
            mtn.setUserObject(n.info);
            model.reload();
        } catch (MibTreeException exception) {
System.out.println(n.identifier + ":" + exception.getMessage());
            Message.error(n.identifier, exception.getMessage());
        }
    }

    static private MibTreeNode buildMibTreeHierarchy(AssignedValue val)
                                                throws MibTreeException {
        NodeList oid_list = val.oidValue;
        int i = 0;
        while (i < (oid_list.size() - 2)) {
            if (debug) System.out.println("WHILE: " + i);
            makeMibTreeNode((OidValue)oid_list.elementAt(i),
                                        (OidValue)oid_list.elementAt(i+1));
            i++;
        }
        if (debug) System.out.println("LAST: " + i);
        return makeMibTreeNode((OidValue)oid_list.elementAt(i),
                                        (OidValue)oid_list.elementAt(i+1));
    }

    private static MibTreeNode makeMibTreeNode(OidValue parent, OidValue obj) 
                                   throws MibTreeException {
        if (! obj.hasName()) {
            throw new MibTreeException("Cannot create MIB tree node;"
                                                + " has no name");
        }
        if (debug) System.out.println("CHILD: " + obj.getName());
        MibTreeNode obj_mtn = symbolTable.retrieve(obj.getName().toString());
        if (debug) System.out.println("CHILD: " + obj_mtn.toString());
        obj_mtn.setNumber(obj.getNumber().getValue());
        if (! parent.hasName()) {
            throw new MibTreeException("MIB tree node under node;"
                                                + " parent has no name");
        }
        if (debug) System.out.println("PARENT: " + parent.getName());
        MibTreeNode parent_mtn = symbolTable.retrieve(parent.getName().toString());
        if (debug) System.out.println("PARENT: " + parent_mtn.toString());
        parent_mtn.setNumber(parent.getNumber().getValue());
        parent_mtn.add(obj_mtn);
        return obj_mtn;
    }

}

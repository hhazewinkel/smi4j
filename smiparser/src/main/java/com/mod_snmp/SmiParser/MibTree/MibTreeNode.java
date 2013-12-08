/**
 * MibTreeNode
 * The class that represents the nodes of the MIB tree.
 * This class is also used to indicate the start point of
 * MIb module tree representation as well.
 */

package com.mod_snmp.SmiParser.MibTree;

import com.mod_snmp.SmiParser.SyntaxTree.Identifier;
import com.mod_snmp.SmiParser.SyntaxTree.ObjectInfo;
import com.mod_snmp.SmiParser.SyntaxTree.ObjectInfoObjectType;

import javax.swing.tree.DefaultMutableTreeNode;

public class MibTreeNode extends DefaultMutableTreeNode {
    /**
     * The Object Identifier name of the MibTree node.
     */
    private String name;
    /**
     * The Object Identifier number of the MibTree node.
     */
    private long number;

    public MibTreeNode(String n) {
        name = n;
        number = -1;
    }
    public MibTreeNode(String n, long nr) {
        name = n;
        number = nr;
    }
    public void add(MibTreeNode child) throws MibTreeException {
        long child_number = child.getNumber();
        int child_index = 0;
        while (child_index < getChildCount()) {
            MibTreeNode current = (MibTreeNode)getChildAt(child_index);
            if (child_number == current.getNumber()) {
                if (! current.getName().equals(child.getName())) {
                    throw new MibTreeException("oid number value for " + current.getName()
                                                + " and " + child.getName() + " are equal");
                }
                return;
            }
            if (child_number < current.getNumber()) {
                insert(child, child_index);
                return;
            }
            child_index++;
        }
        insert(child, child_index);
    }
    public void setNumber(long nr) 
                                throws MibTreeException {
        if (number == -1) {
            number = nr;
        } else if ((nr > -1) && (number != nr)) {
            throw new MibTreeException("cannot assign a number " + nr);
        }
    }
    public long getNumber() {
        return number;
    }
    public void setName(String n) {
        name = n;
    }
    public void setName(Identifier id) {
        name = id.toString();
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return name + "(" + number + ")";
    }
    public boolean isManagedInstance() {
        if ((userObject instanceof ObjectInfoObjectType) && isLeaf()) {
            return true;
        }
        return false;
    }
    public boolean managesInstances() {
        int child_index = 0;
        while (child_index < getChildCount()) {
            MibTreeNode mtn = (MibTreeNode)getChildAt(child_index);
            if (mtn.isManagedInstance()) {
                return true;
            }
            child_index++;
        }
        return false;
    }
    public boolean isConceptualRow() {
        return ((ObjectInfo)userObject).isConceptualRow();
    }

    /**
     * isScalar
     * Tests whether a MibTreeNode is representing a scalar object in the MIB.
     * true if defines a ObjectInfoObjectType for its userObject and
     *         is a leaf and
     *         parent is not a managed instance
     *                            !(userObject instanceof ObjectInfoObjectType)
     * false otherwise.
     */
    public boolean isScalar() {
        if (isInstance() && isLeaf()) {
            if (parent != null) {
                return !(((MibTreeNode)parent).isInstance());
            }
        }
        return false;
    }
    /**
     * isColumnar
     * Tests whether a MibTreeNode is representing a columnar object in the MIB.
     * true if defines a ObjectInfoObjectType for its userObject and
     *         is a leaf and
     *         parent is a SEQUENCE definition type.
     *                            !(userObject instanceof ObjectInfoObjectType)
     * false otherwise.  
     */ 
    public boolean isColumnar() {
        if (isInstance() && isLeaf()) {
            if (parent != null) {
                return (((MibTreeNode)parent).isInstance());
            }
        }
        return false;
    }
    /**
     * isTable
     * Tests whether a MibTreeNode is representing a table object in the MIB.
     * true if defines a ObjectInfoObjectType for its userObject and
     *         parent is not a managed instance and
     *                            !(userObject instanceof ObjectInfoObjectType)
     *         has only one child representing the SEQUENCE.
     * false otherwise.
     */
    public boolean isTable() {
        if (isInstance()) {
            if (parent != null) {
                if (!(((MibTreeNode)parent).isInstance())) {
                    return (getChildCount() == 1);
                        
                }
            }
        }
        return false;
    }

    public boolean isInstance() {
        return (userObject instanceof ObjectInfoObjectType);
    }

    /**
     * A visitor function enabling extension of the functionality
     * of the class without changing the class.
     * This has an runtime impact of 2 function calls.
     */
    public void accept(MibTreeVisitor v) {
        v.visit(this);
    }
    /**
     * A visitor function enabling extension of the functionality
     * of the class without changing the class.
     * This has an runtime impact of 2 function calls.
     */
    public Object accept(MibTreeObjectVisitor v, Object argu) {
        return v.visit(this,argu);
    }
}

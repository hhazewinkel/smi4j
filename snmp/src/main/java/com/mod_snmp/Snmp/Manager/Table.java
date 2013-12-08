package com.mod_snmp.Snmp.Manager;

import com.mod_snmp.Snmp.Protocol.SnmpMessageException;
import com.mod_snmp.Snmp.Protocol.SnmpPdu;
import com.mod_snmp.Snmp.Values.SnmpValueException;
import com.mod_snmp.Snmp.Values.Varbind;
import com.mod_snmp.Snmp.Values.VarbindList;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * A wrapper combining an SnmpManager and some basic/core
 * methods for table access.
 * The (conceptual) table provides access to a coherent set of
 * management information in a remote SNMP agent. This class implements
 * a core set of methods with which data can be retrieved from such
 * a remote SNMP agent on behalf of the SnmpManager.
 * This class maybe used as a convenience class to build
 * management applications.
 * It does not extend an SnmpManager to allow a single SnmpManager
 * to retrieve multiple kinds of data.
 */
public class Table {
    /**
     * The SnmpManager contains all info of the target.
     */
    private SnmpManager manager;

    /**
     * Default constructor.
     */
    public Table (SnmpManager mgr) {
        manager = mgr;
    }

    /**
     * This function will retrieve a complete Table.
     * The rows will be return in a Vector.
     * Each element of the Vector contains an Varbind-object.
     * @param v The indicator for the Vector return type and the
     * managed objects are added to this vector.
     * NOTE: Use 'new Vector()' from the caller. Otherwise, managed
     * object simply get added.
     * @param vbl The variable bindings list containing the
     * the first object identifier from which the
     * the table can be retrieved.
     * Typically, this is the object identifier of the
     * table itself.
     * @return The vector containing the instances of the table.
     */
    public Vector coreGetTable(Vector v, VarbindList vbl) {
        Vector result = new Vector();
        try {
            boolean still_in_table = true;
            VarbindList send_vbl = vbl;
            do {
                SnmpPdu pdu = manager.invokeSnmpGetNext(send_vbl);
                VarbindList recv_vbl = pdu.getVarbindList();

                if (pdu.isErrorStatusSet()) {
                    System.out.println((recv_vbl.varbindAt(pdu.getErrorIndex() - 1)).name +
                                                             " : " + pdu.getErrorStatus(""));
                } else if (vbl.size() != recv_vbl.size()) {
                    System.out.println("did not receive expected amount of varbinds, but error-status was not set");
                } else {
                    send_vbl = new VarbindList();
                    for (int i = 0; i < vbl.size(); i++) {
                        if (recv_vbl.varbindAt(i).name.startsWith((vbl.varbindAt(i)).name)) {
                            ManagedInstance mi = new ManagedInstance(recv_vbl.varbindAt(i));
                            result.insertElementAt(mi, result.size());
                            send_vbl.addVarbind(mi.createVarbind());
                        } else {
                            still_in_table = false;
                        }
                    }
                }
            } while (still_in_table);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This function to retrieve a complete table as a Hashtable.
     * the key is a string build up from the column-oid and the index oid-part.
     * @param h The indicator for the Hashtable return type and the
     * managed objects are added to this Hashtable.
     * NOTE: Use 'new Hashtable()' from the caller if a only newly
     * retrieved variables should be returned. Otherwise, managed object
     * simply get added.
     * @param vbl The variable bindings list containing the     
     * the first object identifier from which the
     * the table can be retrieved.
     * Typically, this is the object identifier of the
     * table itself.
     * @return The hashtable containing the instances of the table.
     * The instances of the table are identified by the key-objects
     * of the Hastable
     */
    public Hashtable coreGetTable(Hashtable h, VarbindList vbl) {
        Hashtable result = new Hashtable();
        try {
            boolean still_in_table = true;
            VarbindList send_vbl = vbl;
            do {
                SnmpPdu pdu = manager.invokeSnmpGetNext(send_vbl);
                VarbindList recv_vbl = pdu.getVarbindList();

                if (pdu.isErrorStatusSet()) {
                    System.out.println((recv_vbl.varbindAt(pdu.getErrorIndex() - 1)).name +
                                                             " : " + pdu.getErrorStatus(""));
                } else if (vbl.size() != recv_vbl.size()) {
                    System.out.println("did not receive expected amount of varbinds, but error-status was not set");
                } else {
                    send_vbl = new VarbindList();
                    for (int i = 0; i < vbl.size(); i++) {
                        if (recv_vbl.varbindAt(i).name.startsWith((vbl.varbindAt(i)).name)) {
                            ManagedInstance mi = new ManagedInstance(recv_vbl.varbindAt(i));
                            result.put(mi.name, mi);
                            send_vbl.addVarbind(mi.createVarbind());
                        } else {
                            still_in_table = false;
                        }
                    }
                }
            } while (still_in_table);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This function will retrieve a column of a table.
     * @param v The indicator for the Vector return type and the   
     * managed objects are added to this vector.   
     * NOTE: Use 'new Vector()' from the caller. Otherwise, managed        
     * object simply get added.  
     * @param column The name of the colum to be retrieved.
     * This is the object identifier of the entry of the table.
     * @return The vector containing the instances of the table.
     */
    public Vector coreGetColumn(Vector v, String column)
                                        throws SnmpValueException {
        Vector result = new Vector();
        ManagedInstance columnObjectId = new ManagedInstance(column);
        VarbindList vbl = new VarbindList();
        vbl.addVarbind(columnObjectId.createVarbind());
        boolean in_column = true;
        try {
            do {
                SnmpPdu pdu = manager.invokeSnmpGetNext(vbl);
                vbl = pdu.getVarbindList();
                if (!pdu.isErrorStatusSet()) {
                    Varbind vb = vbl.varbindAt(0);
                    if (vb.name.startsWith(columnObjectId.vbName)) {
                        result.insertElementAt(new ManagedInstance(vb), result.size());
                        vbl = new VarbindList();
                        vbl.addVarbind(new Varbind(vb.name));
                    } else {
                        in_column = false;
                    }
                }
            } while (in_column);
        } catch (SnmpMessageException e) {
            /* We just catch the exception and return.   */
            /* No harm done. At most an empty table.     */
        }
        return result;
    }

    /**
     * This function will retrieve a column of a table.
     * @param h The indicator for the Hashtable return type and the
     * managed objects are added to this Hashtable.
     * NOTE: Use 'new Hashtable()' from the caller to have only the
     * newly retrieved values.  Otherwise, managed object
     * simply get added.
     * @param column The name of the colum to be retrieved.
     * This is the object identifier of the entry of the table.
     * @return A Hashtable containing the instances of the column.
     * The instances of the column are identified by the key-objects
     * of the Hastable the key is a string build up from the index oid-part.
     */
    public Hashtable coreGetColumn(Hashtable h, String column)
                                        throws SnmpValueException {
        Hashtable result = new Hashtable();
        ManagedInstance columnObjectId = new ManagedInstance(column);
        VarbindList vbl = new VarbindList();
        vbl.addVarbind(columnObjectId.createVarbind());
        boolean in_column = true;
        try {
            do {
                SnmpPdu pdu = manager.invokeSnmpGetNext(vbl);
                vbl = pdu.getVarbindList();
                if (!pdu.isErrorStatusSet()) {
                    Varbind vb = vbl.varbindAt(0);
                    if (vb.name.startsWith(columnObjectId.vbName)) {
                        result.put(vb.name.toString(), new ManagedInstance(vb));
                        vbl = new VarbindList();
                        vbl.addVarbind(new Varbind(vb.name));
                    } else {
                        in_column = false;
                    }
                }
            } while (in_column);
        } catch (SnmpMessageException e) {
            /* We just catch the exception and return.   */
            /* No harm done. At most an empty table.     */
        }
        return result;
    }

    /**
     * Retrieve a row in a table  associated with the index
     * @param h The indicator for the Hashtable return type and the
     * managed objects are added to this Hashtable.
     * NOTE: Use 'new Hashtable()' from the caller to have only the
     * newly retrieved values.  Otherwise, managed object
     * simply get added.
     * @param index Defines the index associated with the row.
     * @return A Hashtable containing the instances of the row.
     * The instances of the row are identified by the key-objects of the Hastable
     * the key represents the column name.
     */
    public Hashtable coreGetRow(Hashtable h, VarbindList vbl, String index)
                                        throws SnmpValueException {
        Hashtable result = new Hashtable();
        try {
            SnmpPdu pdu = manager.invokeSnmpGet(vbl);
            vbl = pdu.getVarbindList();
            if (!pdu.isErrorStatusSet()) {
                Enumeration vbs = vbl.varbinds();
                while (vbs.hasMoreElements()) {
                    ManagedInstance mi = new ManagedInstance((Varbind)vbs.nextElement());
                    result.put(mi.name, mi);
                }
            }
        } catch (SnmpMessageException e) {
            /* We just catch the exception and return.   */
            /* No harm done. At most an empty table.     */
        }
        return result;
    }
    /**
     * Retrieve the next row in a table of associated with the index
     * @param h The indicator for the Hashtable return type and the
     * managed objects are added to this Hashtable.
     * NOTE: Use 'new Hashtable()' from the caller to have only the
     * newly retrieved values.  Otherwise, managed object
     * simply get added.
     * @param index Defines the index associated with the row.
     * @return A Hashtable containing the instances of the row.
     * The instances of the row are identified by the key-objects of the Hastable
     * the key represents the column name.
     */
    public Hashtable coreGetNextRow(Hashtable h, VarbindList vbl, String index)
                                        throws SnmpValueException {
        Hashtable result = new Hashtable();
        try {
            SnmpPdu pdu = manager.invokeSnmpGetNext(vbl);
            vbl = pdu.getVarbindList();
            if (!pdu.isErrorStatusSet()) {
                Enumeration vbs = vbl.varbinds();
                while (vbs.hasMoreElements()) {
                    ManagedInstance mi = new ManagedInstance((Varbind)vbs.nextElement());
                    result.put(mi.name, mi);
                }
            }
        } catch (SnmpMessageException e) {
            /* We just catch the exception and return.   */
            /* No harm done. At most an empty table.     */
        }
        return result;
    }

    /**
     * This function will a row in am table.
     * @param v The indicator for the Vector return type and the   
     * managed objects are added to this vector.   
     * NOTE: Use 'new Vector()' from the caller. Otherwise, managed        
     * object simply get added.  
     * @param vbl The variable bindings list of the column to be
     * retreived.
     * @param index The index of the row to be retrieved.
     * @return The vector containing the instances of the row.
     */
    public Vector coreGetRow(Vector v, VarbindList vbl, String index)
                                        throws SnmpValueException {
        Vector result = new Vector();
        try {
            SnmpPdu pdu = manager.invokeSnmpGet(vbl);
            vbl = pdu.getVarbindList();
            if (!pdu.isErrorStatusSet()) {
                Enumeration vbs = vbl.varbinds();
                while (vbs.hasMoreElements()) {
                    result.insertElementAt(new ManagedInstance((Varbind)vbs.nextElement()), result.size());
                }
            }
        } catch (SnmpMessageException e) {
            /* We just catch the exception and return.   */
            /* No harm done. At most an empty table.     */
        }
        return result;
    }

    public Vector coreGetNextRow(Vector v, VarbindList vbl, String index)
                                        throws SnmpValueException {
        Vector result = new Vector();   
        try {
            SnmpPdu pdu = manager.invokeSnmpGetNext(vbl);
            vbl = pdu.getVarbindList();
            if (!pdu.isErrorStatusSet()) {
                Enumeration vbs = vbl.varbinds();
                while (vbs.hasMoreElements()) {
                    result.insertElementAt(new ManagedInstance((Varbind)vbs.nextElement()), result.size());   
                }
            }
        } catch (SnmpMessageException e) { 
            /* We just catch the exception and return.   */
            /* No harm done. At most an empty table.     */
        }
        return result;
    }
    /**
     * Test application that is able to read data from the sysORTable of
     * a remote SNMP agent.
     * The arguments are parsed in accordance to the Arguments class.
     * @see Arguments
     */
    public static void main(String args[]) {
        try {
            final String str_sysORTable = "1.3.6.1.2.1.1.9";
            final String str_sysOREntry = "1.3.6.1.2.1.1.9.1";
            final String str_sysORIndex = "1.3.6.1.2.1.1.9.1.1";
            final String str_sysORID = "1.3.6.1.2.1.1.9.1.2";
            final String str_sysORDescr = "1.3.6.1.2.1.1.9.1.3";
            final String str_sysORUpTime = "1.3.6.1.2.1.1.9.1.4";
            final ManagedInstance sysORID = new ManagedInstance(str_sysORID);
            final ManagedInstance sysORDescr = new ManagedInstance(str_sysORDescr);
            final ManagedInstance sysORUpTime = new ManagedInstance(str_sysORUpTime);
            final Hashtable columns = new Hashtable();
            columns.put("sysORIndex",  new Integer(1));
            columns.put("sysORID",  new Integer(2));
            columns.put("sysORDescr",  new Integer(3));
            columns.put("sysORUpTime",  new Integer(4));
            Arguments a = new Arguments("SnmpGet");
            a.parse(args);
            SnmpManager manager = a.generateManager();
            Table managedObject = new Table(manager);
            System.out.println("Retrieve a table as a HashTable");
            VarbindList vbl = new VarbindList();
            vbl.addVarbind(sysORID.createVarbind());
            vbl.addVarbind(sysORDescr.createVarbind());
            vbl.addVarbind(sysORUpTime.createVarbind());
            Hashtable h = managedObject.coreGetTable(new Hashtable(), vbl);
            Enumeration h_cells = h.elements();
            while (h_cells.hasMoreElements()) {
                 ManagedInstance mi = (ManagedInstance)h_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            }
            System.out.println("Retrieve a table as a Vector");
            Vector v = managedObject.coreGetTable(new Vector(), vbl);
            Enumeration v_cells = v.elements();
            while (v_cells.hasMoreElements()) {   
                 ManagedInstance mi = (ManagedInstance)v_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            } 
            System.out.println("Retrieve a column as HashTable");
            h = managedObject.coreGetColumn(new Hashtable(), str_sysORID);
            h_cells = h.elements();
            while (h_cells.hasMoreElements()) {
                 ManagedInstance mi = (ManagedInstance)h_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            }
            System.out.println("Retrieve a column as Vector");
            v = managedObject.coreGetColumn(new Vector(), str_sysORID);   
            v_cells = v.elements();
            while (v_cells.hasMoreElements()) { 
                 ManagedInstance mi = (ManagedInstance)v_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            }
            System.out.println("Retrieve a row as HashTable");
            h = managedObject.coreGetRow(new Hashtable(), vbl, "1");
            h_cells = h.elements();
            while (h_cells.hasMoreElements()) {
                 ManagedInstance mi = (ManagedInstance)h_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            }
            System.out.println("Retrieve a row as Vector");
            v = managedObject.coreGetRow(new Vector(), vbl, "1");
            v_cells = v.elements(); 
            while (v_cells.hasMoreElements()) {
                 ManagedInstance mi = (ManagedInstance)v_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            }
            System.out.println("Retrieve the next row as HashTable");
            h = managedObject.coreGetNextRow(new Hashtable(), vbl, "1");
            h_cells = h.elements();
            while (h_cells.hasMoreElements()) {   
                 ManagedInstance mi = (ManagedInstance)h_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            }   
            System.out.println("Retrieve the next row as Vector");
            v = managedObject.coreGetNextRow(new Vector(), vbl, "1");
            v_cells = v.elements();
            while (v_cells.hasMoreElements()) {
                 ManagedInstance mi = (ManagedInstance)v_cells.nextElement();
                 System.out.println("Name: " + mi.name + " value: " + mi.value);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}

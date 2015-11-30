package com.mod_snmp.Snmp.Tools;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.Snmp.Manager.Arguments;
import com.mod_snmp.Snmp.Manager.ManagedInstance;
import com.mod_snmp.Snmp.Manager.SnmpManager;
import com.mod_snmp.Snmp.Manager.SnmpManagerException;
import com.mod_snmp.Snmp.Protocol.SnmpMessageException;
import com.mod_snmp.Snmp.Protocol.SnmpPdu;
import com.mod_snmp.Snmp.Values.SnmpValueException;
import com.mod_snmp.Snmp.Values.VarbindList;

/**
 * Retrieving the managed instances provided.
 * This class is an example on how to retrieve data with an SNMP-GET
 * request from a remote SNMP agent.   
 */
public class SnmpScalar {

    /**
     * The SnmpManager contains all info of the target.
     */
    private SnmpManager manager;

    public SnmpScalar(SnmpManager mgr) {
        manager = mgr;
    }

    /**
     * <PRE>
     * This function will retrieve the instance.
     * <PRE>
     */
    public ManagedInstance get(ManagedInstance req) throws
                        SnmpValueException, SnmpManagerException, SnmpMessageException {
        try {
            VarbindList vbl = new VarbindList();
            vbl.addVarbind(req.createVarbind());
            SnmpPdu pdu = manager.invokeSnmpGet(vbl);
            vbl = pdu.getVarbindList();
            if (!pdu.isErrorStatusSet()) {
                return new ManagedInstance(vbl.varbindAt(0));
            }
            throw new SnmpManagerException(pdu);
        } catch (SnmpMessageException e) {
            /* We just catch the exception and return.   */
            /* No harm done. At most an empty table.     */
        }
        throw new SnmpManagerException();
    }
    /**
     * <PRE>
     * This function will change (set) the instance.
     * @param value The value to which the instance is set.
     * </PRE>
     */
    public void set(int value) {
    }


    public static void main(String args[]) {
        final String sysLocation = "1.3.6.1.2.1.1.6";
        try {
            Arguments a = new Arguments("SnmpScalar");
            a.parse(args);
            SnmpManager manager = a.generateManager();
            SnmpScalar managedObject = new SnmpScalar(manager);
            ManagedInstance request = new ManagedInstance(sysLocation);
            ManagedInstance instance = managedObject.get(request);
            System.out.println("Scalar " + instance.name +
                                         ":" + instance.value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}

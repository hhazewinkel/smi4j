package com.mod_snmp.snmp.tools;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.snmp.manager.Arguments;
import com.mod_snmp.snmp.manager.ManagedInstance;
import com.mod_snmp.snmp.manager.Scalar;
import com.mod_snmp.snmp.manager.SnmpManager;
import com.mod_snmp.snmp.manager.SnmpManagerException;
import com.mod_snmp.snmp.values.SnmpValueException;

/**
 * The SysLocation managed instance.
 * An example on how to use the Scalar class of the Manager package.
 */
public class SysLocation extends Scalar {

    /**
     * Default constructor for the management application
     * for a specific SnmpManager.
     * @param mgr The SNMP manager to be used for the communication
     * with the remote SNMP agent.
     * @param inst The managed instance.
     */
    public SysLocation(SnmpManager mgr, ManagedInstance inst)
                                                throws SnmpValueException {
        super(mgr, inst);
    }

    public static void main(String args[]) {
        final String sysLocation = new String("1.3.6.1.2.1.1.6");
        try {
            Arguments a = new Arguments("java Snmp.Manager.Scalar");
            a.parse(args);
            SysLocation managedObject = new SysLocation(a.generateManager(),
                                        new ManagedInstance(sysLocation));
            System.out.println("-------- SNMP-GET --------------------------");
            try {
                ManagedInstance instance = managedObject.get();
                System.out.println("SysLocation " + instance);
            } catch (SnmpManagerException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("-------- SNMP-GET-NEXT ---------------------");
            try {
                ManagedInstance instance = managedObject.getNext();
                System.out.println("SysLocation " + instance);
            } catch (SnmpManagerException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}

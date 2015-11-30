package com.mod_snmp.Snmp.Tools;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.Snmp.Manager.Arguments;
import com.mod_snmp.Snmp.Manager.SnmpManager;
import com.mod_snmp.Snmp.Protocol.SnmpMessageException;
import com.mod_snmp.Snmp.Protocol.SnmpPdu;
import com.mod_snmp.Snmp.Values.Varbind;
import com.mod_snmp.Snmp.Values.VarbindList;

import java.util.Enumeration;

/**
 * Retrieving the managed instances provided.
 * This class is an example on how to retrieve data with an SNMP-GET
 * request from a remote SNMP agent.
 */
public class SnmpGet {
    private SnmpManager mgr;

    private SnmpGet(SnmpManager a) {
         mgr = a;
    }

    public void get(VarbindList vbl) throws SnmpMessageException {
        SnmpPdu pdu = mgr.invokeSnmpGet(vbl);
        VarbindList recv_vbl = pdu.getVarbindList();
        if (pdu.isErrorStatusSet()) {
            System.out.println(
                         (recv_vbl.varbindAt(pdu.getErrorIndex() - 1)).name +
                                                " : " + pdu.getErrorStatus(""));
        } else {
            Enumeration recv_vbs = recv_vbl.elements();
            while (recv_vbs.hasMoreElements()) {
                Varbind vb = (Varbind)recv_vbs.nextElement();
                    System.out.println(vb.toString());
            }
        }
    }

    public static void main(String args[]) {
        try {
            Arguments a = new Arguments("SnmpGet");
            a.parse(args);
            SnmpGet appl = new SnmpGet(a.generateManager());
            appl.get(a.getVarbindList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}

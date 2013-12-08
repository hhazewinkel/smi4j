package com.mod_snmp.Snmp.Tools;

import com.mod_snmp.Snmp.Manager.Arguments;
import com.mod_snmp.Snmp.Manager.SnmpManager;
import com.mod_snmp.Snmp.Protocol.SnmpMessageException;
import com.mod_snmp.Snmp.Protocol.SnmpPdu;
import com.mod_snmp.Snmp.Values.Varbind;
import com.mod_snmp.Snmp.Values.VarbindList;

import java.util.Enumeration;

/**
 * Changing the value of a managed instance.
 * This class is an example on how to change data with an SNMP-SET
 * request from a remote SNMP agent.
 */
public class SnmpSet {
    private SnmpManager mgr;

    private SnmpSet(SnmpManager a) {
         mgr = a;
    }

    public void set(VarbindList vbl) throws SnmpMessageException {
        SnmpPdu pdu = mgr.invokeSnmpSet(vbl);
        VarbindList recv_vbl = pdu.getVarbindList();
        if (pdu.isErrorStatusSet()) {
            System.out.println("SNMP-SET failure: " + pdu.getErrorStatus("") + " : "+
                                    recv_vbl.varbindAt(pdu.getErrorIndex() - 1));
            /* Just get the real values */
            pdu = mgr.invokeSnmpGet(vbl);
            if (pdu.isErrorStatusSet()) {
                System.out.println("SNMP-GET " + pdu.getErrorStatus("") + " : " +
                      recv_vbl.varbindAt(pdu.getErrorIndex() - 1));
            } else {
                System.out.println("SNMP-GET " + pdu.getErrorStatus("") + ":");
                recv_vbl = pdu.getVarbindList();
                Enumeration recv_vbs = recv_vbl.elements();
                while (recv_vbs.hasMoreElements()) {
                    Varbind vb = (Varbind)recv_vbs.nextElement();
                        System.out.println(vb.toString());
                }
            }
        } else {
            System.out.println("SNMP-SET succeeded");
        }
    }


    public static void main(String args[]) {
        try {
            Arguments a = new Arguments("SnmpSet");
            a.parse(args);
            SnmpSet appl = new SnmpSet(a.generateManager());
            appl.set(a.getVarbindList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}

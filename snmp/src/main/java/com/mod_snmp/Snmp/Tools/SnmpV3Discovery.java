package com.mod_snmp.Snmp.Tools;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.Snmp.Manager.Arguments;
import com.mod_snmp.Snmp.Manager.SnmpManager;

/**
 * Retrieving the engine id of a remote SNMP agent.
 * This class is an example on how to retrieve the SNMP engine Id from
 * a remote SNMP agent.   
 * NOTE: this is normally not needed, since the generation of an
 * SnmpManager instance takes care for this. During the generation of the
 * instance it performs the 'discovery' based on the need by the SNMP
 * version used.
 */
public class SnmpV3Discovery {

    public static void main(String args[]) {
        try {
            Arguments a = new Arguments("SnmpV3Discovery");
            a.parse(args);
	    SnmpManager appl = a.generateManager();
            appl.dump();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}

package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.Snmp.Utils.TypeConversions;

/**
 * Message parameters for the community based security model.
 * This is a class that is driven from the community based security model.
 */
public class SecParametersCommunity implements SecParametersInterface {
    public final static int number = 0;
    public final static String version = "Community-Based: 0";
    private final static byte[] zeroLength = new byte[0];

    /**
     * The community security parameters
     */
    protected byte[] community;

    /**
     * Security parameter constructor.
     */
    public SecParametersCommunity() {
        community = zeroLength;
    }
    public SecParametersCommunity(byte[] c) {
        community = c;
    }
    public SecParametersCommunity(String c) {
        community = c.getBytes();
    }
    public SecParametersInterface makeSecurityParameters() {
        return new SecParametersCommunity();
    }
    public String toString() {
        return "Community-Based: 0";
    }
    public String getName() {
        return new String(community);
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
	System.out.println("  Security Param.: " + this);
        try {
            System.out.println("  community       : " +
                                TypeConversions.bytes2HexString(community));
        } catch (NullPointerException e) {
            System.out.println("<null>");
        }
    }
}

package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.Snmp.Utils.TypeConversions;

/**
 * Message parameters for the user based security model.
 * This is a class that is driven from the user based security model.
 */
public class SecParametersUsm implements SecParametersInterface {
    private final static byte[] zeroLength = new byte[0];

    /**
     * The USM security parameters
     */
    protected byte[] username;
    protected byte[] engineId;
    protected int eBoots;
    protected int eTime;
    protected byte[] authValue;
    protected byte[] privValue;
    protected int authPos = 0;
    private LocalizedKeys lKeys;
    /**
     * Security parameter constructor.
     */
    public SecParametersUsm() {
        username = zeroLength;
        engineId = zeroLength;
        eBoots = 0;
        eTime = 0;
        authValue = zeroLength;
        privValue = zeroLength;
    }
    public SecParametersUsm(byte[] u, byte[] id, int boots, int time,
                            boolean auth, boolean priv, LocalizedKeys lk) {
        username = u;
        engineId = id;
        eBoots = boots;
        eTime = time;
        if (auth) {
            authValue = new byte[12];
        } else {
            authValue = zeroLength;
        }
        if (priv) {
            privValue = new byte[12];
        } else {
            privValue = zeroLength;
        }
        lKeys = lk;
    }
    public String toString() {
        return "USM: 3";
    }
    public String getName() {
        return new String(username);
    }
    public void setLocalizedKeys(LocalizedKeys lk) {
        lKeys = lk;
    }
    public byte[] generateHMac(byte[] b) throws SecModelException {
        try {
            return lKeys.generateHMac(b);
        } catch (NullPointerException e) {
            throw new SecModelException("no localized key available");
        }
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
	System.out.println("  Security Param.: " + this);
        System.out.println("    username       : " + new String(username));
        System.out.print  ("    engine Id      : ");
        try {
            System.out.println(TypeConversions.bytes2HexString(engineId));
        } catch (NullPointerException e) {
            System.out.println("<null>");
        }
        System.out.println("    engine Boots   : " + eBoots);
        System.out.println("    engine time    : " + eTime);
        System.out.print  ("    auth param.    : ");
        try {
            System.out.println(TypeConversions.bytes2HexString(authValue));
        } catch (NullPointerException e) {
            System.out.println("<null>");
        }
        System.out.print  ("    priv param.    : ");
        try {
            System.out.println(TypeConversions.bytes2HexString(privValue));
        } catch (NullPointerException e) {
            System.out.println("<null>");
        }
    }
}

package com.mod_snmp.snmp.protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Session specific parameters for community based SNMP session.
 */
public class SecSessDataCommunity implements SecSessDataInterface {
    protected byte[] community;

    public SecSessDataCommunity() {
    }
    public void fixup(SnmpMessage msg, User u) throws SecModelException {
        community = u.name;
    }
    public void setUser(User u) {
        community = u.name;
    }
    public SecParametersInterface makeSecurityParameters() {
        return new SecParametersCommunity(community);
    }
    public SecParametersInterface makeDiscoveryParameters() {
        return new SecParametersCommunity();
    }
    public void update(SecParametersInterface spi) {
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("  Security Parameters - Community");
        System.out.println("    community        : " + new String(community));
    }
}

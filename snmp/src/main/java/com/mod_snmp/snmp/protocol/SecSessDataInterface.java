package com.mod_snmp.snmp.protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Session specific parameters interface.
 */
public interface SecSessDataInterface {
    public void fixup(SnmpMessage msg, User u) throws SecModelException;
    public SecParametersInterface makeSecurityParameters();
    public SecParametersInterface makeDiscoveryParameters();
    public void update(SecParametersInterface spi);
    public void dump();
}

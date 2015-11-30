package com.mod_snmp.snmp.protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * Session specific parameters for user based SNMP session.
 */
public class SecSessDataUsm implements SecSessDataInterface {
    private final static byte[] zeroLength = new byte[0];

    protected byte[] username;
    protected EngineId eId;
    protected int eBoots;
    protected EngineTime eTime;
    protected boolean authentication;
    protected boolean privacy;
    private LocalizedKeys lKeys;

    public SecSessDataUsm() {
        username = zeroLength;
        eId = EngineId.zeroLengthEngineId;
        eBoots = 0;
        eTime = new EngineTime();
    }
    /**
     * Fixup method that enables an 'update' of parameters
     * that hardly change during a session.
     */
    public void fixup(SnmpMessage msg, User u) throws SecModelException {
        SecParametersUsm usmParm = (SecParametersUsm)msg.secInfo;
        username = u.name;
        eId = new EngineId(usmParm.engineId);
        authentication = u.hasAuthentication();
        privacy = u.hasPrivacy();
        lKeys = new LocalizedKeys(u, eId);
        LocalizedKeysTable.insert(new String(u.name),
                                new String(usmParm.engineId), lKeys);
    }
    /**
     * Factory class for the associated security parameters.
     */
    public SecParametersInterface makeSecurityParameters() {
        return new SecParametersUsm(username, eId.getValue(),
                                    eBoots, eTime.getTime(),
                                    authentication, privacy, lKeys);
    }
    /**
     * Factory class for the associated security parameters.
     */
    public SecParametersInterface makeDiscoveryParameters() {
        return new SecParametersUsm(zeroLength, zeroLength, 0, 0,
                                    false, false, null);
    }
    /**
     * Data update function after a message from an
     * authorative engine has arrived.
     * This method should only update the dynamic 
     * content that is changed almost always after each request.
     */
    public void update(SecParametersInterface spi) {
        SecParametersUsm usmParm = (SecParametersUsm)spi;
        eBoots = usmParm.eBoots;
        eTime.updateTime(usmParm.eTime);
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("  Security Parameters - USM");
        System.out.println("    username         : " + new String(username));
        System.out.println("    engineId         : " + eId);
        System.out.println("    engineBoots      : " + eBoots);
        eTime.dump();
    }
}

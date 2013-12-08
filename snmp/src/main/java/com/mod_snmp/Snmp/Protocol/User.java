package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import java.security.NoSuchAlgorithmException;

/**
 * This class represents a user of the SNMP application. It
 * can either be the community or the user in the sense of SNMPv3.
 * The user is constructed by an upper layer (above the SNMP protocol
 * layer) and needs to define its name, the SNMP protocol version and
 * for SNMPv3 only the security model it wants to use.
 * When no protocol version is defined it assumed the default SNMPv1.
 * For SNMPv1 and SNMPv2c the username  is used as the community string.
 * If the protocol is SNMPv3  a protocol/password combination can be added
 * to enable authentication and privacy.
 * Privacy cannot be used if no authentication is used and protocols/
 * password combination do not neccessarily be the same.
 * This User class can support all protocol version and
 * security models one could think of, but at the required
 * discovery phase the protocol versions and security models
 * are tested whether the SnmpEngine supports it.
 */
public class User implements SnmpMessageInterface,
                             SecModelNumberInterface {
    /**
     * A predefined user that could be used as the discovery user.
     */
    public final static User discoveryUser =
                                 new User("", SNMP_VERSION_3);
    /**
     * The name of the user.
     */
    protected byte[] name;
    /**
     * The SNMP protocol version.
     */
    protected int version;
    /**
     * The security model to be used for SNMPv3.
     */
    protected int secModel;
    /**
     * The authentication protocol/password combination.
     */
    protected UserKey authKey;
    /**
     * The privacy protocol/password combination.
     */
    protected UserKey privKey;

    /**
     * Create the User by its username as a String.
     * Protocol version defaults to SNMPv1.
     */
    public User(String n) {
        name = n.getBytes();
        setParameters(SNMP_VERSION_1);
    }
    /**
     * Create the User by its username in a byte-array.
     * Protocol version defaults to SNMPv1.
     */
    public User(byte[] n) {
        name = n;
        setParameters(SNMP_VERSION_1);
    }
    /**
     * Create the User by its protocol version and the String
     * that defines the username.
     * If the protocol version SNMPv3 is defined the default
     * security model is USM.
     */
    public User(String n, int v) {
	name = n.getBytes();
        setParameters(v);
    }
    /**
     * Create the User by its protocol version and byte-array
     * that defines the username.
     * If the protocol version SNMPv3 is defined the default
     * security model is USM.
     */
    public User(byte[] n, int v) {
        name = n;
        setParameters(v);
    }
    /**
     * Create the user with a protocol version and 
     * security model.
     * Only the protocol version 3 supports multiple security models.
     */
    public User(String n, int v, int s) {
        name = n.getBytes();
        version = v;
        secModel = s;
    }
    /**
     * Create the User and its authentication parameters.
     * This is only usefull is the protocol version is SNMPv3.
     */
    public User(String n, String authprot, String authpass)
                                                throws UserException {
        name = n.getBytes();
        setParameters(SNMP_VERSION_3);
        try {
            authKey = UserKeyAuth.create(authprot, authpass);
        } catch (NoSuchAlgorithmException e) {
            throw new UserException(e.getMessage());
        }
    }
    /**
     * Create the User and its authentication and privacy
     * parameters.
     * This is only usefull is the protocol version is SNMPv3.
     */
    public User(String n, String authprot, String authpass,
                                String privprot, String privpass)
                                                throws UserException {
        name = n.getBytes();
        setParameters(SNMP_VERSION_3);
        setAuthParameters(authprot, authpass);
        setPrivParameters(privprot, privpass);
    }
    /**
     * Internal function that processes the protocol version and
     * security model.
     */
    private void setParameters(int v) { 
        version = v;
        switch (v) {
        case SNMP_VERSION_1:
            secModel = SECMODEL_V1;
            break;
        case SNMP_VERSION_2C:
            secModel = SECMODEL_V2C;
            break;
        default:
            secModel= SECMODEL_USM;
            break;
        }
    }
    /**
     * Set the authentication parameters for this user.
     * This is only usefull is the protocol version is SNMPv3.
     */
    public void setAuthParameters(String authprot, String authpass)
                                                throws UserException {
        try {
            authKey = UserKeyAuth.create(authprot, authpass);
        } catch (NoSuchAlgorithmException e) {
            throw new UserException(e.getMessage());
        }
    }
    /**
     * Set the privacy parameters for this user.
     * This is only usefull is the protocol version is SNMPv3.
     */
    public void setPrivParameters(String privprot, String privpass)
                                                throws UserException {
        if (!hasAuthentication()) {
             throw new UserException("Privacy without authentication " +
                                      "is not allowed");
        }
        try {
            privKey = UserKeyPriv.create(privprot, privpass);
        } catch (NoSuchAlgorithmException e) {
            throw new UserException(e.getMessage());
        }
    }
    /**
     * Informational method whether the User has an authentication
     * protocol and password defined.
     */
    public boolean hasAuthentication() {
        return (authKey != null);
    }
    /**
     * Informational method whether the User has a privacy
     * protocol and password defined.
     */
    public boolean hasPrivacy() {
        return (privKey != null);
    }
    /**
     * Retrieve the SNMP protocol version number of the user.
     */
    public int getProtocol() {
        return version;
    }
    /**
     * Retrieve the SNMP security model number of the user.
     */
    public int getSecModel() {
        return secModel;
    }
    /**
     * Retrieve the user name as a String.
     */
    public String toString() {
        return new String(name);
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("    User             : " + name);
        System.out.println("        Version      : " +
                                SnmpMessage.versionToString(version));
        System.out.println("        Sec. Model   : " +
                                SecuritySystem.toString(secModel));
        try {
            authKey.dump();
        } catch (NullPointerException e) {
            System.out.println("        Auth Key     : <null>");
        }
        try {
            privKey.dump();
        } catch (NullPointerException e) {
            System.out.println("        Priv Key     : <null>");
        }
    }
}

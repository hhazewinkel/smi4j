package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * The security model for community based authorization.
 */
public class SecModelCommunity implements SecModelInterface {
    private final static boolean debug = false;
    private static int number;

    public SecModelCommunity(int v) {
        number = v;
    }

    public int getSecModelNumber() {
        return number;
    }
    public String toString() {
        return "Community(#" + number + ")";
    }
    public boolean isSecModel(int m) {
        return (m == number);
    }
    /**
     * Create the session specific security model parameters.
     * This way an SnmpSession can have its own security data
     * but still share the same model in an SnmpEngine.
     */
    public SecSessDataInterface createSessData() {
        return new SecSessDataCommunity();
    }
    /**
     * Create/Decode security model specific parameters.
     */
    public void decodeSecurityParameters(BerDecoder raw_packet,
                                            SnmpMessage msg)
                                            throws BerException {
        if (debug) dump("Decode Security Parameters");
        /**
         * parsing security model header data.
         */
        msg.secInfo = new SecParametersCommunity(
                                         raw_packet.decodeSnmpOctetStr());
        if (debug) dump("        Community: " + msg.secInfo.getName());
        if (debug) dump("Decode Security Parameters done");
    }

    /**
     * Authenticate the decoded SnmpMessage.
     */
    public void decodeAuthentication(BerDecoder raw_packet,
                                            SnmpMessage msg)
                                            throws SecModelException {
        if (debug) dump("No Decode Authentication");
    }

    public int encodeSecurityParameters(BerEncoderReverse bre,
                                            SnmpMessage msg)
                                            throws BerException {
        if (debug) dump("Encode Security Parameters");
        try {
            SecParametersCommunity community = (SecParametersCommunity)msg.secInfo;
            if (community == null) {
                community = new SecParametersCommunity();
            }
            return encode(bre, new byte[1], community);
        } catch (NullPointerException e) {
            throw new BerException("Cannot encode security parameters");
        }
    }
    private int encode(BerEncoderReverse bre, byte[] name,
                                            SecParametersCommunity community)
                                            throws BerException {
        int start = bre.position();
        int end = bre.encodeSnmpOctetStr(community.community);
        return (end - start);
    }

    /**
     * Authenticate the encoded Snmp Message.
     */
    public byte[] encodeAuthentication(BerEncoderReverse bre,
                                            SnmpMessage msg)
                                            throws SecModelException {
        if (debug) dump("No Encode Authentication");
        return bre.toByteArray();
    }

    public BerDecoder decrypt(BerDecoder raw_packet, SnmpMessage msg) {
        if (debug) dump("No Decryption");
        return raw_packet;
    }
    public BerEncoderReverse encrypt(BerEncoderReverse bre, SnmpMessage msg) {
        if (debug) dump("No Encryption");
        return bre;
    }
    /**
     * Debug dumper.
     */
    private void dump(String s) {
        System.out.println("SM-community:" + s);
    }
}

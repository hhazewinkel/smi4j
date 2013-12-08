package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.Snmp.Utils.TypeConversions;

import java.util.Random;

/**
 * The user based security model, also known as USM which is
 * standardized in the IETF.
 */
public class SecModelUsm implements SecModelInterface {
    private final static boolean debug = false;
    private final static int number = 3;
    private final static String version = "USM(#3)";

    private int saltCount;

    public SecModelUsm() {
        /* In case we need some decoder initialization like a scratch pad. */
        Random rand = new Random();
        saltCount = rand.nextInt();
    }

    public int getSecModelNumber() {
        return number;
    }
    public String toString() {
        return version;
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
        return new SecSessDataUsm();
    }

    public SecParametersInterface makeSecurityParameters() {
        return new SecParametersUsm();
    }
    /**
     * Compare authentication parameters.
     */
    public boolean compareAuthentication(byte[] msgHash,
                                            byte[] authValue) {
        if ((authValue.length != 12) || (msgHash.length < 12)) {
            return false;
        }
        for (int i = 0 ; i < 12 ; i++) {
            if (msgHash[i] != authValue[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Create/Decode security model specific parameters.
     */
    public void decodeSecurityParameters(BerDecoder raw_packet,
                                            SnmpMessage msg)
                                            throws BerException {
        if (debug) dump("Decode Security Parameters");
        /**
         * Setup.
         */
        SecParametersUsm usmParm = new SecParametersUsm();
        msg.secInfo = usmParm;
        /**
         * parsing security model header data.
         */
        int length = raw_packet.decodeSequenceHeader();
        if (debug) dump("      length:" + length);
        usmParm.engineId = raw_packet.decodeSnmpOctetStr();
        if (debug) dump("    engineId: " +
                        TypeConversions.bytes2HexString(usmParm.engineId));
        usmParm.eBoots = raw_packet.decodeSnmpInteger();
        if (debug) dump("engine boots: " + usmParm.eBoots);
        usmParm.eTime = raw_packet.decodeSnmpInteger();
        if (debug) dump(" engine time: " + usmParm.eTime);
        usmParm.username = raw_packet.decodeSnmpOctetStr();
        if (debug) dump("   user name: " + usmParm.getName());
        /* The security parameters. */
        usmParm.authValue = raw_packet.decodeSnmpOctetStr();
        if (debug) dump(" auth parms.: " +
                        TypeConversions.bytes2HexString(usmParm.authValue));
        usmParm.privValue = raw_packet.decodeSnmpOctetStr();
        if (debug) dump(" priv parms.: " +
                        TypeConversions.bytes2HexString(usmParm.privValue));
        if (debug) dump("Decode Security Parameters done");
    }

    /**
     * Authenticate the decoded SnmpMessage.
     */
    public void decodeAuthentication(BerDecoder raw_packet,
                                            SnmpMessage msg)
                                            throws SecModelException {
        if (debug) dump("Decode Authentication");
        /**
         * Setup.
         */
        SecParametersUsm usmParm = (SecParametersUsm)msg.secInfo;
        LocalizedKeys kul = LocalizedKeysTable.lookup(usmParm.username,
                                                              usmParm.engineId);
        byte[] msgHash = kul.generateHMac(new byte[0]);
        if (compareAuthentication(msgHash, usmParm.authValue)) {
            throw new SecModelException("Received message is not authentic");
        }
        if (debug) dump("Decode Authentication done");
    }

    public int encodeSecurityParameters(BerEncoderReverse bre,
                                            SnmpMessage msg)
                                            throws BerException {
        if (debug) dump("Encode Security Parameters");
        SecParametersUsm usmParm = (SecParametersUsm)msg.secInfo;
        int start = bre.position();
        usmParm.authPos = bre.encodeSnmpOctetStr(usmParm.privValue);
        bre.encodeSnmpOctetStr(usmParm.authValue);
        bre.encodeSnmpOctetStr(usmParm.username);
        bre.encodeSnmpInteger(usmParm.eTime);
        bre.encodeSnmpInteger(usmParm.eBoots);
        int end = bre.encodeSnmpOctetStr(usmParm.engineId);
        end = bre.encodeHeader(SNMP_SEQUENCE, (end - start));
        return (end - start);
    }

    /**
     * Authenticate the encoded Snmp Message.
     */
    public byte[] encodeAuthentication(BerEncoderReverse bre,
                                            SnmpMessage msg)
                                            throws SecModelException {
        if (debug) dump("Encode Authentication");
        /**
         * Setup.
         */
        SecParametersUsm usmParm = (SecParametersUsm)msg.secInfo;
        byte[] packet = bre.toByteArray();
        /* Start of authentication value is calculated from a
         * postion 'usmParm.authPos' memorized while writing reverse.
         * This is the end of the authentication value. So the
         * calculation for the start of the authentication value is:
         * Length of the packet - the start of the value written
         * backwards (This is the end position of the authvalue.
         * We correct still with -12 to get to the begining.
         */
        int startOfAuthValue = (packet.length - usmParm.authPos) - 12;
        if (debug) dump("Start auth position: (" + packet.length + "-" +
                        usmParm.authPos + ")-11=" + startOfAuthValue);
        byte[] msgHash = usmParm.generateHMac(packet);
        if (debug) dump("HMAC: " + TypeConversions.bytes2HexString(msgHash));
        int i = 0;
        while (i < 12) {
            packet[startOfAuthValue++] = msgHash[i++];
        }
        if (debug) dump("Encode Authentication done");
        return packet;
    }

    public BerDecoder decrypt(BerDecoder raw_packet, SnmpMessage msg) {
        if (debug) dump("Decrypt");
        SecParametersUsm usmParm = (SecParametersUsm)msg.secInfo;
        System.out.println(" Decrypton salt: " +
                TypeConversions.bytes2HexString(usmParm.privValue));
        if (debug) dump("Encrypt done");
        return raw_packet;
    }
    public BerEncoderReverse encrypt(BerEncoderReverse bre, SnmpMessage msg) {
        if (debug) dump("Encrypt");
        SecParametersUsm usmParm = (SecParametersUsm)msg.secInfo;
        byte[] salt = new byte[8]; /* 8 is the salt length */
        TypeConversions.int2Bytes(salt, usmParm.eBoots, 0);
        TypeConversions.int2Bytes(salt, saltCount++, 4);
        usmParm.privValue = salt;
        System.out.println(" Decrypton salt: " +
                TypeConversions.bytes2HexString(usmParm.privValue));
        if (debug) dump("Encrypt done");
        return bre;
    }

    /**
     * Debug dumper.
     */
    private void dump(String s) {
        System.out.println("SM-USM: " + s);
    }
}

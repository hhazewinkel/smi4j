package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.Snmp.Utils.TypeConversions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Core class generating keys for password associated with
 * a specific authentication/privacy protocols.
 * This class generate passpphrses as defined in RFC XXXX.
 */
public class UserKey {
    public static final UserKey zeroLengthUserKey = new UserKey();
    protected String protocol;
    private String passPhrase;
    private byte[] passKey = null;               /* Also named KU.  */

    public UserKey() {
        protocol = "UNKNOWN";
        passPhrase = "";
        passKey = new byte[0];
    }
    public UserKey(String prot) {
        protocol = prot;
        passPhrase = "";
        passKey = new byte[0];
    }
    public UserKey(String prot, String phrase)
                                throws NoSuchAlgorithmException {
        protocol = prot;
        passPhrase = "";
        passKey = new byte[0];
        generatePassPhraseKey(phrase);
    }
    public void generatePassPhraseKey(String passPhrase)
                                throws NoSuchAlgorithmException {
        int passPhraseLength = passPhrase.length();
        byte[] passPhraseBuf = new byte[passPhraseLength];
        for (int i = 0; i < passPhraseLength; i++) {
             passPhraseBuf[i] = (byte)(0xFF & passPhrase.charAt(i));
        }

        byte[] buffer = new byte[64];
        MessageDigest md = MessageDigest.getInstance(protocol);
        md.reset();
        int passPhraseIndex = 0;
        for (int count = 0; count < 1048576; count += 64) {
             for (int i = 0; i < 64 ; i++) {
                 buffer[i] = passPhraseBuf[ passPhraseIndex++ % passPhraseLength ];
             }
             md.update(buffer, 0, buffer.length);
        }
        passKey = md.digest();
    }
    public byte[] getPassPhraseKey() {
        return passKey;
    }
    public String getProtocol() {
        return protocol;
    }
    public String toString() {
        return protocol + ":" + passPhrase;
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        Protocol        : " + protocol + ":" + passPhrase);
        System.out.println("        PassPhraseKey   : " + TypeConversions.bytes2HexString(passKey));
    }
    public static void main(String args[]) {
        if (args.length != 2) {
            usage();
        }
        try {
            UserKey ap = new UserKey(args[0]);
            ap.generatePassPhraseKey(args[1]);
            ap.dump();
        } catch (NoSuchAlgorithmException e) {
            usage();
            System.err.println(e.getMessage());
        }
    }
    private static void usage() {
        System.err.println("Usage: java Snmp/Protocol/UserKey [MD5|SHA1] passphrase");
    }
}

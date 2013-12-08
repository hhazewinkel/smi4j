package com.mod_snmp.Snmp.Protocol;

import com.mod_snmp.Snmp.Utils.TypeConversions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The LocalizedKeys class is a combination of 2 seperate
 * 'Localized Key's.
 * It provides the localized keys for authentication and privacy
 * as used in SNMPv3 User-based Security Model.
 */
public class LocalizedKeys {
    private final static boolean debug = false;

    private static final byte iPadXor = 0x36;
    private static final byte oPadXor = 0x5C;
    private String authProtocol = null;
    private byte[] authKey = null;    /* Also named KUL.  */
    private byte[] authIPad = new byte[64];
    private byte[] authOPad = new byte[64];
    private String privProtocol = null;
    private byte[] privKey = null;    /* Also named KUL.  */
    private byte[] privIPad = new byte[64];
    private byte[] privOPad = new byte[64];

    /**
     * Default constructor for the localized authentication and
     * privacy key for a user and a specific reomte SNMP agent.
     * @param u The user for which the localized keys are created.
     * @param eId The engine id of the remote SNMP agent.
     */
    public LocalizedKeys(User u, EngineId eId) {
        if (u.hasAuthentication()) {
           setAuthKey(u.authKey, eId);
        }
        if (u.hasPrivacy()) {
            setPrivKey(u.privKey, eId);
        }
    }
    /**
     * Generate for the authentication key for the user to which the
     * LocalizedKey belongs for a specific engine id.
     */
    public void setAuthKey(UserKey k, EngineId eId) {
        authProtocol = k.getProtocol();
        authKey = generate(authIPad, authOPad,
                           authProtocol, k.getPassPhraseKey(), eId);
    }
    /**
     * Generate for the privacy key for the user to which the 
     * LocalizedKey belongs for a specific engine id.
     */
    public void setPrivKey(UserKey k, EngineId eId) {
        privProtocol = k.getProtocol();
        privKey = generate(privIPad, privOPad,
                           privProtocol, k.getPassPhraseKey(), eId);
    }
    /**
     * Internal key generation method.
     */
    private byte[] generate(byte[] ipad, byte[] opad, String protocol,
                        byte[] passKey, EngineId eId) {
        try {
            MessageDigest md = MessageDigest.getInstance(protocol);
            md.reset();
            md.update(passKey, 0, passKey.length);
            md.update(eId.value, 0, eId.value.length);
            md.update(passKey, 0, passKey.length);
            byte[] localizedKey = md.digest();
            int keylength = localizedKey.length;
            int i = 0;
            while (i < localizedKey.length) {
                ipad[i] = (byte)(localizedKey[i] ^ iPadXor);
                opad[i] = (byte)(localizedKey[i] ^ oPadXor);
                i++;
            }
            while (i < 64) {
                ipad[i] = 0 ^ iPadXor;
                opad[i] = 0 ^ oPadXor;
                i++;
            }
            return localizedKey;
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        AuthProtocol    : " +
                                    authProtocol);
        System.out.println("        AuthLocalizedKey: " +
                                    TypeConversions.bytes2HexString(authKey));
        System.out.println("        PrivProtocol    : " +
                                    privProtocol);
        System.out.println("        PrivLocalizedKey: " +
                                    TypeConversions.bytes2HexString(privKey));
    }
    /**
     * Generate the HMac for the message that is in byte[] form
     * with the localized authenticationkey.
     */
    public byte[] generateHMac(byte[] msg) {
        try {
            /* Do first digest for authIPad and prepend the message. */
            MessageDigest md1 = MessageDigest.getInstance(authProtocol);
            md1.reset();
            md1.update(authIPad, 0, authIPad.length);
            md1.update(msg, 0, msg.length);
            byte[] intermediate = md1.digest();
            if (debug) dump("digest 1: " +
                                TypeConversions.bytes2HexString(intermediate));
            /* prepend authOPad to previous digest. */
            MessageDigest md2 = MessageDigest.getInstance(authProtocol);
            md2.reset();
            md2.update(authOPad, 0, authOPad.length);
            md2.update(intermediate, 0, intermediate.length);
            byte[] digest = md2.digest();
            if (debug) dump("digest 2: " +
                                TypeConversions.bytes2HexString(digest));
            return digest;
        } catch (NoSuchAlgorithmException e) {
            /* Just do nothing. The protocol is set and tested earlier. */
        }
        return null;
    }
    /**
     * Encrypt a message in byte[] form with the generated localized
     * privacy key.
     */
    public byte[] encrypt(byte[] msg) {
        return msg;
    }
    /**
     * Decrypt a message in byte[] form with the generated localized
     * privacy key.
     */
    public byte[] decrypt(byte[] msg) {
        return msg;
    }
    /**
     * Debug dumper
     */
    private void dump(String s) {
        System.out.println("LKeys: " + s);
    }
}

package com.mod_snmp.Snmp.Protocol;

import java.security.NoSuchAlgorithmException;

/**
 * A UserKey factory for privacy protocols.
 */
public class UserKeyPriv {
    /**
     * The protocols array provides the various key protocols
     * it knowns of.
     */
    public static final String[] protocols = { "UNKOWN", "DES" };

    public static UserKey create()
                                                throws NoSuchAlgorithmException {
        return new UserKey("UNKNOWN");
    }
    public static UserKey create(String prot)
                                                throws NoSuchAlgorithmException {
        int qp = 0;
        for (int i = 0; i < protocols.length; i++) {
            if (prot.equals(protocols[i])) {
                qp = i;
                break;
            }
        }
        if (qp == 0) {
            throw new NoSuchAlgorithmException("No such key generation protocol");
        }
        return new UserKey(protocols[qp]);
    }   
    public static UserKey create(String prot, String phrase)
                                                throws NoSuchAlgorithmException {
        UserKey result = new UserKey(prot); 
        result.generatePassPhraseKey(phrase);
        return result;
    } 
    public static void main(String args[]) {
        if (args.length != 2) {
            usage();
        }
        try {
            UserKey ap = UserKeyPriv.create(args[0]);
            ap.generatePassPhraseKey(args[1]);
            ap.dump();
        } catch (NoSuchAlgorithmException e) {
            usage();
            System.err.println(e.getMessage());
        }
    }
    private static void usage() {
        System.err.println("Usage: java Snmp/Protocol/UserKeyPriv [MD5|SHA1] passphrase");
    }
}

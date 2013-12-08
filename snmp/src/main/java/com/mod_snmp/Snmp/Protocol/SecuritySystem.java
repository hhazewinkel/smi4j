package com.mod_snmp.Snmp.Protocol;

/**
 * The SecuritySystem class decodes and encodes a DatagramPacket
 * into an SnmpMessage.
 * It extends the BER class that contains all BER encoding
 * definitions. This class conatins the functions that process
 * the packages.
 */
public class SecuritySystem implements SecModelNumberInterface {
    private SecModelInterface[] array;

    public SecuritySystem() {
        array = new SecModelInterface[4];
    }
    public void addSecModel(SecModelInterface sm) {
        int m = sm.getSecModelNumber();
        array[m] = sm;
    }

    public SecModelInterface getSecModel(int model) {
        try {
            return array[model];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    public static String toString(int secsys) {
        switch (secsys) {
        case SECMODEL_V1:
            return "Community Based (v1)";
        case SECMODEL_V2C:
            return "Community Based (v2c)";
        case SECMODEL_USM:
            return "USM";
        }
        return "ANY";
    }
}

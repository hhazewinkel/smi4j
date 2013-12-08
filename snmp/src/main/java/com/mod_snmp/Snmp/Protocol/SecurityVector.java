package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import java.util.Vector;

/**
 * The SecurityVector class decodes and encodes a DatagramPacket
 * into an SnmpMessage.
 * It extends the BER class that contains all BER encoding
 * definitions. This class conatins the functions that process
 * the packages.
 */
public class SecurityVector extends Vector implements SecModelNumberInterface {
    /**
     * The Vector based Security System contstructor.
     */
    public SecurityVector() {
        /* In case we need some decoder initialization like a scratch pad. */
    }
    public void addSecModel(SecModelInterface sm) {
        addElement(sm);
    }

    public SecModelInterface getSecModel(int model) {
        for (int i = 0; i < size(); i++) {
            SecModelInterface secModel = (SecModelInterface)elementAt(i);
            if (secModel.isSecModel(model)) {
                return secModel;
            }
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

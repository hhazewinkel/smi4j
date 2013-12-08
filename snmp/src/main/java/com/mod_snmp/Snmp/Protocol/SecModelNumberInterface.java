package com.mod_snmp.Snmp.Protocol;

/**
 * Known security model numbers.
 * Defined in RFC 2571 in the TEXTUAL-CONVENTION named SnmpSecurityModel.
 */
public interface SecModelNumberInterface {
    static final int SECMODEL_ANY = 0;
    static final int SECMODEL_V1 = 1;
    static final int SECMODEL_V2C = 2;
    static final int SECMODEL_USM = 3;
}

package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * SNMP message interface defining some message specific
 * definitions.
 */
public interface SnmpMessageInterface {
    /**
     * SNMP versions, each version defines a specific
     * format and requires a specific message processing model.
     * Predefined message processing model numbers.
     * 4 - 255:
     *     reserved for future standards track message processing models.
     * values greater then 255 are enterprise specific:
     *     enterpriseNumber * 256 + messageProcessingModel
     */
    static final byte SNMP_VERSION_1  = 0;
    static final byte SNMP_VERSION_2C = 1;
    static final byte SNMP_V2USEC_V2STAR = 2;
    static final byte SNMP_VERSION_3  = 3;
}

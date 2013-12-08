package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * The SNMP PDU interface defining the pdu types and error status'
 * of an SNMP message.
 */
public interface SnmpPduInterface extends BerInterface {
   /**
     * PDU types
     */ 
    public static final byte SNMP_MSG_GET =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x0);
    public static final byte SNMP_MSG_GETNEXT =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x1);
    public static final byte SNMP_MSG_RESPONSE =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x2);
    public static final byte SNMP_MSG_SET =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x3);
    public static final byte SNMP_MSG_TRAP =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x4);
    public static final byte SNMP_MSG_GETBULK =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x5);
    public static final byte SNMP_MSG_INFORM =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x6);
    public static final byte SNMP_MSG_TRAP2 =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x7);
    public static final byte SNMP_MSG_REPORT =
                                (byte)(ASN_CONTEXT | ASN_CONSTRUCTOR | 0x8);

     /* Error codes for:
      * SNMP_VERSION_1, SNMP_VERSION_2C, SNMP_VERSION_3
      */
     public static final byte SNMP_ERR_NOERROR             = (byte)(0);
     public static final byte SNMP_ERR_TOOBIG              = (byte)(1);
     public static final byte SNMP_ERR_NOSUCHNAME          = (byte)(2);
     public static final byte SNMP_ERR_BADVALUE            = (byte)(3);
     public static final byte SNMP_ERR_READONLY            = (byte)(4);
     public static final byte SNMP_ERR_GENERR              = (byte)(5);
     /* Error codes for:
      * SNMP_VERSION_2C, SNMP_VERSION_3 
      */
     public static final byte SNMP_ERR_NOACCESS            = (byte)(6);
     public static final byte SNMP_ERR_WRONGTYPE           = (byte)(7);
     public static final byte SNMP_ERR_WRONGLENGTH         = (byte)(8);
     public static final byte SNMP_ERR_WRONGENCODING       = (byte)(9);
     public static final byte SNMP_ERR_WRONGVALUE          = (byte)(10);
     public static final byte SNMP_ERR_NOCREATION          = (byte)(11);
     public static final byte SNMP_ERR_INCONSISTENTVALUE   = (byte)(12);
     public static final byte SNMP_ERR_RESOURCEUNAVAILABLE = (byte)(13);
     public static final byte SNMP_ERR_COMMITFAILED        = (byte)(14);
     public static final byte SNMP_ERR_UNDOFAILED          = (byte)(15);
     public static final byte SNMP_ERR_AUTHORIZATIONERROR  = (byte)(16);
     public static final byte SNMP_ERR_NOTWRITABLE         = (byte)(17);
     public static final byte SNMP_ERR_INCONSISTENTNAME    = (byte)(18);
     /* Maximum value of the error status.
      */
     public static final byte SNMP_ERR_MAXIMUM             = SNMP_ERR_INCONSISTENTNAME;

}

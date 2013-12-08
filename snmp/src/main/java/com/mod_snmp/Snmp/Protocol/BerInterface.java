package com.mod_snmp.Snmp.Protocol;

/**
 * The BER interface defining the tags required for the encoding and
 * decoding.
 */
public interface BerInterface {
    /**
     * The various contexts used. SNMP uses only:
     * UNIVERSAL and APPLICATION.
     */
    public static final byte ASN_UNIVERSAL    =(byte)(0x00);
    public static final byte ASN_APPLICATION  =(byte)(0x40);
    public static final byte ASN_CONTEXT      =(byte)(0x80);
    public static final byte ASN_PRIVATE      =(byte)(0xC0);
    /**
     * The type construct sort.
     */
    public static final byte ASN_PRIMITIVE    =(byte)(0x00);
    public static final byte ASN_CONSTRUCTOR  =(byte)(0x20);
    /**
     * Helper encodings (masks).
     */
    public static final byte ASN_LONG_LENGTH  =(byte)(0x80);
    public static final byte ASN_LENGTH       =(byte)(0x7F); 
    public static final byte ASN_EXTENSION_ID =(byte)(0x1F);
    public static final byte ASN_BIT8         =(byte)(0x80);
    public static final byte ASN_TYPE         =(byte)(0x1F);
    /**
     * The ASN_UNIVERSAL types.
     */
    public static final byte ASN_INTEGER      =(byte)(0x02);
    public static final byte ASN_BIT_STR      =(byte)(0x03);
    public static final byte ASN_OCTET_STR    =(byte)(0x04);
    public static final byte ASN_NULL         =(byte)(0x05);
    public static final byte ASN_OBJECT_ID    =(byte)(0x06);
    public static final byte ASN_SEQUENCE     =(byte)(0x10);
    /**
     * The SNMP universal types.
     */
    public static final byte SNMP_SEQUENCE    =(byte)(ASN_CONSTRUCTOR | ASN_SEQUENCE);
    /**
     * The SNMP application types.
     */
    public static final byte SNMP_INTEGER     =(byte)ASN_INTEGER;
    public static final byte SNMP_STRING      =(byte)ASN_OCTET_STR;
    public static final byte SNMP_OBJID       =(byte)ASN_OBJECT_ID;
    public static final byte SNMP_NULL        =(byte)ASN_NULL;
    public static final byte SNMP_BIT_STR     =(byte)ASN_BIT_STR;
    public static final byte SNMP_IPADDRESS   =(byte)(ASN_APPLICATION | 0);
    public static final byte SNMP_COUNTER     =(byte)(ASN_APPLICATION | 1);
    public static final byte SNMP_COUNTER32   =(byte)(ASN_APPLICATION | 1);
    public static final byte SNMP_GAUGE       =(byte)(ASN_APPLICATION | 2);
    public static final byte SNMP_GAUGE32     =(byte)(ASN_APPLICATION | 2);
    public static final byte SNMP_UNSIGNED    =(byte)(ASN_APPLICATION | 2);
    public static final byte SNMP_UNSIGNED32  =(byte)(ASN_APPLICATION | 2);
    public static final byte SNMP_TIMETICKS   =(byte)(ASN_APPLICATION | 3);
    public static final byte SNMP_OPAQUE      =(byte)(ASN_APPLICATION | 4);
    public static final byte SNMP_NSAP        =(byte)(ASN_APPLICATION | 5);
    public static final byte SNMP_COUNTER64   =(byte)(ASN_APPLICATION | 6);
    public static final byte SNMP_UINTEGER32  =(byte)(ASN_APPLICATION | 7);
    /**
     * SNMP MIB access exceptions.
     */
    public static final byte SNMP_NOSUCHOBJECT   =(byte)(ASN_CONTEXT | ASN_PRIMITIVE | 0x0);
    public static final byte SNMP_NOSUCHINSTANCE =(byte)(ASN_CONTEXT | ASN_PRIMITIVE | 0x1);
    public static final byte SNMP_ENDOFMIBVIEW   =(byte)(ASN_CONTEXT | ASN_PRIMITIVE | 0x2);


}

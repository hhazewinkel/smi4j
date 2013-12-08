package com.mod_snmp.Snmp.Protocol;

import com.mod_snmp.Snmp.Values.SnmpBitStr;
import com.mod_snmp.Snmp.Values.SnmpCounter32;
import com.mod_snmp.Snmp.Values.SnmpCounter64;
import com.mod_snmp.Snmp.Values.SnmpInteger;
import com.mod_snmp.Snmp.Values.SnmpIpAddress;
import com.mod_snmp.Snmp.Values.SnmpNsap;
import com.mod_snmp.Snmp.Values.SnmpNull;
import com.mod_snmp.Snmp.Values.SnmpObjectId;
import com.mod_snmp.Snmp.Values.SnmpOctetStr;
import com.mod_snmp.Snmp.Values.SnmpOpaque;
import com.mod_snmp.Snmp.Values.SnmpTimeTicks;
import com.mod_snmp.Snmp.Values.SnmpUnsigned32;

import java.io.ByteArrayOutputStream;

/**
 * BER encoding class that defines the transform methods of a management
 * application SnmpMessage object into an on-the-wire message message.
 * The class defines only method for the generic elements, the varbind and      
 * varbindlist to encode the on-the-wire message.
 * The order an semantics of the on-the-wire message is defined in a
 * message processor for a specific protocol version.
 * @see MsgProcessingSystem
 */
public class BerEncoderForward extends ByteArrayOutputStream
                               implements BerInterface {
    private final static boolean debug = false;

    protected BerEncoderForward(int max /* max. msg. size */) {
        super(max);
    }
    protected int encodeSnmpInteger(SnmpInteger val) {
        return encodeAsnInteger(ASN_INTEGER, val.getValue());
    }
    protected int encodeSnmpInteger(int val) {
        return encodeAsnInteger(ASN_INTEGER, val);
    }
    protected int encodeAsnInteger(byte tag, int val) {
        int length = 4;
        int tmp = val;
        int mask = (0x1FF << 23);
        while ((((tmp & mask) == 0) || ((tmp & mask) == mask))
                                    && (length > 1)) {
            length--;
            tmp <<= 8;
        }
        encodeHeader(tag, length);
        switch (length) {
        case 4:
            write((val & 0xff000000) >> 24);
        case 3:
            write((val & 0xff0000) >> 16);
        case 2:
            write((val & 0xff00) >> 8);
        case 1:
            write(val & 0xff);
        }
        return count;
    }
    protected int encodeSnmpCounter32(SnmpCounter32 val) {
        return encodeLong(SNMP_COUNTER32, val.getValue() & 0xffff);
    }
    protected int encodeSnmpCounter64(SnmpCounter64 val) {
        return encodeLong(SNMP_COUNTER64, val.getValue() & 0xffff);
    }
    protected int encodeSnmpUnsigned32(SnmpUnsigned32 val) {
        return encodeLong(SNMP_GAUGE, val.getValue() & 0xffff);
    }
    protected int encodeSnmpTimeTicks(SnmpTimeTicks val) {
        return encodeLong(SNMP_TIMETICKS, val.getValue() & 0xffff);
    }
    protected int encodeLong(byte tag, long val) {
        int length = 4;
        long tmp = val;
        int mask = (0x1FF << 23);
        while ((((tmp & mask) == 0) || ((tmp & mask) == mask))
                                    && (length > 1)) {
            length--;
            tmp <<= 8;
        }
        encodeHeader(tag, length);
        switch (length) {
        case 4:
            write((int)((val & 0xff000000) >> 24));
        case 3:
            write((int)((val & 0xff0000) >> 16));
        case 2:
            write((int)((val & 0xff00) >> 8));
        case 1:
            write((int)(val & 0xff));
        }
        return count;
    }

    protected int encodeSnmpNull(SnmpNull val) {
        return encodeNull();
    }
    protected int encodeNull() {
        return encodeHeader(ASN_NULL, 0);
    }
    protected int encodeSnmpOctetStr(String str) {
        return encodeOctetStr(ASN_OCTET_STR, str.getBytes());
    }
    protected int encodeSnmpOctetStr(byte[] bytes) {
        return encodeOctetStr(ASN_OCTET_STR, bytes);
    }
    protected int encodeSnmpOctetStr(SnmpOctetStr str) {
        return encodeOctetStr(ASN_OCTET_STR, str.getBytes());
    }
    protected int encodeSnmpBitStr(SnmpBitStr str) {
        return encodeOctetStr(ASN_OCTET_STR, str.getBytes());
    }
    protected int encodeSnmpOpaque(SnmpOpaque str) {
        return encodeOctetStr(SNMP_OPAQUE, str.getBytes());
    }
    protected int encodeOctetStr(byte tag, byte[] bytes) {
        int length = bytes.length;
        encodeHeader(tag, length);
        int i = 0;
        while (length-- > 0) {
            write(bytes[ i++ ]);
        }
        return count;
    }
    protected int encodeSnmpIpAddress(SnmpIpAddress val) {
        return encodeOctetStr(SNMP_IPADDRESS, val.getValue());
    }
    protected int encodeSnmpNsap(SnmpNsap val) {
        return encodeOctetStr(SNMP_NSAP, val.getValue());
    }
    protected int encodeSnmpObjectId(SnmpObjectId oid) {
        int asn_length = 0;
        int length = oid.getLength();
        long sub_oid;
        int i;
        if (0 < length) {
            asn_length = 1;
        }
        for (i = 2; i < length; i++) {
            sub_oid = oid.getOidAt(i);
            if (sub_oid < (long)0x80) {
                asn_length += 1;
            } else if (sub_oid < (long)0x4000) {
                asn_length += 2;
            } else if (sub_oid < (long)0x200000) {
                asn_length += 3;
            } else if (sub_oid < (long)0x10000000) {
                asn_length += 4;
            } else {
                asn_length += 5;
            }
        }
        encodeHeader(SNMP_OBJID, asn_length);
        if (length == 0) {
            write(0);
        } else if (length == 1) {
            write((int)(oid.getOidAt(0) * 40));
        } else if (length >= 2) {
            write((int)(oid.getOidAt(0) * 40 + oid.getOidAt(1)));
            for (i = 2; i < length; i++ ){
                sub_oid = oid.getOidAt(i);
                if (sub_oid < (long)0x80) {
                    write((int)sub_oid);
                } else if (sub_oid < (long)0x4000) {
                    write((int)((sub_oid>>7) | 0x80));
                    write((int)(sub_oid & 0x07f));
                } else if (sub_oid < (long)0x200000) {
                    write((int)((sub_oid>>14) | 0x80));
                    write((int)((sub_oid>>7 & 0x7f) | 0x80));
                    write((int)(sub_oid & 0x07f));
                } else if (sub_oid < (long)0x10000000) {
                    write((int)((sub_oid>>21) | 0x80));
                    write((int)((sub_oid>>14 & 0x7f) | 0x80));
                    write((int)((sub_oid>>7 & 0x7f) | 0x80));
                    write((int)(sub_oid & 0x07f));
                } else {
                    write((int)((sub_oid>>28) | 0x80));
                    write((int)((sub_oid>>21 & 0x7f) | 0x80));
                    write((int)((sub_oid>>14 & 0x7f) | 0x80));
                    write((int)((sub_oid>>7 & 0x7f) | 0x80));
                    write((int)(sub_oid & 0x07f));
                }
            }
        }
        return count;
    }

    protected int encodeHeader(byte tag, int length) {
        int start = count;
        write(tag);
        if (length < 0x80) {
            write(length);
        } else if (length <= 0xFF) {
            write(1 | ASN_LONG_LENGTH);
            write(length);
        } else { /* 0xFF < length < 0xFFFF */
            write(2 | ASN_LONG_LENGTH);
            write((length & 0xFF00) >> 8);
            write(length & 0xFF);
        }
        return start;
    }
}

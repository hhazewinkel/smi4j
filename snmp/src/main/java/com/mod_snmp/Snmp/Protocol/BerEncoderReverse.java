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
import com.mod_snmp.Snmp.Values.Varbind;
import com.mod_snmp.Snmp.Values.VarbindList;

import java.io.ByteArrayOutputStream;

/**
 * BER encoding class that defines the transform methods of a management
 * application format as defined by the SnmpMessage class into an
 * on-the-wire message.
 * The class defines only method for the generic elements, the varbind and      
 * varbindlist to encode the on-the-wire message.
 * The order an semantics of the on-the-wire message is defined in a
 * message processor for a specific protocol version.
 * The class maintains an internal array in which the on-the-wire
 * message is written in reverse order.
 */
public class BerEncoderReverse extends ByteArrayOutputStream
                               implements BerInterface {
    private final static boolean debug = false;

    protected BerEncoderReverse(int max /* max. msg. size */) {
        super(max);
    }
    protected int encodeVarbindList(VarbindList vbl)
                                                  throws BerException {
        if (debug) dump("Encode Varbind List");
        int start = count;
        int end = start;
        int i = vbl.size();
        while (i > 0) {
            try {
                end = encodeVarbind(vbl.varbindAt(--i));
            } catch (BerException e) {
                throw new BerException(e.getMessage() + " in varbind(" + i + ")");
            }
        }
        int length = encodeHeader(SNMP_SEQUENCE, (end - start));
        if (debug) dump("Encode Varbind List done");
        return length;
    }
    protected int encodeVarbind(Varbind vb) throws BerException {
        int start = count;
        if (debug) dump("Varbind: val: " + vb.value);
        if (vb.value == null) {
            encodeNull();
        } else if (vb.value instanceof SnmpNull) {
            encodeSnmpNull((SnmpNull)vb.value);
        } else if (vb.value instanceof SnmpInteger) {
           encodeSnmpInteger((SnmpInteger)vb.value);
        } else if (vb.value instanceof SnmpOctetStr) {
            encodeSnmpOctetStr((SnmpOctetStr)vb.value);
        } else if (vb.value instanceof SnmpObjectId) {
            encodeSnmpObjectId((SnmpObjectId)vb.value);
        } else if (vb.value instanceof SnmpBitStr) {
            encodeSnmpBitStr((SnmpBitStr)vb.value);
        } else if (vb.value instanceof SnmpIpAddress) {
            encodeSnmpIpAddress((SnmpIpAddress)vb.value);
        } else if (vb.value instanceof SnmpCounter32) {
            encodeSnmpCounter32((SnmpCounter32)vb.value);
        } else if (vb.value instanceof SnmpUnsigned32) {
            encodeSnmpUnsigned32((SnmpUnsigned32)vb.value);
        } else if (vb.value instanceof SnmpTimeTicks) {
            encodeSnmpTimeTicks((SnmpTimeTicks)vb.value);
        } else if (vb.value instanceof SnmpOpaque) {
            encodeSnmpOpaque((SnmpOpaque)vb.value);
        } else if (vb.value instanceof SnmpNsap) {
            encodeSnmpNsap((SnmpNsap)vb.value);
        } else if (vb.value instanceof SnmpCounter64) {
            encodeSnmpCounter64((SnmpCounter64)vb.value);
        } else {
            throw new BerException("Not supported variable type:" + vb.value);
        } 
        if (debug) dump("Varbind: name: " + vb.name);
        encodeSnmpObjectId(vb.name);
        return encodeHeader(SNMP_SEQUENCE, (count - start));
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
        switch (length) {
        case 4:
            write((byte)(val & 0xff));
            write((byte)((val & 0xff00) >> 8));
            write((byte)((val & 0xff0000) >> 16));
            write((byte)((val & 0xff000000) >> 24));
            break;
        case 3:
            write((byte)(val & 0xff));
            write((byte)((val & 0xff00) >> 8));
            write((byte)((val & 0xff0000) >> 16));
            break;
        case 2:
            write((byte)(val & 0xff));
            write((byte)((val & 0xff00) >> 8));
            break;
        case 1:
            write((byte)(val & 0xff));
        }
        return encodeHeader(tag, length);
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
        switch (length) {
        case 4:
            write((byte)(val & 0xff));
            write((byte)(val & 0xff00) >> 8);
            write((byte)(val & 0xff0000) >> 16);
            write((byte)(val & 0xff000000) >> 24);
            break;
        case 3:
            write((byte)(val & 0xff));
            write((byte)(val & 0xff00) >> 8);
            write((byte)(val & 0xff0000) >> 16);
            break;
        case 2:
            write((byte)(val & 0xff));
            write((byte)(val & 0xff00) >> 8);
            break;
        case 1:
            write((byte)(val & 0xff));
        }
        return encodeHeader(tag, length);
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
    protected int encodeSnmpOctetStr(byte[] bytes, int length) {
        return encodeOctetStr(ASN_OCTET_STR, bytes, length);
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
    protected int encodeSnmpOctetStr(byte oneByte) {
        write(oneByte);
        return encodeHeader(ASN_OCTET_STR, 1);
    }
    protected int encodeOctetStr(byte tag, byte[] bytes, int length) {
        writeReverse(bytes, 0, length);
        return encodeHeader(tag, length);
    }
    protected int encodeOctetStr(byte tag, byte[] bytes) {
        int length = bytes.length;
        writeReverse(bytes, 0, length);
        return encodeHeader(tag, length);
    }
    protected int encodeSnmpIpAddress(SnmpIpAddress val) {
        return encodeOctetStr(SNMP_IPADDRESS, val.getValue());
    }
    protected int encodeSnmpNsap(SnmpNsap val) {
        return encodeOctetStr(SNMP_NSAP, val.getValue());
    }
    protected int encodeSnmpObjectId(SnmpObjectId oid) {
        int start = count;
        int i = oid.getLength();
        i--;
        while (i > 1) {
            long subOid = oid.getOidAt(i--);
            if (subOid < (long)0x80) {
                write((byte)subOid);
            } else if (subOid < (long)0x4000) {
                write((byte)(subOid & 0x07f));
                write((byte)((subOid>>7 & 0x7f) | 0x80));
            } else if (subOid < (long)0x200000) {
                write((byte)(subOid & 0x07f));
                write((byte)((subOid>>7 & 0x7f) | 0x80));
                write((byte)((subOid>>14 & 0x7f) | 0x80));
            } else if (subOid < (long)0x10000000) {
                write((byte)(subOid & 0x07f));
                write((byte)((subOid>>7 & 0x7f) | 0x80));
                write((byte)((subOid>>14 & 0x7f) | 0x80));
                write((byte)((subOid>>21 & 0x7f) | 0x80));
            } else {
                write((byte)(subOid & 0x07f));
                write((byte)((subOid>>7 & 0x7f) | 0x80));
                write((byte)((subOid>>14 & 0x7f) | 0x80));
                write((byte)((subOid>>21 & 0x7f) | 0x80));
                write((byte)((subOid>>28) | 0x80));
            }
        }
        if (i == 1) {
            write((byte)(oid.getOidAt(0) * 40 + oid.getOidAt(1)));
        } else if (i == 0) {
            write((byte)(oid.getOidAt(0) * 40));
        } else { /* i == -1 */
            write(0);
        }
        return encodeHeader(SNMP_OBJID, (count - start));
    }

    protected int encodeHeader(byte tag, int length) {
        if (length < 0x0080) {
            write((byte)length);
        } else if (length <= 0x00FF) {
            write((byte)length);
            write((byte)(1 | ASN_LONG_LENGTH));
        } else { /* 0xFF < length < 0xFFFF */
            write((byte)(length & 0xFF));
            write((byte)((length & 0xFF) >> 8));
            write((byte)(2 | ASN_LONG_LENGTH));
        }
        write(tag);
        return count;
    }
    private void writeReverse(byte[] b, int off, int len) {
        byte[] bytes = new byte[len - off];
        for (int i = len, j = 0; i > off; j++) {
            bytes[j] = b[--i];
        }
        write(bytes, 0, (len - off));
    }

    public byte[] toByteArray() {
        byte[] resultBuf = new byte[count];
        int i = count;
        int j = 0;
        while (i > 0) {
            resultBuf[j++] = buf[--i];
        }
        return resultBuf;
    }
    protected byte[] toPaddedByteArray() {
        int padding = (count & 0x07);
        byte[] resultBuf = new byte[count];
        int i = count;
        int j = 0;
        while (i > 0) {
            resultBuf[j++] = buf[--i];
        }
        return resultBuf;
    }

    protected void write(byte b) {
        buf[count++]=b;
    }
    protected int position() {
        return count;
    }
    /**
     * Debug dumper
     */
    private void dump(String s) {
        System.out.println("Enc: " + s);
    }
}

package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.Snmp.Utils.TypeConversions;
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
import com.mod_snmp.Snmp.Values.SnmpValue;
import com.mod_snmp.Snmp.Values.SnmpValueException;
import com.mod_snmp.Snmp.Values.Varbind;
import com.mod_snmp.Snmp.Values.VarbindList;

import java.io.ByteArrayInputStream;
import java.util.Vector;

/**
 * BER decoding class that defines the transform methods from an on-the-wire
 * message into a management application SnmpMessage object.
 * The class defines only method for the generic elements, the varbind and      
 * varbindlist to decode the on-the-wire message.
 * The order an semantics of the on-the-wire message is defined in a
 * message processor for a specific protocol version.
 */
public class BerDecoder extends ByteArrayInputStream
                        implements BerInterface {
    private final static boolean debug = false;
    
    /**
     * Default contructor that initializes the message representation.
     * @param buffer The byte array put as the star for this decoder.
     */
    protected BerDecoder(byte[] buffer) {
        super(buffer);
    }
    /**
     * Default contructor that initializes the message representation.
     * @param buffer The byte array put as the start for this decoder.
     * @param start The first byte of the byte array used for initialization.
     * @param end The last byte of the byte array used for initialization.
     */
    protected BerDecoder(byte[] buffer, int start, int end) {
        super(buffer, start, end);
    }
    /**
     * Decode a variable binding list.
     * @return The decoded variable bindings list.
     */
    protected VarbindList decodeVarbindList()
                                                throws BerException {
        int length = decodeSequenceHeader();
        VarbindList vbl = new VarbindList();
        while (available() > 0) {
            vbl.addVarbind(decodeVarbind());
        }
        return vbl;
    }
    /**
     * Decode a variable binding.
     * @return The decoded variable bindings.
     */
    protected Varbind decodeVarbind()
                                                throws BerException {
        decodeTag(SNMP_SEQUENCE);
        int length = decodeLength();
        decodeLengthAvailable(length);
        Varbind vb = new Varbind();
        try {
            vb.name = new SnmpObjectId(decodeSnmpObjectId());
        } catch (SnmpValueException e) {
            System.out.println(e.getMessage());
        }
        if (debug) dump("Varbind Oid: " + vb.name);
        /* Just peek into the value tag to figure out the type. */
        mark(0);
        int type = decodeTag(-1);
        reset();
        if (debug) dump("Varbind type: " + Integer.toHexString(type));
        try {
            switch (type) {
            case SNMP_INTEGER:
                vb.value = new SnmpInteger(decodeSnmpInteger());
                break;
            case SNMP_STRING:
                vb.value = new SnmpOctetStr(decodeSnmpOctetStr());
                break;
            case SNMP_OBJID:
                vb.value = new SnmpObjectId(decodeSnmpObjectId());
                break;
            case SNMP_NULL:
                vb.value = new SnmpNull(decodeSnmpNull());
                break;
            case SNMP_BIT_STR:
                vb.value = new SnmpBitStr(decodeSnmpBitString());
                break;
            case SNMP_IPADDRESS:
                vb.value = new SnmpIpAddress(decodeSnmpIpAddress());
                break;
            case SNMP_COUNTER32: /* Also SNMP_COUNTER */
                vb.value = new SnmpCounter32(decodeSnmpCounter32());
                break;
            case SNMP_GAUGE: /* Also SNMP_GAUGE32 and SNMP_UNSIGNED */
                vb.value = new SnmpUnsigned32(decodeSnmpUnsigned32());
                break;
            case SNMP_TIMETICKS:
                vb.value = new SnmpTimeTicks(decodeSnmpTimeTicks());
                break;
            case SNMP_OPAQUE:
                vb.value = new SnmpOpaque(decodeSnmpOpaque());
                break;
            case SNMP_NSAP:
                vb.value = new SnmpNsap(decodeSnmpNsap());
                break;
            case SNMP_COUNTER64:
                vb.value = new SnmpCounter64(decodeSnmpCounter64());
                break;
            case SNMP_UINTEGER32: /* Is historic */
                vb.value = new SnmpUnsigned32(decodeSnmpUInteger32());
                break;
            default:
                vb.value = decodeValueContext();
            } 
        } catch (SnmpValueException e) { 
            System.out.println("Wrong value for type:" + e.getMessage()); 
        }
        return vb;
    }
    /**
     * Decode an SNMP INTEGER.
     * @return The integer value decoded.
     */
    protected int decodeSnmpInteger()
                                                throws BerException {
        decodeTag(SNMP_INTEGER);
        int length = decodeLength();
        decodeLengthAvailable(length);
        int value = 0;
        int octet = 0xff & read();
        if ((octet & ASN_BIT8) != 0) {
            value = -1;
        }
        value = (value << 8) | octet;
        while (--length != 0) {
            octet = 0xff & read();
            value = (value << 8) | octet;  
        }
        if (debug) dump("Integer: 0x" + Long.toHexString(value) + " " + value);
        return value;
    }
    /**
     * Decode an SNMP OCTET STRING.
     * @return The decoded octet string as a byte array.
     */
    protected byte[] decodeSnmpOctetStr()
                                                throws BerException {
        decodeTag(ASN_OCTET_STR);
        int length = decodeLength();
        decodeLengthAvailable(length);
        byte[]buffer = new byte[length];
        for (int i = 0 ; i < length ; i++) {
            buffer[i]= (byte)read();
        }
        if (debug) dump("OctetStr: " + TypeConversions.bytes2HexString(buffer));
        return buffer;
    }
    /**
     * Decode an SNMP OBJECT IDENTIFIER.
     * @return The decoded object identifier as a Vector.
     */
    protected Vector decodeSnmpObjectId()
                                                throws BerException {
        decodeTag(ASN_OBJECT_ID);
        int length = decodeLength();
        decodeLengthAvailable(length);
        Vector buffer = new Vector(length);
        long val = 0;
        if (length > 0) {
            byte item = (byte)read();
            length--;
            buffer.addElement(new Long(item/40)); 
            buffer.addElement(new Long(item%40));
            while (length > 0) {
                item = (byte)read();
                length--;
                val = (item & 0x7f);
                if ((item & ASN_LONG_LENGTH) != 0) {
                     do {
                         item = (byte)read();
                         length--;
                         val = (val << 7);
                         val += (item & ASN_LENGTH);
                     } while (((item & ASN_LONG_LENGTH) != 0) && (length > 0));
                     if ((item & ASN_LONG_LENGTH) != 0) {
                         throw new BerException("last byte of oid has 'next' bit set");
                     }
                 }
                 buffer.addElement(new Long(val));
            }
        }
        if (debug) dump("ObjectId: " + buffer);
        buffer.trimToSize();
        return buffer;
    }
    /**
     * Decode an SNMP OPAQUE value.
     * @return The decoded opaque value as a byte array.
     */
    protected byte[] decodeSnmpOpaque()
                                                throws BerException {
        decodeTag(SNMP_OPAQUE);
        int length = decodeLength();
        decodeLengthAvailable(length);
        byte[]buffer = new byte[length];
        for (int i = 0 ; i < length ; i++) {
            buffer[i]= (byte)read();
        }
        if (debug) dump("Opaque: " + TypeConversions.bytes2HexString(buffer));
        return buffer;
    }
    /**
     * Decode an SNMP IP ADDRESS value.
     * @return The decoded ip address value as a byte array.
     */
    protected byte[] decodeSnmpIpAddress()
                                                throws BerException {
        decodeTag(SNMP_IPADDRESS);
        int length = decodeLength();
        if (4 != length) {
            throw new BerException("IpAddress has incorrect length '" + length + "'");
        }
        decodeLengthAvailable(length);
        byte[]buffer = new byte[length];
        for (int i = 0 ; i < length ; i++) {
            buffer[i]= (byte)read();
        }
        if (debug) dump("IpAddress: " + TypeConversions.bytes2HexString(buffer));
        return buffer;
    }
    /**
     * Decode an SNMP NSAP ADDRESS value.
     * @return The decoded nsap address value as a byte array.
     */
    protected byte[] decodeSnmpNsap()
                                                throws BerException {
        decodeTag(SNMP_NSAP);
        int length = decodeLength();
        decodeLengthAvailable(length);
        byte[]buffer = new byte[length];
        for (int i = 0 ; i < length ; i++) {
            buffer[i]= (byte)read();
        }
        if (debug) dump("Nsap: " + TypeConversions.bytes2HexString(buffer));
        return buffer;
    }
    /**
     * Decode an SNMP NULL value which is no value or an SNMP exception.
     * @return The decoded SNMP exception.
     */
    protected int decodeSnmpNull()
                                                throws BerException {
        decodeTag(ASN_NULL);
        int length = decodeLength();
        int value;
        if (length == 0) {
            value = 0;
        } else if (length == 1) {
            decodeLengthAvailable(length);
            value = (int)read();
        } else {
            throw new BerException("AsnNull has incorrect length '" + length + "'");
        }
        if (debug) dump("Null: <>");
        return value;
    }
    /**
     * Decode an SNMP context.
     */
    protected SnmpValue decodeValueContext()
                                                throws BerException {
        try {
            byte tag = (byte)decodeTag(-1);
            int length = decodeLength();
            if (debug) dump("Context " + tag + " length " + length + " " + SNMP_ENDOFMIBVIEW);
            if ((tag == SNMP_NOSUCHOBJECT) ||
                (tag == SNMP_NOSUCHINSTANCE) ||
                (tag == SNMP_ENDOFMIBVIEW)) {
                if (debug) dump("Known context " + tag + " length " + length);
                while (length > 0) {
                    read();
                }
                return new SnmpNull(ASN_TYPE & tag);
            }
            if (debug) dump("Unknown context " + tag + " length " + length + "(just skipping)");
            byte[] buffer = new byte[length];
            for (int i = 0 ; i < length ; i++) {
                buffer[i]= (byte)read();
            }
            return new SnmpOctetStr(buffer);
        } catch (SnmpValueException e) {
        }
        throw new BerException("A context encoding error");
    }
    /**
     * Decode an SNMP COUNTER32 value.
     * @return The encoded counter32 value as a long.
     */
    protected long decodeSnmpCounter32()
                                                throws BerException {
        decodeTag(SNMP_COUNTER32);
        int length = decodeLength();
        decodeLengthAvailable(length);
        long value = 0;
        int octet = 0xff & read();
        if ((octet & ASN_BIT8) != 0) {
            value = -1;
        }
        value = (value << 8) | octet;    
        while (--length != 0) {  
            value = (value << 8) | octet;
            octet = 0xff & read();
        }
        if (debug) dump("Counter32: " + value);
        return value;
    }
    /**
     * Decode an SNMP COUNTER64 value.
     * @return The encoded counter64 value as a long.
     */
    protected long decodeSnmpCounter64()
                                                throws BerException {
        decodeTag(SNMP_COUNTER64);
        int length = decodeLength();
        decodeLengthAvailable(length);
        long value = 0;
        int octet = 0xff & read();
        if ((octet & ASN_BIT8) != 0) {
            value = -1;
        }
        value = (value << 8) | octet;    
        while (--length != 0) {  
            value = (value << 8) | octet;
            octet = 0xff & read();
        }
        if (debug) dump("Counter64: " + value);
        return value;
    }
    /**
     * Decode an SNMP BIT STRING.
     * @return The decoded bit string as a byte array.
     */
    protected byte[] decodeSnmpBitString()
                                                throws BerException {
        decodeTag(SNMP_BIT_STR);
        int length = decodeLength();
        decodeLengthAvailable(length);
        byte[] buffer = new byte[length];
        for (int i = 0 ; i < length ; i++) {
            buffer[i] = (byte)read();
        }
        if (debug) dump("BitStr: " + TypeConversions.bytes2HexString(buffer));
        return buffer;
    }
    protected long decodeSnmpTimeTicks() 
                                                throws BerException {
        decodeTag(SNMP_TIMETICKS);
        int length = decodeLength();
        if (debug) dump("TimeTicks: length " + length);
        decodeLengthAvailable(length);
        long value = 0;
        int octet = 0xff & read();
        if ((octet & ASN_BIT8) != 0) {
            value = -1;
        }
        value = (value << 8) | octet;
        while (--length != 0) {  
            octet = 0xff & read();
            value = (value << 8) | octet;
        }
        if (debug) dump("TimeTicks: 0x" + Long.toHexString(value) + " " + value);
        return value;
    }
    protected long decodeSnmpUnsigned32()
                                                throws BerException {
        decodeTag(SNMP_GAUGE);
        int length = decodeLength();
        decodeLengthAvailable(length);
        long value = 0;
        int octet = 0xff & read();
        if ((octet & ASN_BIT8) != 0) {
            value = -1;
        }
        value = (value << 8) | octet;    
        while (--length != 0) {  
            value = (value << 8) | octet;
            octet = 0xff & read();
        }
        if (debug) dump("Unsigned32: 0x" + Long.toHexString(value) + " " + value);
        return value;
    }
    protected long decodeSnmpUInteger32()
                                                throws BerException {
        decodeTag(SNMP_UINTEGER32);
        int length = decodeLength();
        decodeLengthAvailable(length);
        long value = 0;
        int octet = 0xff & read();
        if ((octet & ASN_BIT8) != 0) {
            value = -1;
        }   
        value = (value << 8) | octet;
        while (--length != 0) {
            value = (value << 8) | octet;
            octet = 0xff & read();
        }
        if (debug) dump("UInteger32: 0x" + Long.toHexString(value) + " " + value);
        return value;
    }

    protected int decodeSequenceHeader()
                                                throws BerException {
        int length;
        decodeTag(SNMP_SEQUENCE);
        length = decodeLength();
        decodeLengthAvailable(length);
        if (debug) dump("Sequence: " + Integer.toHexString(length) + " " + length);
        return length;
    }

    protected int decodeTag(int expected)
                                                throws BerException {
        int tag = read();
        if ((expected != -1) && (tag != expected)) {
            throw new BerException("tag '" + Integer.toHexString(tag) +
                          "' and expected '" + Integer.toHexString(expected) + "'");
        }
        return tag;
    }
    protected int decodeLength()
                                                throws BerException {
        int length = read();
        if ((length & ASN_LONG_LENGTH) > 0) {
            length = (length & ASN_LENGTH);
            if (length == 1) {
                length = read();
            } else if (length == 2) {
                length = read();
                length = length << 8;
                length += read();
            } else {
                throw new BerException("sequence length '" + length + "'");
            }
        }
        return length;
    }
    protected void decodeLengthEquals(int length)
                                                throws BerException {
        if (length != available()) {
            throw new BerException("difference of length '" + length +
                      "' and available data '" + available() + "'");
        }
    }
    protected void decodeLengthAvailable(int length)
                                                throws BerException {
        if (length > available()) {
            throw new BerException("length '" + length +
                      "' is not available data '" + available() + "'");
        }
    }
    /**
     * Getting access to the internal byte array which is the
     * message received. It is not a good idea to manipulate the
     * array, but an excemption would be the message hash for
     * SNMP authentication.
     * This class should not know that, but this is a developers note.
     */
    protected byte[] getByteArray() {
        return buf;
    }
    /**
     * Method dumping out the data (including administrative) of the
     * instantiated class.
     */
    protected void dump() {
        System.out.println("Buffer adm: " + count + " " + mark + " " + pos);
        System.out.println("Buffer: " + TypeConversions.bytes2HexString(buf));
    }
    /**
     * Debug dumper
     */
    private void dump(String s) {
        System.out.println("Dec: " + s);
    }
}

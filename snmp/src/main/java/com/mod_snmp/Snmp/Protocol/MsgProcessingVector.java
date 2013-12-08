package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
/**
 * MsgProcessingVector.java
 * The MsgProcessingVector class decodes and encodes a DatagramPacket
 * into an SnmpMessage.
 * It extends the BER class that contains all BER encoding
 * definitions. This class conatins the functions that process
 * the packages.
 */


import com.mod_snmp.Snmp.Utils.TypeConversions;

import java.net.DatagramPacket;
import java.util.Vector;

public class MsgProcessingVector extends Vector {
    private final static boolean debug = false;

    /**
     * The vector based Message Processing System constructor.
     */
    public MsgProcessingVector() {
    }
    /**
     * add an message processing model.
     * @param mpm message processing model.
     */
    public void addMsgProcModel(MsgProcInterface mpm) {
        addElement(mpm);
    }
    /**
     * decoding a message that is contained by 'DatagramPacket packet'.
     * This function is a wrapper in order to get to the actual processing.
     * @param packet the message to be decoded.
     * @throws SnmpMessageException When the message contains an incorrect BER
     * encoding, authentication failed or decryption failed.
     */
    public SnmpMessage decode(DatagramPacket packet)
                                                throws SnmpMessageException {
        return decode(packet.getData(), packet.getLength());
    }
    /**
     * decoding a message that is contained by 'byte[] data, int datalength'.
     * This function is a wrapper in order to get to the actual processing.
     * @param data the message as byte array to be decoded.
     * @param datalength the length of the message contained in data.
     */
    public SnmpMessage decode(byte[] data, int datalength)
                                                throws SnmpMessageException {
        try {
            if (debug) dump("Decode message start");
            BerDecoder raw_packet = new BerDecoder(data, 0, datalength);
	    if (debug) dump(data, datalength);
            int length = raw_packet.decodeSequenceHeader();
            if (debug) dump("Message data length " + length);
            int version = raw_packet.decodeSnmpInteger();
            for (int i = 0; i < size(); i++) {
                MsgProcInterface mpm = (MsgProcInterface)elementAt(i);
                if (mpm.isMsgProcModel(version)) {
                    return mpm.decode(version, raw_packet);
                }
            }
            if (debug) dump("Decode message done");
        } catch (BerException e) {
            throw new SnmpMessageException(e.getMessage());
        }
        throw new SnmpMessageException("SNMP Version is not supported.");
    }
    /**
     * encoding a message into the on-the-wire format.
     * @param msg the message to be encoded.
     * @return The encoded message in DatagramPacket format, ready to send.
     * @see com.mod_snmp.Snmp.Protocol.SnmpMessage
     */
    public DatagramPacket encode(SnmpMessage msg)
                                                throws SnmpMessageException {
        for (int i = 0; i < size(); i++) {
            MsgProcInterface mpm = (MsgProcInterface)elementAt(i);
            if (mpm.isMsgProcModel(msg.version)) {
                byte[] raw_packet = mpm.encode(msg);
                if (debug) dump(raw_packet, raw_packet.length);
                return new DatagramPacket(raw_packet, raw_packet.length);
            }
        }
        throw new SnmpMessageException("SNMP Version is not supported.");
    }
    /**
     * The discovery phase needed for the session that gets
     * initialized.
     * @param v The protocol version for the discovery
     * @param s The security model for the discovery
     * @return A session security data class
     */
    public SecSessDataInterface discovery(int v, int s)
                                                throws SnmpEngineException {
        for (int i = 0; i < size(); i++) {
            MsgProcInterface mpm = (MsgProcInterface)elementAt(i);
            if (mpm.isMsgProcModel(v)) {
                return mpm.discovery(s);
            }
        }
        throw new SnmpEngineException("Not such message processor");
    }
    /**
     * Debug dumper.
     */
    private void dump(String s) {
        System.out.println("MPS: " + s);
    }
    /**
     * Debug dumper.
     */
    private void dump(byte[] array, int length) {
        dump(TypeConversions.bytes2HexString(array, length));
    }
}

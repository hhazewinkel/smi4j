package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * SNMP version 1 message processor.
 * The MsgProcSnmpV1 class decodes/encodes an SNMP version 1 messages.
 */
public class MsgProcSnmpV1 implements SnmpMessageInterface,
                                      SecModelNumberInterface,
                                      BerInterface, MsgProcInterface {
    private final boolean debug = false;
    /**
     * The community based security model.
     */
    protected SecModelCommunity sm;

    public MsgProcSnmpV1() {
        sm = new SecModelCommunity(SECMODEL_V1);
    }
    public String toString() {
        return "SNMPv1(#0)";
    }
    public int getMsgProcModel() {
        return SNMP_VERSION_1;
    }
    public boolean isMsgProcModel(int m) {
        if (debug) dump("msg model (#" + m + ")");
        return (m == SNMP_VERSION_1);
    }
    public SecSessDataInterface discovery(int s) throws SnmpEngineException {
        if (debug) dump("sec model (#" + s + ")");      
        if (s == SECMODEL_V1) {
            return sm.createSessData();
        }
        throw new SnmpEngineException("no such security model");
    }
    public SnmpMessage decode(int version, BerDecoder raw_packet)
                                                throws SnmpMessageException {
        try {
            if (debug) dump("Decode processor");
            SnmpMessage msg = new SnmpMessage();
            msg.version = version;
            /*
             * Decode the message header SNMPV1 only the community.
             */
            sm.decodeSecurityParameters(raw_packet, msg);
            /*
             * Decode the PDU.
             */
            msg.setPdu(decodePDU(raw_packet));
            if (debug) dump("Decode processor done");
            return msg;
        } catch (BerException e) {
            throw new SnmpMessageException(e.getMessage());
        }
    }

    public byte[] encode(SnmpMessage msg) throws SnmpMessageException {
        try {
            if (debug) dump("Encode processor");
            BerEncoderReverse bre = new BerEncoderReverse(msg.size);
            /*
             * Encode the PDU.
             */
            encodePdu(bre, msg.getPdu());
            /*
             * Encode the message header. SNMPV1 only Community.
             */
            sm.encodeSecurityParameters(bre, msg);
            /*
             * Add the version.
             */
            int length = bre.encodeSnmpInteger(msg.getVersion());
            int total_length = bre.encodeHeader(SNMP_SEQUENCE, length);
            byte[] raw_packet = bre.toByteArray();
            if (debug) dump("Encode processor done");
            return raw_packet;
        } catch (BerException e) {
            throw new SnmpMessageException(e.getMessage());
        }
    }
    public SnmpPdu decodePDU(BerDecoder raw_packet)
                                        throws SnmpMessageException, BerException {
        SnmpPdu pdu = new SnmpPdu();
        int length;

        pdu.setPduType(raw_packet.decodeTag(-1));
        if (debug) dump(pdu.pduTypeToString());
        switch (pdu.getPduType()) {
        case SnmpPdu.SNMP_MSG_GET:
        case SnmpPdu.SNMP_MSG_GETNEXT:
        case SnmpPdu.SNMP_MSG_RESPONSE:
        case SnmpPdu.SNMP_MSG_SET:
        case SnmpPdu.SNMP_MSG_TRAP:
        case SnmpPdu.SNMP_MSG_INFORM:
        case SnmpPdu.SNMP_MSG_TRAP2:
        case SnmpPdu.SNMP_MSG_REPORT:
            length = raw_packet.decodeLength();
            raw_packet.decodeLengthEquals(length);
            pdu.setRequestId(raw_packet.decodeSnmpInteger());
            pdu.setErrorStatus(raw_packet.decodeSnmpInteger());
            pdu.setErrorValue(raw_packet.decodeSnmpInteger());
            break;
        default: 
            throw new SnmpMessageException("Unknown PDU type");
        }
        pdu.setVarbindList(raw_packet.decodeVarbindList());
        return pdu;
    }
    public int encodePdu(BerEncoderReverse bre, SnmpPdu pdu)
                                                  throws BerException {
        switch (pdu.getPduType()) {
        case SnmpPdu.SNMP_MSG_GET:
        case SnmpPdu.SNMP_MSG_GETNEXT:
        case SnmpPdu.SNMP_MSG_RESPONSE:
        case SnmpPdu.SNMP_MSG_SET:
        case SnmpPdu.SNMP_MSG_TRAP:
            bre.encodeVarbindList(pdu.getVarbindList());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getErrorIndex());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getErrorStatus());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getRequestId());
            break;
        default:
            throw new BerException("pdu type '" + pdu.getPduType() + "' not supported");
        }
        if (debug) dump("PDU header");
        return bre.encodeHeader(pdu.getPduType(), bre.position());
    }
    /**
     * Debug dumper.
     */
    protected void dump(String s) {
        System.out.println(toString() + ": " + s);
    }
}

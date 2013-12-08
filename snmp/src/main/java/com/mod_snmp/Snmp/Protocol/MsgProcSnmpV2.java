package com.mod_snmp.Snmp.Protocol;

/**
 * SNMP version 1 message processor.
 * The MsgProcSnmpV1 class decodes/encodes an SNMP version 1 messages.
 * The class extends MsgProcSnmpV1:
 *    different static message values,
 *    same message format (encode/decode functions).
 *    different PDU types (encodePDU/decodePDU functions),
 */
public class MsgProcSnmpV2 extends MsgProcSnmpV1
                           implements SnmpMessageInterface,
                                      SecModelNumberInterface,
                                      BerInterface, MsgProcInterface {
    private final boolean debug = false;

    public MsgProcSnmpV2() {
        sm = new SecModelCommunity(SECMODEL_V2C);
    }
    public String toString() {
        return "SNMPv2C(#1)";
    }
    public int getMsgProcModel() {
        return SNMP_VERSION_2C;
    }
    public boolean isMsgProcModel(int m) {
        if (debug) dump("msg model (#" + m + ")");
        return (m == SNMP_VERSION_2C);
    }
    public SecSessDataInterface discovery(int s) throws SnmpEngineException {
        if (debug) dump("sec model (#" + s + ")");      
        if (s == SECMODEL_V2C) {
            return sm.createSessData();
        }
        throw new SnmpEngineException("no such security model");
    }
    /**
     * These are all implemented in super class:
     * public SnmpMessage decode(int version, BerDecoder raw_packet)
     *                                          throws SnmpMessageException
     * public byte[] encode(SnmpMessage msg) throws SnmpMessageException
     */
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
        case SnmpPdu.SNMP_MSG_GETBULK:
            length = raw_packet.decodeLength();
            raw_packet.decodeLengthEquals(length);
            pdu.setRequestId(raw_packet.decodeSnmpInteger());
            pdu.setNonRepeaters(raw_packet.decodeSnmpInteger());
            pdu.setMaxRepetitions(raw_packet.decodeSnmpInteger());
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
        case SnmpPdu.SNMP_MSG_INFORM:
        case SnmpPdu.SNMP_MSG_TRAP2:
        case SnmpPdu.SNMP_MSG_REPORT:
            bre.encodeVarbindList(pdu.getVarbindList());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getErrorIndex());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getErrorStatus());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getRequestId());
            break;    
        case SnmpPdu.SNMP_MSG_GETBULK:
            bre.encodeVarbindList(pdu.getVarbindList());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getMaxRepetitions());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getNonRepeaters());
            bre.encodeAsnInteger(SNMP_INTEGER, pdu.getRequestId());
            break;
        default:
            throw new BerException("pdu type '" + pdu.getPduType() + "' not supported");
        }
        if (debug) dump("PDU header");
        return bre.encodeHeader(pdu.getPduType(), bre.position());
    }

}

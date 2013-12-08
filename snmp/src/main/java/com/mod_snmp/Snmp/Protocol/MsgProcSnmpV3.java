package com.mod_snmp.Snmp.Protocol;
import com.mod_snmp.Snmp.Utils.TypeConversions;

/**
 * SNMP version 1 message processor.
 * The MsgProcSnmpV1 class decodes/encodes an SNMP version 1 messages.
 * The class extends the MsgProcSnmpV2:
 *    different static message values,
 *    different message format (encode/decode functions).
 *    same PDU types (encodePDU/decodePDU functions),                    
 */
public class MsgProcSnmpV3 extends MsgProcSnmpV2
       	                    implements SnmpMessageInterface,    
                                       BerInterface, MsgProcInterface {
    private final boolean debug = false;

    private SecuritySystem secSys;

    public MsgProcSnmpV3() {
        secSys =  new SecuritySystem();
    }
    public String toString() {
        return "SNMPv3(#3)";
    }
    public int getMsgProcModel() {
        return SNMP_VERSION_3;
    }
    public boolean isMsgProcModel(int m) {
        if (debug) dump("msg model (#" + m + ")");
        return (m == SNMP_VERSION_3);
    }
    public SecSessDataInterface discovery(int s) {
        if (debug) dump("sec model (#" + s + ")");
        return (secSys.getSecModel(s)).createSessData();
    }
    public void addSecModel(SecModelInterface si) {
        secSys.addSecModel(si);
    }
    public SnmpMessage decode(int version, BerDecoder raw_packet)
                                                throws SnmpMessageException {
        try {
            if (debug) dump("Decode processor");
            SnmpMessage msg = new SnmpMessage();
            msg.version = version;
            /* Parse first the common header. */
            int length_header = raw_packet.decodeSequenceHeader();
            msg.messageId = raw_packet.decodeSnmpInteger();
            if (debug) dump("       MsgId: " + msg.messageId);
            msg.size = raw_packet.decodeSnmpInteger();
            if (debug) dump("       MaxMsgSize: " + msg.size);
            msg.flags = raw_packet.decodeSnmpOctetStr();
            if (msg.flags.length != 1) {
                throw new BerException("Flags do not have correct size");
            }
            if (debug) dump("       Flags: " +
                            TypeConversions.bytes2HexString(msg.flags));
            msg.secModel = raw_packet.decodeSnmpInteger();

            /* Parse now the security model specific security parameters.
             * The security parameters are coded within an octet string.
             */
            SecModelInterface si = secSys.getSecModel(msg.secModel);
            if (debug) dump("Security Model: " + si);
            raw_packet.decodeTag(ASN_OCTET_STR);
            int length_parameters = raw_packet.decodeLength();
            if (debug) dump("     Security Parameters: length " +
                            length_parameters);
            raw_packet.decodeLengthAvailable(length_parameters);
            int available = raw_packet.available();
            si.decodeSecurityParameters(raw_packet, msg);
            if (debug) dump("available " + raw_packet.available() +
                            " and should have " + available + " - " +
                            length_parameters + " = "
                            + (available - length_parameters));
            if (raw_packet.available() != (available - length_parameters)) {
                throw new SnmpMessageException("Security Parameters decoding" +
                            " used more then its length of data");
            }
            if (msg.hasAuthentication()) {
                si.decodeAuthentication(raw_packet, msg);
                if (msg.hasPrivacy()) {
                    raw_packet = si.decrypt(raw_packet, msg);
                }
            }
            
            /**
             * Parse the plain text scoped PDU.
             */
            int length_scopedPdu = raw_packet.decodeSequenceHeader();
            byte[] contextEngineId = raw_packet.decodeSnmpOctetStr();
            byte[] contextName = raw_packet.decodeSnmpOctetStr();
            msg.setPdu(decodePDU(raw_packet), contextEngineId, contextName);
            if (debug) dump("Decode processor done");
            return msg;
        } catch (SecModelException e) {
            throw new SnmpMessageException(e.getMessage());
        } catch (BerException e) {
            throw new SnmpMessageException(e.getMessage());
        }
    }
    public byte[] encode(SnmpMessage msg) throws SnmpMessageException {
        try {
            if (debug) dump("Encode processor");
            BerEncoderReverse raw_packet = new BerEncoderReverse(msg.size);
            /**
             * Encode the scoped PDU.
             */
            encodeScopedPdu(raw_packet, msg.pdu);
            /**
             * Retrieve the security model.
             */
            SecModelInterface si = secSys.getSecModel(msg.secModel);
            if (debug) dump("Security Model: " + si);
            /**
             * Encrypt scopedPDU if needed.
             */
            if (debug) dump(raw_packet.toByteArray());
            if (msg.hasPrivacy()) {
                if (!msg.hasAuthentication()) {
                    throw new SnmpMessageException(
                            "Privacy without authentication is not allowed.");
                }
                raw_packet = si.encrypt(raw_packet, msg);
                if (debug) dump(raw_packet.toByteArray());
            }
            /**
             * Encode the security model specific security parameters.
             * The security parameters are encoded within an octet string.
             * This time we provide an phase 1 parameters.
             * If authentication is needed the authParameters will be the
             * 12 bytes of '0'. This needs to be replaced by the MAC.
             */
            int length = si.encodeSecurityParameters(raw_packet, msg);
            raw_packet.encodeHeader(ASN_OCTET_STR, length);
            if (debug) dump(raw_packet.toByteArray());
            /**
             * Encode the message header.
             */
            length = encodeMessageHeader(raw_packet, msg);
            length = raw_packet.encodeHeader(SNMP_SEQUENCE, length);
            /**
             * Add the version.   
             */
            length = raw_packet.encodeSnmpInteger(SnmpMessage.SNMP_VERSION_3);
            /**
             * Add the full message header.
             */
            raw_packet.encodeHeader(SNMP_SEQUENCE, length);
            if (debug) dump(raw_packet.toByteArray());
            /*
             * Encode the security model specific authentication parameters.
             * We return directly from si.encodeAuthentication, since it
             * returns the byte[] already.
             */
            if (msg.hasAuthentication()) {
                return si.encodeAuthentication(raw_packet, msg);
            }
            return raw_packet.toByteArray();
        } catch (BerException e) {
            throw new SnmpMessageException(e.getMessage());
        } catch (SecModelException e) {
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
    public int encodeMessageHeader(BerEncoderReverse bre, SnmpMessage msg) {
        int start = bre.position();
        bre.encodeSnmpInteger(msg.secModel);
        bre.encodeSnmpOctetStr(msg.flags);
        bre.encodeSnmpInteger(msg.size);
        int end = bre.encodeSnmpInteger(msg.messageId);
        return (end - start);
    }
    public int encodeScopedPdu(BerEncoderReverse bre, SnmpPdu pdu)
                                                      throws BerException {
        encodePdu(bre, pdu);
        bre.encodeSnmpOctetStr(pdu.context.name);
        int length = bre.encodeSnmpOctetStr(pdu.context.engineId);
        return bre.encodeHeader(SNMP_SEQUENCE, length);
    }
    /**
     * Debug dumper
     */
    private void dump(byte[] array) {
        dump(TypeConversions.bytes2HexString(array));
    }
}

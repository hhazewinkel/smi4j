package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.Snmp.Utils.TypeConversions;

/**
 * The local representation of an SNMP message.
 */
public class SnmpMessage implements SnmpMessageInterface {
    /**
     * A zeroLength byte[], commonly used to avoid NullPointers.
     */
    public static final byte[] zeroLength = new byte[0];
    /**
     * SNMPv3 message flags.
     */
    private static final byte SNMPV3_FLAG_AUTH_BIT  = 0x01;
    private static final byte SNMPV3_FLAG_AUTH_MASK = ~(0x01);
    private static final byte SNMPV3_FLAG_PRIV_BIT  = 0x02;
    private static final byte SNMPV3_FLAG_PRIV_MASK = ~(0x02);
    private static final byte SNMPV3_FLAG_REPT_BIT  = 0x04;
    private static final byte SNMPV3_FLAG_REPT_MASK = ~(0x04);
    /**
     * SNMP message fields.
     */
    protected SnmpPdu pdu;
    protected int messageId = 0;
    /**
     * A common defined version for an SNMP message.
     */
    protected int version;
    /**
     * SNMPv3 message parameters.
     */
    protected int size = SnmpEngine.MAX_MESSAGE_SIZE;
    protected byte[] flags = new byte[1];
    protected int secModel;
    /**
     * SNMPv3 message security parameters.
     */
    protected SecParametersInterface secInfo;
    /**
     * Constructors.
     */
    protected SnmpMessage() {
    }
    private SnmpMessage(int v, SecParametersCommunity community,
                      SnmpPdu p) throws SnmpMessageException {
        version = v;
        secInfo = community;
        pdu = p;
    }
    static protected SnmpMessage createSnmpV1Msg(SecParametersInterface ssi,
                        SnmpPdu p) throws SnmpMessageException {
        return new SnmpMessage(SNMP_VERSION_1,
                       (SecParametersCommunity)ssi, p);
    }
    static protected SnmpMessage createSnmpV1Msg(String community,
                        SnmpPdu p) throws SnmpMessageException {
        return new SnmpMessage(SNMP_VERSION_1,
                        new SecParametersCommunity(community), p);
    }
    static protected SnmpMessage createSnmpV1Msg(byte[] community,
                        SnmpPdu p) throws SnmpMessageException {
        return new SnmpMessage(SNMP_VERSION_1,
                        new SecParametersCommunity(community), p);
    }
    static protected SnmpMessage createSnmpV2Msg(SecParametersInterface ssi,
                        SnmpPdu p) throws SnmpMessageException {
        return new SnmpMessage(SNMP_VERSION_1,
                        (SecParametersCommunity)ssi, p);
    }
    static protected SnmpMessage createSnmpV2Msg(String community,
                      SnmpPdu p) throws SnmpMessageException {
        return new SnmpMessage(SNMP_VERSION_2C,
                        new SecParametersCommunity(community), p);
    }
    static protected SnmpMessage createSnmpV2Msg(byte[] community,
                      SnmpPdu p) throws SnmpMessageException {
        return new SnmpMessage(SNMP_VERSION_2C,
                        new SecParametersCommunity(community), p);
    }
    private SnmpMessage(int v, int s,
                      boolean auth, boolean priv, boolean report,
                      int sm, SecParametersInterface si,
                      Context c, SnmpPdu p) throws SnmpMessageException {
        version = v;
        size = s;
        sessionFlags(auth, priv, report);
        secModel = sm;
        secInfo = si;
        p.context = c;
        pdu = p;
    }
    static protected SnmpMessage createSnmpV3Msg(int s,
                      boolean auth, boolean priv, boolean report,
                      int sm, SecParametersInterface si,
                      Context c, SnmpPdu p) throws SnmpMessageException {
        return new SnmpMessage(SNMP_VERSION_3, s,
                                auth, priv, report, sm, si, c, p);
    }
    
    /**
     * Set the message id to value.
     */
    public void setMessageId(int mid) {
        messageId = mid;
        try {
            pdu.setRequestId(messageId);
        } catch (NullPointerException e) {
            /* Just catch. Otherwise we don't set the PDU reqId. */
            /* The value is then '0'. */
        }
    }
    public int getMessageId() {
        return messageId;
    }
    public int getMaxMsgSize() {
        return size;
    }
    public void setSecModel(int sm) {
        secModel = sm;
    }
    public void setPdu(SnmpPdu payload) {
         setPdu(payload, zeroLength, zeroLength);
    }
    public void setPdu(SnmpPdu payload,
                                byte[] contextEngineId,
                                byte[] contextName) {
        pdu = payload;
        pdu.context.engineId = contextEngineId;
        pdu.context.name = contextName;
    }
    public SnmpPdu getPdu() {
        return pdu;
    }
    public boolean hasAuthentication() {
        return (0 != (SNMPV3_FLAG_AUTH_BIT & flags[0]));
    }
    public boolean hasPrivacy() {
        return (0 != (SNMPV3_FLAG_PRIV_BIT & flags[0]));
    }
    public boolean hasReportable() {
        return (0 != (SNMPV3_FLAG_REPT_BIT & flags[0]));
    }
    public void setAuthentication(boolean auth) {
        if (auth) {
            flags[0] |= SNMPV3_FLAG_AUTH_BIT;
        } else {
            flags[0] &= SNMPV3_FLAG_AUTH_MASK;
        }
    }
    public void setPrivacy(boolean priv) {
        if (priv) {
            flags[0] |= SNMPV3_FLAG_PRIV_BIT;
        } else {
            flags[0] &= SNMPV3_FLAG_PRIV_MASK;
        }
    }
    public void setReportable(boolean report) {
        if (report) {
            flags[0] |= SNMPV3_FLAG_REPT_BIT;
        } else {
            flags[0] &= SNMPV3_FLAG_REPT_MASK;
        }
    }
    public void sessionFlags(boolean auth, boolean priv, boolean report) {
        setReportable(report);
        setAuthentication(auth);
        setPrivacy(priv);
    }
    public int getVersion() {
         return version;
    }
    public static String versionToString(int v) {
        switch (v) {
        case SNMP_VERSION_1:  return "SNMPv1: " + v;
        case SNMP_VERSION_2C: return "SNMPv2c: " + v;
        case SNMP_VERSION_3:  return "SNMPv3: " + v;
        }
        return "unknown:" + v;
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("--- SNMP Message ----------");
        System.out.println("  version         : " + versionToString(version));
        String tmp_str;
        System.out.println("  size            : " + size);
        System.out.println("  security model  : " + secModel);
        System.out.println("  flags           : " +
                    TypeConversions.bytes2HexString(flags));
        try {
            secInfo.dump();
        } catch (NullPointerException e) {
            System.out.println("<null>");
        }
        try {
            pdu.dump();
        } catch (NullPointerException e) {
            System.out.println("  SNMP PDU        : <null>");
        }
        System.out.println("---------------------------");
    }

}

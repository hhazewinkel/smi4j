package com.mod_snmp.Snmp.Protocol;

import com.mod_snmp.Snmp.Values.VarbindList;

/**
 * The SNMP PDU.
 */
public class SnmpPdu implements SnmpPduInterface {
    public static final byte[] zeroLength = new byte[0];
    /**
     * Context Information.
     * For SNMPv1 and SNMPv2c the contextName can be used for the cummunity.
     */
    protected Context context = new Context();

    /**
     * Plain PDU parameters.
     */
    private byte pdu_type;
    private int requestId;
    private int errorStatus = 0;
    private int errorIndex = 0;
    private int nonRepeaters = 0;
    private int maxRepetitions = 0;
    private VarbindList varbindList;

    public SnmpPdu() 
                                                throws SnmpMessageException {
            this(SNMP_MSG_GET);
    }
    public SnmpPdu(byte tp) 
                                                throws SnmpMessageException {
        this(tp, new VarbindList());
    }
    public SnmpPdu(byte tp, VarbindList vbl)
                                                throws SnmpMessageException {
        pdu_type = tp;
        varbindList = vbl;
    }

    public void setPduType(int type)
                                                throws SnmpMessageException {
        if ((type < SNMP_MSG_GET) && (SNMP_MSG_REPORT < type)) {
            throw new SnmpMessageException("Incorrect PDU type '" + type + "'");
        }
        pdu_type = (byte)type;
    }
    public byte getPduType() {
        return pdu_type;
    }
    public String pduTypeToString() {
       switch (pdu_type) {
        case SNMP_MSG_GET:
            return "SNMP-Get";
        case SNMP_MSG_GETNEXT:
            return "SNMP-GetNext";
        case SNMP_MSG_RESPONSE:
            return "SNMP-Response";
        case SNMP_MSG_SET:
            return "SNMP-Set";
        case SNMP_MSG_TRAP:
            return "SNMP-TrapV1";
        case SNMP_MSG_INFORM:
            return "SNMP-Inform";
        case SNMP_MSG_TRAP2:
            return "SNMP-TrapV2";
        case SNMP_MSG_REPORT:
            return "SNMP-Report";
        case SNMP_MSG_GETBULK:
            return "SNMP-GetBulk";
        }
        return "Unknown " + (pdu_type & 0xFF) + "'";
    }
    public void setRequestId(int reqId) {
        requestId = reqId;
    }
    public int getRequestId() {
        return requestId;
    }
    public void setErrorStatus(int status) throws SnmpMessageException {
        if ((status < 0) || (SNMP_ERR_MAXIMUM < status)) {
            throw new SnmpMessageException("ErrorStatus exceeds range '"
                                             + status + "'");
        }
        errorStatus = status;
    }
    public int getErrorStatus() {
        return errorStatus;
    }
    public String getErrorStatus(String s) {
        switch (errorStatus) {
        case SNMP_ERR_TOOBIG:
            return "too big";
        case SNMP_ERR_NOSUCHNAME:
            return "no such name";
        case SNMP_ERR_BADVALUE:
            return "bad value";
        case SNMP_ERR_READONLY:
            return "read only";
        case SNMP_ERR_GENERR:
            return "gen error";
        case SNMP_ERR_NOACCESS:
            return "no access";
        case SNMP_ERR_WRONGTYPE:
            return "wrong type";
        case SNMP_ERR_WRONGLENGTH:
            return "wrong length";
        case SNMP_ERR_WRONGENCODING:
            return "wrong encoding";
        case SNMP_ERR_WRONGVALUE:
            return "wrong value";
        case SNMP_ERR_NOCREATION:
            return "no creation";
        case SNMP_ERR_INCONSISTENTVALUE:
            return "inconsistent value";
        case SNMP_ERR_RESOURCEUNAVAILABLE:
            return "resource unavailable";
        case SNMP_ERR_COMMITFAILED:
            return "commit failed";
        case SNMP_ERR_UNDOFAILED:
            return "undo failed";
        case SNMP_ERR_AUTHORIZATIONERROR:
            return "authorization error";
        case SNMP_ERR_NOTWRITABLE:
            return "not writable";
        case SNMP_ERR_INCONSISTENTNAME:
            return "inconsistent name";
        }
        return "no error";
    }

    public boolean isErrorStatusSet() {
        return (errorStatus != SNMP_ERR_NOERROR);
    }
    public void setErrorValue(int index) throws SnmpMessageException {
        errorIndex = index;
    }
    public int getErrorIndex() {
        return errorIndex;
    }
    public void setNonRepeaters(int nr) {
        nonRepeaters = nr;
    }
    public int getNonRepeaters() {
        return nonRepeaters;
    }
    public void setMaxRepetitions(int mr) {
        maxRepetitions = mr;
    }
    public int getMaxRepetitions() {
        return maxRepetitions;
    }
    public void setVarbindList(VarbindList vbl) {
        varbindList = vbl;
    }
    public VarbindList getVarbindList() {
        return varbindList;
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        context.dump();
        System.out.println("    SNMP PDU");
        System.out.println("    Type               : " + pduTypeToString());
        System.out.println("    Request Id         : " + requestId);
        if (pdu_type == SNMP_MSG_GETBULK) {
            System.out.println("    Non-Repeaters  : " + nonRepeaters);
            System.out.println("    Max-Repetitions: " + maxRepetitions);
        }
        System.out.println("    Error-Status       : " + errorStatus);
        System.out.println("    Error-Value        : " + errorIndex);
        varbindList.dump();
    }
}

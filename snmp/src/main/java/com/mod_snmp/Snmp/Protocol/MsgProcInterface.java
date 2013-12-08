package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

public interface MsgProcInterface {
    public int getMsgProcModel();
    public boolean isMsgProcModel(int m);
    public SecSessDataInterface discovery(int s) throws SnmpEngineException;
    public SnmpMessage decode(int version, BerDecoder raw_packet)
                                                throws SnmpMessageException;
    public byte[] encode(SnmpMessage msg)
                                                throws SnmpMessageException;
}

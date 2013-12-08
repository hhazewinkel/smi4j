package com.mod_snmp.Snmp.Protocol;

import com.mod_snmp.Snmp.Utils.TypeConversions;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * The EngineId class is used to identify uniquely an SNMP engine.
 */
public class EngineId {
    public static final EngineId zeroLengthEngineId = new EngineId("");

    protected byte[] value;

    /**
     * The constructor in order to create a local engine id.
     * No parameters are given and the local engine id is
     * retrieved by probing the system.
     */
    public EngineId() {
        probe();
    }
    /**
     * The constructor in order to create a remote engine id.
     * The parameter str is a textual string representation 
     * in Hexadecimal format.
     */
    public EngineId(String str) {
        value = TypeConversions.string2Bytes(str);
    }
    /**
     * The constructor in order to create a remote engine id.
     * The parameter bytes is a byte array representation.
     */
    public EngineId(byte[] bytes) {
        value = bytes;
    }

    /**
     * The probe to generate the local engine id.
     */
    private void probe() {
        value = new byte[9];
        value[4] = 0x01;
        try {
            InetAddress address = InetAddress.getLocalHost();
            byte [] addr = address.getAddress();
            value[5] = addr[0];
            value[6] = addr[1];
            value[7] = addr[2];
            value[8] = addr[3];
        } catch (UnknownHostException e) {
        }
    }
    public byte[] getValue() {
        return value;
    }

    /**
     * Return the engine id as an hexadecimal string.
     */
    public String toString() {
        return TypeConversions.bytes2HexString(value);
    }

    /**
     * Test for checking the probe.
     */
    public static void main(String[] args) {
        EngineId eid = new EngineId();
        System.out.println("EngineId: " + eid);
    }
}

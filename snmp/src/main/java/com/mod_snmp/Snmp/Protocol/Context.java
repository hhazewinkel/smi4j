package com.mod_snmp.Snmp.Protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import com.mod_snmp.Snmp.Utils.TypeConversions;

/**
 * Context Information is composed of an engine id and a name. 
 * The context information can be used in order to access
 * a specific view of the MIB in the remote SNMP agent.
 * The engine id is then used for the specific SNMP
 * engine of the remote SNMP agent and the name for the
 * user on behalf access is done.
 */
public class Context {
    /**
     * local veriable to be used to initialize the context
     * variables.
     */
    private static final byte[] zeroLength = new byte[0];
    /**
     * The 'zero' context.
     */
    public static final Context zeroContext = new Context();

    protected byte[] engineId;
    protected byte[] name;

    /**
     * Default constructor of a context.
     * The initial values while by the zero length names.
     */
    public Context() {
        engineId = zeroLength;
        name = zeroLength;
    }
    /**
     * Constructor for the context from the engine id and name.
     */
    public Context(byte[] eId, byte[] n) {
        engineId = eId;
        name = n;
    }
    /**
     * Constructor for the context from the engine id and name.
     */
    public Context(String eId, String n) {
        engineId = eId.getBytes();
        name = n.getBytes();
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("    Context");
        System.out.println("      engine Id: " +
                    TypeConversions.bytes2HexString(engineId));
        System.out.println("      name     : " +
                    TypeConversions.bytes2HexString(name));
    }
}

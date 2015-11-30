package com.mod_snmp.snmp.protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.snmp.utils.ServiceAddress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

/**
 * The SnmpEngine class composes the SNMP protocol engine.
 * The architecture of the SNMP engine follows the design
 * of the SNMPv3 framework. The framework architecture enables
 * the ability for multiple message formats identified by a
 * protocol version number and multiple security models.
 * The SNMP engine performs as a message dispatcher handling
 * application requests until an SNMP message on-the-wire or
 * the processing of an on-the-wire received mesage until
 * the hand-over to the management application.
 *
 * @author Harrie Hazewinkel harrie@lisanza.net
 */
public class SnmpEngine {
    private static final boolean debug = false;
    /**
     * The default transport port used for SNMP.
     */
    protected static final int defaultPort = 161;
    protected static ServiceAddress local = new ServiceAddress(defaultPort);
    /**
     * The maximum message length this SnmpEngine can handle.
     */
    protected static final int MAX_MESSAGE_SIZE = 65507;

    /**
     * The socket used for network communication.
     */
    private DatagramSocket socket;
    /**
     * The transmit and received buffers for network communication.
     */
    private byte[] txmit_buf;
    private byte[] recv_buf;
    /**
     * The MessageProcessin System
     */
    private MsgProcessingSystem mps;
    private int sequenceNumber;
    private EngineId eId;
    private int eBoots;
    private EngineTime eTime;

    /**
     * Default constructor for an SNMP engine which
     * creates a default 'MAX_MESSAGE_SIZE' maximum message size.
     */
    public SnmpEngine() {
        this(MAX_MESSAGE_SIZE);
    }
    /**
     * Default constructor for an SNMP engine.
     * creates a default 'MAX_MESSAGE_SIZE' maximum message size.
     */
    public SnmpEngine(int max_msg_size) {
        txmit_buf = new byte[ max_msg_size ];
        recv_buf = new byte[ max_msg_size ];
        mps = new MsgProcessingSystem();
        Random rnd = new Random();
        sequenceNumber = rnd.nextInt();
        eId = new EngineId();
        eBoots = 0;
        eTime = new EngineTime();
    }
    public void open() throws SocketException {
        open(defaultPort);
    }
    public void open(int port) throws SocketException {
        if (port > 0) {
            socket = new DatagramSocket(port);
        } else {
            socket = new DatagramSocket();    
        }
    }
    public void open(int port, InetAddress addr) throws SocketException {
        socket = new DatagramSocket(port, addr);
    }
    public void open(InetAddress addr) throws SocketException {
        open(defaultPort, addr);
    }
    public void addMsgProcModel(MsgProcInterface mpm) {
        mps.addMsgProcModel(mpm);
    }
    public SnmpMessage receive() throws SnmpMessageException, IOException {
        SnmpMessage msg = null;
        DatagramPacket packet =  new DatagramPacket(recv_buf, recv_buf.length);
        socket.receive(packet);
        if (debug) dump("received from", packet);
        msg = mps.decode(packet);
        if (debug) msg.dump();
        return msg;
    }
    public void send(SnmpMessage msg, ServiceAddress target)
                                        throws SnmpMessageException,
                                        IOException {
        msg.setMessageId(getSequenceNumber());
        if (debug) msg.dump();
        DatagramPacket packet = mps.encode(msg);
        packet.setAddress(target.getInetAddress());
        packet.setPort(target.getPort());
        socket.send(packet);
        if (debug) dump("send to", packet);
    }
    public SnmpMessage send_receive(SnmpMessage txmit_msg, SnmpSession s) {
        SnmpMessage recv_msg = null;
        try {
            send(txmit_msg, s.target);
            socket.setSoTimeout(50000);
            recv_msg = receive();
            s.updateSecurityData(recv_msg);
            return recv_msg;
        } catch (SnmpMessageException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Time out");
        }
        return null;
    }

    private int getSequenceNumber() {
        sequenceNumber++;
        if (sequenceNumber < 0) {
            sequenceNumber = 0;
        }
        return sequenceNumber;
    }
    public void discovery(SnmpSession s)
                                throws SnmpEngineException {
        s.setSecurityData(mps.discovery(s.getVersion(), s.getSecModel()));
        try {
            SnmpMessage msg = s.discoveryMsg();
            if (msg != null) {
                msg = send_receive(msg, s);
            }
            s.fixup(msg);
        } catch (SecModelException e) {
            throw new SnmpEngineException("security model error: " +
                                                        e.getMessage());
        } catch (SnmpMessageException e) {
            throw new SnmpEngineException("message handling error: " +
                                                        e.getMessage());
        }
    }
    /**
     * This convenience function builds a multi-lingual SNMP engine able 
     * to communicate the SNMPv1/SNMPv2C/SNMPv3 message formats.
     */
    public static SnmpEngine makeDefault()
                                throws SnmpEngineException {
        SnmpEngine engine = new SnmpEngine();
        MsgProcSnmpV1 mps1 = new MsgProcSnmpV1();
        engine.addMsgProcModel(mps1);
        MsgProcSnmpV2 mps2 = new MsgProcSnmpV2();
        engine.addMsgProcModel(mps2);
        MsgProcSnmpV3 mps3 = new MsgProcSnmpV3();
        mps3.addSecModel(new SecModelUsm());
        engine.addMsgProcModel(mps3);
        try {
            engine.open(0);
        } catch (SocketException e) {
            throw new SnmpEngineException(e.getMessage());
        }
        return engine;
    }

    /**
     * This convenience function builds an SNMP engine that performs
     * the (community based) SNMPv1 and SNMPv2C communication.
     */
    public static SnmpEngine makeCommunityBased()
                                throws SnmpEngineException {
        SnmpEngine engine = new SnmpEngine();
        MsgProcSnmpV1 mps1 = new MsgProcSnmpV1();
        engine.addMsgProcModel(mps1);
        try {
            engine.open(0);
        } catch (SocketException e) {
            throw new SnmpEngineException(e.getMessage());
        }
        return engine;
    }

    public static SnmpEngine makeSnmpV3()
                                throws SnmpEngineException {
        SnmpEngine engine = new SnmpEngine();
        MsgProcSnmpV3 mps3 = new MsgProcSnmpV3();
        mps3.addSecModel(new SecModelUsm());
        engine.addMsgProcModel(mps3);
        try {
            engine.open(0);
        } catch (SocketException e) {
            throw new SnmpEngineException(e.getMessage());
        }
        return engine;
    }

    /**
     * Debug dumper.
     */
    private void dump(String i, DatagramPacket p) {
        System.out.println("SnmpEngine: Message (" + p.getLength() + " bytes) "
                        + i + " " + p.getAddress() + ":" + p.getPort());
    }
}

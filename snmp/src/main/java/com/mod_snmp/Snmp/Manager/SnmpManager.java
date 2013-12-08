package com.mod_snmp.Snmp.Manager;

import com.mod_snmp.Snmp.Protocol.EngineId;
import com.mod_snmp.Snmp.Protocol.SnmpEngine;
import com.mod_snmp.Snmp.Protocol.SnmpEngineException;
import com.mod_snmp.Snmp.Protocol.SnmpMessage;
import com.mod_snmp.Snmp.Protocol.SnmpMessageException;
import com.mod_snmp.Snmp.Protocol.SnmpPdu;
import com.mod_snmp.Snmp.Protocol.SnmpSession;
import com.mod_snmp.Snmp.Protocol.User;
import com.mod_snmp.Snmp.Utils.ServiceAddress;
import com.mod_snmp.Snmp.Values.VarbindList;

/**
 * The SnmpManager class is a wrapper around the SNMP protocol to abstract
 * protocol specific features. The wrapper combines associates an SNMP
 * user, a SNMP target and an SNMP engine. The SNMP user contains
 * administrative information on behalf communication with a specific SNMP
 * target. Where the SNMP engine provides the protocol specific messaging
 * with the target.
 * The SNMP manager class provides message (PDU) invocation methods to
 * perform management application requests to the SNMP target.
 * For each specific target a seperate SNMP Manager is required due to
 * the SNMP session that contains target specific information, such as
 * encryption keys. However, it is possible to have for multiple SNMP
 * managers a single SNMP engine. This usage is even adviced.
 * This class will handle also the the synchronous and asynchronous
 * protocol behaviour. (Future version)
 */
public class SnmpManager {
    /**
     * The private SNMP engine that this application will use for
     * its communication.
     */
    private SnmpEngine engine;
    /**
     * The SNMP session information that is used by this application.
     */
    private SnmpSession session;
    /**
     * Private variable for a varbind list that could be used for
     * polling. No need to create each time a new one.
     */
    private VarbindList varbindList;
    /**
     * This is a silencer for compiler warnings when this class is
     * extended to make meaningfull applications.
     * Otherwise, the subclass must define a constructor.
     */
    public SnmpManager() {
        new Error("Manager not correctly set up");
    }
    /**
     * The constructor that sets up an application for a specific user for
     * an SNMP target (addr:port).
     * The construction of this application includes the SNMPv3
     * discovery phase for the remote engine Id if needed.
     * This constructor creates its own (private) SNMP engine.
     */
    public SnmpManager(ServiceAddress addr,
                                        User u)
                   throws SnmpManagerException, SnmpEngineException {
        this(SnmpEngine.makeDefault(), addr, u);
    }
    /**
     * The constructor that sets up an application for a specific community
     * and protocol version (1/2c only) for an SNMP target (addr:port).
     * This constructor creates its own (private) SNMP engine.
     */
    public SnmpManager(ServiceAddress addr,
                                        String community, int version) 
                   throws SnmpManagerException, SnmpEngineException {
        this(SnmpEngine.makeCommunityBased(), addr, new User(community, version));
    }
    /**
     * The constructor that sets up an application for a specific user for
     * an SNMP target (addr:port).
     * The construction of this application includes the SNMPv3
     * discovery phase for the remote engine Id if needed.
     * This constructor is useful when you want to build a management
     * application that uses a 'shared' SNMP engine.
     */
    public SnmpManager(SnmpEngine se, ServiceAddress addr, User u)
                throws SnmpManagerException, SnmpEngineException {
        engine = se;
        session = new SnmpSession(addr, u);
        discovery();
    }
    /**
     * The discovery in order to setup the security model
     * information for the SnmpSession used in the SnmpManager.
     * For SNMPv1/SNMPv2C this involves the setup of the
     * SecModelCommunity parameters for the version.
     * For SNMPv3 this involves the setup of the security
     * model parameters and the discovery of the remote
     * engine id.
     */
    public void discovery() throws SnmpManagerException, SnmpEngineException{
        engine.discovery(session);
    }
    /**
     * Set the Context information.
     * If not set the default context is used.
     * The default context is:
     * context engine == remote engine id
     * context name == ""
     */
    public void setContext(EngineId ce, String cn) {
        session.setContext(ce, cn);
    }
    /**
     * Set the security level for agent communication.
     * The possible options are checked against the provided
     * User of the SNMP manager.
     */
    public void setSecurityLevel(boolean auth, boolean priv)
                                        throws SnmpEngineException {
	session.setSecurityLevel(auth, priv);
    }
    /**
     * To perform repetitive polling of the same varbind list.
     */
    public void setVarbindList(VarbindList vbl) {
        varbindList = vbl;
    }
    /**
     * message invocation method for an SNMP GET request.
     */
    public SnmpPdu invokeSnmpGet(VarbindList vbl)
                                throws SnmpMessageException {
        return invoke(SnmpPdu.SNMP_MSG_GET, vbl, true);
    }
    /**
     * message invocation method for an SNMP GET-NEXT request.
     */
    public SnmpPdu invokeSnmpGetNext(VarbindList vbl)
                                throws SnmpMessageException {
        return invoke(SnmpPdu.SNMP_MSG_GETNEXT, vbl, true);
    }
    /**
     * message invocation method for an SNMP SET request.
     */
    public SnmpPdu invokeSnmpSet(VarbindList vbl)
                                throws SnmpMessageException {
        return invoke(SnmpPdu.SNMP_MSG_SET, vbl, true);
    }
    /**
     * message invocation method for an SNMP GETBULK request.
     */
    public SnmpPdu invokeSnmpGetBulk(VarbindList vbl)
                                throws SnmpMessageException {
        return invoke(SnmpPdu.SNMP_MSG_GETBULK, vbl, true);
    }
    /**
     * message invocation method for an SNMP TRAP request.
     */
    public SnmpPdu invokeSnmpTrap(VarbindList vbl)
                                throws SnmpMessageException {
        return invoke(SnmpPdu.SNMP_MSG_TRAP, vbl, true);
    }
    /**
     * message invocation method for an SNMP INFORM request.
     */
    public SnmpPdu invokeSnmpInform(VarbindList vbl)
                                throws SnmpMessageException {
        return invoke(SnmpPdu.SNMP_MSG_INFORM, vbl, true);
    }
    /**
     * message invocation method for an SNMP TRAP2 request.
     */
    public SnmpPdu invokeTrap2(VarbindList vbl)
                                throws SnmpMessageException {
        return invoke(SnmpPdu.SNMP_MSG_TRAP2, vbl, true);
    }
    /**
     * message invocation method.
     */
    public SnmpPdu invoke(byte pduType, VarbindList vbl, boolean report)
                                throws SnmpMessageException {
        SnmpMessage msg = session.createMsg(pduType, vbl);
        msg = engine.send_receive(msg, session);
        return msg.getPdu();
    }
    /**
     * Debug function to view the settings for the SNMP session.
     */
    public void dump() {
        System.out.println("Snmp Manager Info");
        session.dump();
    }
}

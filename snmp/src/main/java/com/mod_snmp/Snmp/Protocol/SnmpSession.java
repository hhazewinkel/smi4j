package com.mod_snmp.Snmp.Protocol;

import com.mod_snmp.Snmp.Utils.ServiceAddress;
import com.mod_snmp.Snmp.Values.VarbindList;

/**
 * This class represents a session of the SNMP application.
 * An instance of this class must be created via one of the 'factory'
 * methods, 'create...'.
 * This is to ensure that the developer creates correct 
 * setup instances of an SnmpSession.
 */
public class SnmpSession implements SnmpMessageInterface,
                                    SecModelNumberInterface {
    public ServiceAddress target;
    private int msgSize = SnmpEngine.MAX_MESSAGE_SIZE;
    private int version;
    private User user;
    private boolean authentication = false;
    private boolean privacy = false;
    private Context context = Context.zeroContext;
    private int sm;
    private SecSessDataInterface smData;

    /**
     * For creating an SNMPv1 or SNMPv2C (community-based) session.
     * Private constructor for use in the create..Session methods
     * that ensure the corrects creation.
     */
    private SnmpSession(ServiceAddress addr, String community, int v)
                                throws SnmpEngineException {
        target = addr;
        version = v;
        user = new User(community, v);
        switch (v) {
        case SNMP_VERSION_1:
            sm = SECMODEL_V1;
            break;
        case SNMP_VERSION_2C:
            sm = SECMODEL_V2C;
            break;
        default:
            throw new SnmpEngineException("SNMP version is not Community-based");
	}
    }
    /**
     * Factory of a 'version 1' session.
     */
    static public SnmpSession createV1Session(ServiceAddress addr,
                       String community) throws SnmpEngineException {
        return new SnmpSession(addr, community,
                            SNMP_VERSION_1);
    }
    /**
     * Factory of a 'version 2' session.
     */
    static public SnmpSession createV2Session(ServiceAddress addr,
                       String community) throws SnmpEngineException {
        return new SnmpSession(addr, community,
                            SNMP_VERSION_2C);
    }
    public SnmpSession(ServiceAddress addr, User u)
			throws SnmpEngineException {
	this(addr, u, u.hasAuthentication(), u.hasPrivacy(),
                            Context.zeroContext);
    }
    /**
     * For creating a session.
     * Private constructor for use in the create..Session methods
     * that ensure the corrects creation.
     */
    private SnmpSession(ServiceAddress addr, User u,
                       boolean a, boolean p,
                       Context c) throws SnmpEngineException {
        target = addr;
        version = u.version;
        user = u;
        authentication = a;
        privacy = p;
        context = c;
        switch (version) {
        case SNMP_VERSION_1:
            sm = SECMODEL_V1;
            break;
        case SNMP_VERSION_2C:
            sm = SECMODEL_V2C;
            break;
        default:
            sm = u.secModel;
        }
    }
    /**
     * Factory of a 'version 3' session.
     */
    static public SnmpSession createV3Session(ServiceAddress addr,
                       User u) throws SnmpEngineException {
        return new SnmpSession(addr, u,
                            u.hasAuthentication(), u.hasPrivacy(),
                            Context.zeroContext);
    }
    /**
     * Factory of a 'version 3' session.
     */
    static public SnmpSession createV3Session(ServiceAddress addr,
                       User u, boolean a, boolean p,
                       EngineId ce, String cn) throws SnmpEngineException {
        return new SnmpSession(addr,
                            u, a, p, new Context(ce.value, cn.getBytes()));
    }
    static public SnmpSession createSession(ServiceAddress addr, int v,
                       User u) throws SnmpEngineException {
	switch (v) {
        case SNMP_VERSION_1:
            return createV1Session(addr, u.toString());
        case SNMP_VERSION_2C:
            return createV2Session(addr, u.toString());
        case SNMP_VERSION_3:
	    return createV3Session(addr, u);
        }
        throw new SnmpEngineException("unknown version");
    }
    protected int getVersion() {
        return version;
    }
    protected int getSecModel() {
        return sm;
    }
    /**
     * Enables session specific context.
     */
    public void setContext(EngineId ce, String cn) {
        context = new Context(ce.value, cn.getBytes());
    }
    /**
     * Set the security level for SnmpSession.
     */
    public void setSecurityLevel(boolean auth, boolean priv)
                                        throws SnmpEngineException {
        if (auth) {
            if (user.hasAuthentication()) {     
                authentication = auth;
                if (priv) {
                    if (user.hasPrivacy()) {
                        privacy = priv;
                    } else {
                        throw new SnmpEngineException("Session: " + user +
                                        " has no privacy passphrase.");
                    }
                }
            } else {
                throw new SnmpEngineException("Session: " + user +
                                        " has no authentication passphrase.");
            }
        } else if (priv) {
            throw new SnmpEngineException(
                       "Cannot use privacy without authentication");       
        }
    }
    /**
     * Test whether the session can support authentication.
     */
    public boolean hasAuthentication() {
        return (user.hasAuthentication() && authentication);
    }
    /**
     * Test whether the session can support privacy.
     */
    public boolean hasPrivacy() {
        return (user.hasPrivacy() && privacy);
    }
    /**
     * Factory for an SNMP message of the session.
     */
    public SnmpMessage createMsg(byte pduType, VarbindList vbl)
                                        throws SnmpMessageException {
        return createMsg(pduType, vbl, false);
    }
    public SnmpMessage createMsg(byte pduType, VarbindList vbl, boolean report)
                                        throws SnmpMessageException {
        switch (version) {
        case SNMP_VERSION_1:
            return SnmpMessage.createSnmpV1Msg(smData.makeSecurityParameters(),
                                        new SnmpPdu(pduType, vbl));
        case SNMP_VERSION_2C:
            return SnmpMessage.createSnmpV2Msg(smData.makeSecurityParameters(),
                                        new SnmpPdu(pduType, vbl));
        case SNMP_VERSION_3:
            return SnmpMessage.createSnmpV3Msg(msgSize,
                                hasAuthentication(), hasPrivacy(), report,
                                sm, smData.makeSecurityParameters(),
                                context, new SnmpPdu(pduType, vbl));
        }
        throw new SnmpMessageException("incorrect message version: " + version);
    }
    /**
     * Set the private security session data.
     * This is prvate, since this disables access access
     * to the localized keys from the session.
     */
    public void setSecurityData(SecSessDataInterface ssd) {
        smData = ssd;
    }
    public void updateSecurityData(SnmpMessage m) {
        if (msgSize < m.size) {
            msgSize = m.size;
        }
        context = m.pdu.context;
        smData.update(m.secInfo);
    }
    public void fixup(SnmpMessage m) throws SecModelException {
        smData.fixup(m, user);
    }
    /**
     * Factory for an SNMP V3 discovery message of the session.
     */
    public SnmpMessage discoveryMsg() throws SnmpMessageException {
        if (version == SNMP_VERSION_3) {
            return SnmpMessage.createSnmpV3Msg(msgSize,
                    false, false, true,
                    sm, smData.makeDiscoveryParameters(),
                    context, new SnmpPdu(SnmpPdu.SNMP_MSG_GET));
           
        }
        return null;
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("    Target address   : " + target);
        System.out.println("    Version          : " + version);
        System.out.println("    Size             : " + msgSize);
        try {
            user.dump();
        } catch (NullPointerException e) {
            System.out.println("    User             : <null>");
        }
        System.out.println("    Auth             : " + authentication);
        System.out.println("    Privacy          : " + privacy);
        context.dump();
        try {
            smData.dump();
        } catch (NullPointerException e) {
            System.out.println("      <null>");
        }
    }
}

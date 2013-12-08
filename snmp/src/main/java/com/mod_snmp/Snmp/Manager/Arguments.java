package com.mod_snmp.Snmp.Manager;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.Snmp.Protocol.SecModelNumberInterface;
import com.mod_snmp.Snmp.Protocol.SnmpEngineException;
import com.mod_snmp.Snmp.Protocol.SnmpMessageInterface;
import com.mod_snmp.Snmp.Protocol.User;
import com.mod_snmp.Snmp.Protocol.UserException;
import com.mod_snmp.Snmp.Utils.ServiceAddress;
import com.mod_snmp.Snmp.Values.SnmpValueException;
import com.mod_snmp.Snmp.Values.VarbindList;
import net.lisanza.CliArgs.ArgsException;
import net.lisanza.CliArgs.ArgsHandlerInterface;
import net.lisanza.CliArgs.ArgsReader;
import net.lisanza.CliArgs.SubArgsReader;

import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The class that interpretes arguments from the commandline for the Tools.
 * See usage string for all options.
 */
public class Arguments extends ArgsReader
                       implements SnmpMessageInterface,
                                  SecModelNumberInterface {
    private Vector lst = new Vector();
    private static User user;
    private boolean auth = false;
    private boolean priv = false;
    private static ServiceAddress addr;

    /**
     * The SNMP commnand-line argument reader.
     */
    public Arguments(String n)  throws ArgsException {
        addHandler(new ArgsSnmpV1());
        addHandler(new ArgsSnmpV2c());
        addHandler(new ArgsSnmpV3());
    }
    public void parse(String[] args) throws ArgsException {
        int idx = 0;
        try {
            idx = parse_internal(args, idx);
        } catch (ArgsException e) {
            throw new ArgsException(e.getMessage() + usage());
        }
        // Parse now the target address
        if (args.length > idx) {
            try {
                addr = new ServiceAddress(args[idx++]);
            } catch (UnknownHostException e) {
                throw new ArgsException(e.getMessage());
            }
        } else {
            throw new ArgsException("there is no service address");
        }
        idx = parseVarbindList(args, idx);
    }
    private class ArgsSnmpV1 extends SubArgsReader {
        private int version = SNMP_VERSION_1;
        private String community = "";

        public ArgsSnmpV1() throws ArgsException {
            super();
            addHandler(new ArgsCommunity());
        }
        public int parse(String[] args, int idx) throws ArgsException {
            idx++;
            idx = parse_internal(args, idx);
            user = new User(community, version);
            return idx;
        }
        public String argument() {
            return "-v1";
        }
        public String usage() {
            return usage("SNMP version 1:");
        }
        private class ArgsCommunity implements ArgsHandlerInterface {
            public ArgsCommunity() throws ArgsException {
            }
            public int parse(String[] args, int idx) throws ArgsException {
                idx++;
                community = args[idx++];
                return idx;
            }
            public String argument() {
                return "-c";
            }
            public String usage() {
                return "community <string>";
            }
        }
    }
    private class ArgsSnmpV2c extends ArgsSnmpV1 {
        private int version = SNMP_VERSION_2C;
        public ArgsSnmpV2c() throws ArgsException {
            super();
        }
        public String argument() {
            return "-v2c";
        }
        public String usage() {
            return usage("SNMP version 2 community based:");
        }
    }
    private class ArgsSnmpV3 extends SubArgsReader {
        private String username = "";
        private int secmodel = SECMODEL_USM;
        private String auth_protocol = "";
        private String auth_password = "";
        private String priv_protocol = "";
        private String priv_password = "";

        public ArgsSnmpV3() throws ArgsException { 
             super();
             addHandler(new ArgsUsmUser());
             addHandler(new ArgsSecModel());
             addHandler(new ArgsSecLevel());
             addHandler(new ArgsAuthProtocol());
             addHandler(new ArgsPrivProtocol());
        }
        public int parse(String[] args, int idx) throws ArgsException {
            idx++;
            idx = parse_internal(args, idx);
            checkArguments();
            return idx;
        }
        public String argument() {
            return "-v3";
        }
        public String usage() {
            return usage("SNMP v3:");
        }
        /**
         * A check whether all arguments provided are
         * consistent and provided correctly.
         * @throws ArgsException when the check fails.
         */
        public void checkArguments() throws ArgsException {
            if (username.length() == 0) {
                 throw new ArgsException("no username provided");
            }
            try {
                user =  new User(username, SNMP_VERSION_3, secmodel);
                if (auth_protocol.length() != 0) {
                    user.setAuthParameters(auth_protocol, auth_password);
                }
                if (priv_protocol.length() != 0) {
                    user.setPrivParameters(priv_protocol, priv_password);
                }
            } catch (UserException e) {
                throw new ArgsException(e.getMessage());
            }
            if ((auth) && (!user.hasAuthentication())) {
                throw new ArgsException(
                         "security level has authentication, but user has not");
            }
            if ((priv) &&  (!user.hasPrivacy())) {
                throw new ArgsException(
                          "security level has privacy, but user has not");
            }
            if (priv && !auth) {
                throw new ArgsException(
                          "security level has privacy, but no authentication");
            }
        }
        /**
         * The USM user argument handler.
         */
        private class ArgsUsmUser implements ArgsHandlerInterface {
            public ArgsUsmUser() throws ArgsException {
            }
            public int parse(String[] args, int idx) throws ArgsException {
                idx++;
                username = args[idx++];
                return idx;
            }
            public String argument() {
                return "-u";
            }
            public String usage() {
                return "usm user <string>";
            }
        }
        /**
         * The security level handler.
         */
        private class ArgsSecModel implements ArgsHandlerInterface {
            public ArgsSecModel() throws ArgsException {     
            }
            public int parse(String[] args, int idx) throws ArgsException {
                idx++;
                String s_arg = args[idx++];
                if (s_arg.equals("USM")) {
                    secmodel = SECMODEL_USM;
                } else if (s_arg.equals("v1")) {
                    secmodel = SECMODEL_V1;
                } else if (s_arg.equals("v2c")) {
                    secmodel = SECMODEL_V2C;
                } else {
                    ArgsException.invalidValue(s_arg);
                }
                return idx;
            }
            public String argument() {
                return "-m";
            }
            public String usage() {
                return "security model ['USM'|'v1'|'v2c']";
            }
        }
        private class ArgsSecLevel implements ArgsHandlerInterface {
            public ArgsSecLevel() throws ArgsException {
            }
            public int parse(String[] args, int idx) throws ArgsException {
                idx++;
                String s_arg = args[idx++];
                if (s_arg.endsWith("noAuthNoPriv") || s_arg.endsWith("nanp")) {
                    auth = false;
                    priv = false;
                } else if (s_arg.endsWith("authNoPriv") || s_arg.endsWith("anp")) {
                    auth = true;
                    priv = false;
                } else if (s_arg.endsWith("authPriv") || s_arg.endsWith("ap")) {
                    auth = true;
                    priv = true;
                } else {
                    ArgsException.invalidValue(s_arg);
                }
                return idx;
            }
            public String argument() {
                return "-s";
            }
            public String usage() {
                return "security level " +
                    "['noAuthNoPriv'|'nanp'|'authNoPriv'|'anp'|'authPriv'|'ap']";
            }
        }
        private class ArgsAuthProtocol implements ArgsHandlerInterface {
            public ArgsAuthProtocol() throws ArgsException {
            }
            public int parse(String[] args, int idx) throws ArgsException {
                idx++;
                auth_protocol = args[idx];
                if (auth_protocol.equals("MD5") ||
                                auth_protocol.equals("SHA")) { 
                    idx++;
                    auth_password = args[idx++];
                    return idx;
                }
                throw new ArgsException(auth_protocol,
                                "unknown authentication protocol");
            }
            public String argument() {
                return "-a";
            }
            public String usage() {
                return "auhtentication parameters " +
                    "['MD5'|'SHA'] <password>";
            }
        }
        private class ArgsPrivProtocol implements ArgsHandlerInterface {
            public ArgsPrivProtocol() throws ArgsException {
            }
            public int parse(String[] args, int idx) throws ArgsException {
                idx++;
                priv_protocol = args[idx];
                if (priv_protocol.equals("DES")) {
                    idx++;
                    priv_password = args[idx++]; 
                    return idx;
                }
                throw new ArgsException(priv_protocol,
                                "unknown privacy protocol");
            }
            public String argument() {
                return "-p";
            }
            public String usage() {
                return "auhtentication parameters " +
                    "['MD5'|'SHA'] <password>";
            }
        } 
    }
    public int parseVarbindList(String[] args, int idx)
                        throws ArgsException {
        try {
            while (idx < args.length) {
                ManagedInstance mi = new ManagedInstance(args[idx++]);
                 if ((args.length > idx+1) && (args[idx].charAt(0) == '-')) {
                    mi.addValue(args[idx++], args[idx++]);
                }
                lst.add(mi);
                idx++;
            }
        } catch (SnmpValueException e) {
            throw new ArgsException(e.getMessage());
        }
        return idx;
    }
    /**
     * Retrieve the vairable bindings list that was part of the
     * argument list.
     */
    public VarbindList getVarbindList() throws SnmpValueException {
        VarbindList vbl = new VarbindList();
        Enumeration e = lst.elements();
        while (e.hasMoreElements()) {
            ManagedInstance mi = (ManagedInstance)e.nextElement();
            vbl.add(mi.createVarbind());
        }
        return vbl;
    }
    /**
     * Generator for an SNMP manager based on the arguments parsed.
     * @return A configured SnmpManager that still must perform
     * discovery before usage.
     */
    public SnmpManager generateManager()
                         throws SnmpManagerException, SnmpEngineException {
        SnmpManager mgr = new SnmpManager(addr, user);
        if (user.getProtocol() == SNMP_VERSION_3) {
            mgr.setSecurityLevel(auth, priv);
        }
        return mgr;
    }
    public static void main(String args[]) {
        try {
            Arguments a =  new Arguments("SnmpWalk");
            a.parse(args);
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
        }
    }
}

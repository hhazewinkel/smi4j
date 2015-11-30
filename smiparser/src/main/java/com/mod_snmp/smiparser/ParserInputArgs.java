package com.mod_snmp.smiparser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import net.lisanza.CliArgs.ArgsException;
import net.lisanza.CliArgs.ArgsHandlerInterface;
import net.lisanza.CliArgs.ArgsReader;

import java.util.Enumeration;

/**
 * Handler for the CLI options that are added for tasks.
 * This class only reads the input arguments and returns
 * a ParserInput.
 */
public class ParserInputArgs extends ArgsReader {
    private ParserInput config;

    public ParserInputArgs() throws ArgsException {
        super();
        addHandler(new DirectoryLocation());
        addHandler(new MibModules());
        addHandler(new License());

        config = new ParserInput(ParserPreferences.getDefaults());
    }

    private class DirectoryLocation implements ArgsHandlerInterface {
        public DirectoryLocation() throws ArgsException {
        }
        public int parse(String[] args, int idx) throws ArgsException {
            idx++;
            config.setMibDirectory(args[idx++]);
            return idx;
        }
        public String argument() {
            return "-d";
        }
        public String usage() {
            return "location of MIB modules <string>";
        }
    }
    private class MibModules implements ArgsHandlerInterface {
        public MibModules() throws ArgsException {
        }
        public int parse(String[] args, int idx) throws ArgsException {
            idx++;
            while ((idx < args.length) && (args[idx].charAt(0) != '-')) {
                config.addMibModule(args[idx++]);
            }
            return idx;
        }
        public String argument() {
            return "-m";
        }
        public String usage() {
            return "location of MIB modules <string>";
        }
    }
    private class License implements ArgsHandlerInterface {
        public License() throws ArgsException {
        }
        public int parse(String[] args, int idx) throws ArgsException {
            idx++;
            System.out.println("LICENSE can be found at http://www.lisanza.net/SnmpToolBox/LICENSE");
            System.out.println("or look in the COPYING file of this package");

            System.exit(0);
            return idx;
        }
        public String argument() {
            return "-l";
        }
        public String usage() {
            return "license";
        }
    }

    public String getMibDirectory() {
        return config.getMibDirectory();
    }

    public void setMibDirectory(String mibDirectory) {
        config.setMibDirectory(mibDirectory);
    }

    public Enumeration getFiles() {
        return config.getFiles();
    }
}

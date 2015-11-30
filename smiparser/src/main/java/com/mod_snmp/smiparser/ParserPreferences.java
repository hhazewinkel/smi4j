package com.mod_snmp.smiparser;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * the parser configuration.
 * It is based upon a bean oriented configuration.
 */
public class ParserPreferences {
    // The default name of the configuration file
    public static String DEFAULT_CONF_FILENAME = "SmiParserConf.xml";


    public static ParserConf getConf(String fname) throws FileNotFoundException {
        File f = new File(fname);
        XMLDecoder d = new XMLDecoder(new FileInputStream(fname));
        ParserConf config = (ParserConf)d.readObject();
        d.close();
        return config;
    }

    public static ParserConf getDefaults() {
        try {
            return getConf(System.getProperty("user.home") + "/." + DEFAULT_CONF_FILENAME);
        } catch (FileNotFoundException e1) {
            try {
                return getConf("./bean/"  + DEFAULT_CONF_FILENAME);
            } catch (FileNotFoundException e2) {
                System.err.println("Cannot find the parser configuration file");
            }
        }
        return new ParserConf();
    }

    public static void main(String[] args) {
        ParserConf cfg = getDefaults();
        System.out.println("defaults:");
        System.out.println(cfg.toString());
    }
}

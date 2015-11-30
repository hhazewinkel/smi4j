package com.mod_snmp.SmiTools.Generators;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import com.mod_snmp.SmiTools.Generators.Common.GeneratorInterface;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GeneratorConf {
    // The configuration file of the generators
    public static String DEFAULT_CONF_FILENAME =
                "com/mod_snmp/SmiTools/Generators/GeneratorConf.xml";

    public GeneratorConf() {
    }

    private GeneratorInterface[] generators = new GeneratorInterface[0];
    /**
     * Get the generators
     */
    public GeneratorInterface[] getGenerators() {
        return generators;
    }
    /**
     * Set the generators
     */
    public void setGenerators(GeneratorInterface[] garray) {
        generators = garray;
    }


    public static GeneratorConf getConf(String fname) throws FileNotFoundException {
        System.out.println("FILE: " + fname);
        File f = new File(fname);
        XMLDecoder d = new XMLDecoder(new FileInputStream(fname));
        GeneratorConf config = (GeneratorConf)d.readObject();
        d.close();
        return config;
    }
    public static GeneratorConf getDefaults() {
        try {
            return getConf(DEFAULT_CONF_FILENAME);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find the generator configuration file");
        }
        return new GeneratorConf();
    }
}

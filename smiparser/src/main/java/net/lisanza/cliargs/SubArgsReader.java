package net.lisanza.cliargs;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import java.util.Enumeration;
import java.util.Vector;

public class SubArgsReader extends ArgsReader implements ArgsHandlerInterface {
    /**
     * Argument reader constructor.
     * @throws ArgsException when the 'help' argument handler
     * cannot be registered.
     */
    public SubArgsReader() throws ArgsException {
        arguments = new Vector();
    }
    /**
     * The parser function for an array of arguments.
     * @param args The arguments array to be parsed.
     * throws ArgsException when the array of arguments cannot be
     * processed correctly.
     * @param idx The first/current argument to be parsed.
     */
    public int parse(String[] args, int idx)
                        throws ArgsException {
        return parse_internal(args, idx);
    }
    /**
     * The internal parser function for an array of arguments.
     * @param args The arguments array to be parsed.
     * throws ArgsException when the array of arguments cannot be
     * processed correctly.
     * @param idx The first/current argument to be parsed.
     */
    protected int parse_internal(String[] args, int idx)
                        throws ArgsException {
        int first = idx;
        try {
            while (idx < args.length) {
                if (args[idx].charAt(0) == '-') {
                    Enumeration e = arguments.elements();
                    boolean found = false;
                    while ((found == false) && e.hasMoreElements()) {
                        ArgsHandlerInterface h = (ArgsHandlerInterface)
                                                            e.nextElement();
                        if (h.argument().equals(args[idx])) {
                            idx = h.parse(args, idx);
                            found = true;
                        }
                    }
                    if (found == false) {
                        return idx;
                    }
                } else {
                    return idx;
                }
            }
            return idx;
        } catch (ArrayIndexOutOfBoundsException e) {
            ArgsException.missingValue(args[first]);
        }
        return idx;
    }
    /**
     * The argument for this handler class.
     * This is a method that must be always overridden by the clsass extending
     * the SubArgsReader.
     * @return The string value of the first argument that is used for this
     * class.
     */
    public String argument() {
        return "-sub";
    }
    /**
     * The usage of the argument handlers registered.
     * An explaination of the purpose and usage of each handler
     * (if provided by handler).
     * @return A string of the complete usage.
     */
    public String usage() {
        return usage("\nUsage:");
    }
    /**
     * The usage of the argument handlers registered.
     * An explaination of the purpose and usage of each handler
     * (if provided by handler).
     * @return A string of the complete usage.
     */
    public String usage(String msg) {
        Enumeration e = arguments.elements();
         while (e.hasMoreElements()) {
             ArgsHandlerInterface h = (ArgsHandlerInterface)e.nextElement();
             msg += "\n     " + h.argument() + "\t: " + h.usage();
         }
         return msg;
    }
}

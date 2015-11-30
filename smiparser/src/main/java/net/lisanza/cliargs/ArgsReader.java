package net.lisanza.CliArgs;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

import java.util.Enumeration;
import java.util.Vector;

public class ArgsReader implements ArgsReaderInterface {
    protected Vector arguments;

    /**
     * Argument reader constructor.
     * @throws ArgsException when the 'help' argument handler
     * cannot be registered.
     */
    public ArgsReader() throws ArgsException {
        arguments = new Vector();
        addHandler(new Help());
    }
    /**
     * Adding an handler.
     * Adding an ArgsHandlerInterface interface for handling arguments.
     * @param h The to be added argument handler.
     */
    public void addHandler(ArgsHandlerInterface h) throws ArgsException {
        String arg = h.argument();
        if (arg.charAt(0) != '-') {
            ArgsException.invalidRegistration(arg);
        }
        arguments.add(h);
    }
    /**
     * The parser function for an array of arguments.
     * @param args The arguments array to be parsed.
     * throws ArgsException when the array of arguments cannot be
     * processed correctly.
     */
    public void parse(String[] args) throws ArgsException {
        try {
            parse_internal(args, 0);
	} catch (ArgsException e) {
            throw new ArgsException(e.getMessage() + usage());
        }
    }
    /**
     * The internal parser function for an array of arguments.
     * @param args The arguments array to be parsed.
     * throws ArgsException when the array of arguments cannot be
     * processed correctly.
     * @param idx The first/current argument to be parsed.
     */
    protected int parse_internal(String[] args, int idx) throws ArgsException {
        if (args.length == 0) {
            ArgsException.missingArgs();
        }
        while (idx < args.length) {
            try {
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
                        if (args[idx].equals("--")) {
                            return ++idx;
                        }
                        ArgsException.unknownArgument(args[idx]);
                    }
                } else {
                    ArgsException.badArgument(args[idx]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                ArgsException.missingValue(args[idx]);
            }
        }
        if (idx < args.length) {
            ArgsException.notParsed(args, idx);
        }
        return idx;
    }
    /**
     * The usage of the argument handlers registered.
     * An explaination of the purpose and usage of each handler
     * (if provided by handler).
     * @return A string of the complete usage.
     */
    public String usage() {
         String msg = "\nUsage:";
         Enumeration e = arguments.elements();
         while (e.hasMoreElements()) {
             ArgsHandlerInterface h = (ArgsHandlerInterface)e.nextElement();
             msg += "\n" + h.argument() + "\t: " + h.usage();
         }
         return msg;
    }

    /**
     * Internal 'help' class which is always provided.
     * This allows ALWAYS the existance of the '-h' option.
     */
    private class Help implements ArgsHandlerInterface {
        /**
         * The help handler class.
         */
        public Help() {
        }
        /**
         * The parser function when this argument is read.
         * @param args The arguments array to be parsed.
         * @param idx The index of the argument in the args-array on
         * which this parser (class) was executed.
         * @return The value of the index that indicates the first
         * argument in the array after this option.
         */
        public int parse(String[] args, int idx) throws ArgsException {
            throw new ArgsException(args[idx], "Help information");
        }
        /**
         * The argument for this handler class.
         * @return The string value of the first argument that is used for this
         * class.
         */
        public String argument() {
            return "-h";
        }
        /**
         * The usage of this class.
         * An explaination of the purpose and usgae of this class.
         */
        public String usage() {
            return "Help";
        }
    }
}

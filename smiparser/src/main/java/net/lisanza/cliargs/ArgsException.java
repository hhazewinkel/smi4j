package net.lisanza.CliArgs;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * ArgsException which is handler for the CliArgs parser.
 */
public class ArgsException extends Exception {
    /**
     * Exception constructor.
     */
    public ArgsException() {
        super();
    }
    /**
     * Exception consturctor.
     */
    public ArgsException(String s) {
        super(s);
    }
    /**
     * Exception constructor.
     */
    public ArgsException(String a, String m) {
        super(a + ":" + m);
    }
    public static void missingArgs() throws ArgsException {
        throw new ArgsException("no argument provided");
    }
    public static void unknownArgument(String arg) throws ArgsException {
        throw new ArgsException(arg + ": unknown argument provided");
    }
    public static void badArgument(String arg) throws ArgsException {
        throw new ArgsException(arg + ": bad argument provided");
    }
    public static void invalidRegistration(String arg) throws ArgsException {
        throw new ArgsException(arg + ": invalid argument registration");
    }
    public static void invalidValue(String arg) throws ArgsException {
        throw new ArgsException(arg + ": invalid argument value");
    }
    public static void missingValue(String arg) throws ArgsException {
        throw new ArgsException(arg + ": missing value(s)");
    }
    public static void notParsed(String[] args, int idx) throws ArgsException {
        String a = "not parsed arguments: ";
        while (idx <= args.length) {
            a += args[idx++];
        }
        throw new ArgsException(args[idx], a);
    }
}

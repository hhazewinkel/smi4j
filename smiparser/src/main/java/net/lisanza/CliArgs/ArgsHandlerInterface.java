package net.lisanza.CliArgs;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/


/**
 * The generic argument handler interface.
 * This class provides the interface defnition for the
 * classes that can process cli arguments according with the
 * ArgsReader class.
 */
public interface ArgsHandlerInterface {
    /**
     * The parser function when this argument is read.
     * @param args The arguments array to be parsed.
     * @param idx The index of the argument in the args-array on
     * which this parser (class) was executed.
     * @return The value of the index that indicates the first
     * argument in the array after this option.
     */
    public int parse(String[] args, int idx) throws ArgsException;
    /**
     * The argument for this handler class.
     * @return The string value of the first argument that is used for this
     * class.
     */
    public String argument();
    /**
     * The usage of this class.
     * An explaination of the purpose and usgae of this class.
     */
    public String usage();
}

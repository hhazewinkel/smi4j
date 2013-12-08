package net.lisanza.CliArgs;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

public interface ArgsReaderInterface {
    /**
     * The parser function for an array of arguments.
     * @param args The arguments array to be parsed.
     * throws ArgsException when the array of arguments cannot be
     * processed correctly.
     */
    public void parse(String[] args) throws ArgsException;
}

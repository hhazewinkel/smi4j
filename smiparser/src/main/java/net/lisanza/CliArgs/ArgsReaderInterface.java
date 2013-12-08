package net.lisanza.CliArgs;

public interface ArgsReaderInterface {
    /**
     * The parser function for an array of arguments.
     * @param args The arguments array to be parsed.
     * throws ArgsException when the array of arguments cannot be
     * processed correctly.
     */
    public void parse(String[] args) throws ArgsException;
}

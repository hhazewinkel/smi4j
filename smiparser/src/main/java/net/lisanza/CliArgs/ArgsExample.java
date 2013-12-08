package net.lisanza.CliArgs;

/**
 * An example 'application' class.
 * This class (and its main) is an example of the usage of the ArgsReader
 * class that reads command-line arguments.
 * The possible arguments can be registered with an handler of the
 * ArgsHandlerInterface. Each argument (not its values) must start with a
 * '-' sign.
 */
public class ArgsExample extends ArgsReader {

    /**
     * The example 'application' constructor.
     * @throws ArgsException when a  ArgsHandlerInterface
     * cannot be registered.
     */
    public ArgsExample() throws ArgsException {
        super();
        addHandler(new BoolValue());
        addHandler(new MultiValues());
    }

    /**
     * The main method for the application invocation.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            ArgsExample reader = new ArgsExample();
            reader.parse(args);
            System.out.println("arguments parsed succesfully");
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Internal 'example' class for a boolean type value.
     */
    private class BoolValue implements ArgsHandlerInterface {
        /**
         * Boolean type argument handler constructor.
         */
        public BoolValue() {
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
            // Just take one.
            return ++idx;
        }
        /**
         * The argument for this handler class.
         * @return The string value of the first argument that is used for this
         * class.
         */
        public String argument() {
            return "-t";
        }
        /**
         * The usage of this class.
         * An explaination of the purpose and usgae of this class.
         */
        public String usage() {
            return "bool value";
        }
    }

    /**
     * Internal 'example' class for a multi value type value.
     */
    private class MultiValues implements ArgsHandlerInterface {
        /**
         * Multivalue type argument handler constructor.
         */
        public MultiValues() {
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
            // Just take all until next argument (startswidth '-')
            idx++;
            while ((idx < args.length) && (args[idx].charAt(0) != '-')) {
            }
            return idx;
        }
        /** 
         * The argument for this handler class.
         * @return The string value of the first argument that is used for this
         * class.
         */
        public String argument() {
            return "-m";
        }
        /**
         * The usage of this class.
         * An explaination of the purpose and usgae of this class.
         */
        public String usage() {
            return "<..> <..>";
        }
    }
    /**
     * Internal 'example' class for a error-ed type.
     */
    private class TestError implements ArgsHandlerInterface {
        /**
         * Error test class constructor.
         */
        public TestError() {
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
            throw new ArgsException();
        }
        /**
         * The argument for this handler class.
         * @return The string value of the first argument that is used for this
         * class.
         */
        public String argument() {
            return "e";
        }
        /**
         * The usage of this class.
         * An explaination of the purpose and usgae of this class.
         */
        public String usage() {
            return "TestError";
        }
    }

}

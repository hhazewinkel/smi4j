package com.mod_snmp.SmiParser.ErrorHandler;

public interface SmiExceptionLevel {
    /**
     * The SMI exception level.
     */
    public static final int EMERG   = 0;
    public static final int ALERT   = 1;
    public static final int CRIT    = 2;
    public static final int ERROR   = 3;
    public static final int WARNING = 4;
    public static final int NOTICE  = 5;
    public static final int INFO    = 6;
    public static final int DEBUG   = 7;

    /**
     * Internal error, cannot recover.
     */
    public static final int LEVEL0	= 0;
    /**
     * SMI error must be fixed.
     */
    public static final int LEVEL1	= 1;
    /**
     * Error, but tolerated by some applications.
     */
    public static final int LEVEL2	= 2;
    /**
     * Error, but mostly tolerated by applications.
     */
    public static final int LEVEL3	= 3;
    /**
     * OK, but not recommended.
     */
    public static final int LEVEL4	= 4;
    /**
     * OK, but not recommended in some circumstances.
     */
    public static final int LEVEL5	= 5;
    /**
     * notice.
     */
    public static final int LEVEL6	= 6;
    /**
     * debug.
     */
    public static final int LEVEL7	= 7;
}

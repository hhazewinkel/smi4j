package com.mod_snmp.Snmp.Values;
import java.net.InetAddress;

/**
 * SnmpIpAddress.java
 */
public class SnmpIpAddress implements SnmpValue {
    private byte address[];

    /**
     * Constructor that initializes an IpAddress with
     * the address '0.0.0.0'.
     */
    public SnmpIpAddress() {
        address = new byte[4];
    }
    /**
     * Constructor that initializes an IpAddress with
     * the address provided by the addr of type byte[].
     * @throws SnmpValueException when it has an incorrect length
     * which is not equal to 4.
     */
    public SnmpIpAddress(byte[] addr) throws SnmpValueException {
        if (addr.length == 4) {
            address = addr;
        }
        throw new SnmpValueException("Incorrect IpAddress length");
    }
    /**     
     * Constructor that initializes an IpAddress with
     * the address provided by the addr of type byte[].       
     * @throws SnmpValueException when it has an incorrect format
     * which is not having 4 numeric components and each component
     * is between 0 and 255.
     */
    public SnmpIpAddress(String addr) throws SnmpValueException {
        int count = 0;
        int dot = 0;
        while (dot >= 0) {
            dot = addr.indexOf('.', dot+1);
            count++;
        }
        if (count != 3) {
            throw new SnmpValueException("Incorrect IpAddress format");
        }
        address = new byte[4];
        int start = 0;
        int end = addr.indexOf('.', start);
        address[0] = (byte)Long.parseLong(addr.substring(start, dot));
        start = dot+1;
        dot = addr.indexOf('.', start);
        address[1] = (byte)Long.parseLong(addr.substring(start, dot));
        start = dot+1;
        dot = addr.indexOf('.', start);
        address[2] = (byte)Long.parseLong(addr.substring(start, dot));
        start = dot+1;
        dot = addr.length();
        address[3] = (byte)Long.parseLong(addr.substring(start, dot));
    }
    /** 
     * Constructor that initializes an IpAddress with an InetAddress.
     * @throws SnmpValueException when it has an incorrect length.
     */
    public SnmpIpAddress(InetAddress addr) throws SnmpValueException {
        address = addr.getAddress();
        if (address.length > 4) {
            throw new SnmpValueException("IpAddress to large");
        }
    }
    /**
     * Retrieve the IpAddress in byte[] format.
     */
    public byte[] getValue() {
        return address;
    }
    private int value(byte v) {
        return (int)0xff & v;
    }
    /**
     * Retrieve the value as a String.               
     */
    public String toString() {
        return value(address[0]) + "." + value(address[1]) + "." +
                                  value(address[2]) + "." + value(address[3]);
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("        value: IpAddress: " +
                                   value(address[0]) + "." + value(address[1]) + "." +
                                   value(address[2]) + "." + value(address[3])); 
    }    
}

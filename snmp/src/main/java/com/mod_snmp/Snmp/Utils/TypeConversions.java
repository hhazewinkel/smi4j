package com.mod_snmp.Snmp.Utils;

/**
 * This is a class containing all kinds of type conversion
 * methods. All are static.
 */
public class TypeConversions {

    public static int byte2Unsigned(byte b) {
        int result = (0x7F) & b;
        if (b < 0) {
            result += 128;
        }
        return result;
    }
    public static void int2Bytes(byte[] buf, int v, int offset) {
        buf[offset++] = (byte)((v >> 24) & 0xFF);
        buf[offset++] = (byte)((v >> 16) & 0xFF);
        buf[offset++] = (byte)((v >>  8) & 0xFF);
        buf[offset++] = (byte)((v >>  0) & 0xFF);
    }

    public static byte[] string2Bytes(String hexStr) {
        try {
            int hexStrLength = hexStr.length();
            byte[] bytes = new byte[hexStrLength/2];
            hexStr = hexStr.toUpperCase();
            int hexStrPos = 0;
            for (int bytesPos = 0; hexStrPos < hexStrLength; bytesPos++) {
                int nibble1 = Character.digit(hexStr.charAt(hexStrPos++), 16); 
                int nibble2 = Character.digit(hexStr.charAt(hexStrPos++), 16);
                bytes[ bytesPos] = (byte) (nibble1 * 16 + nibble2);
            }
            return bytes;
        } catch (NullPointerException e) {
            return new byte[0];
        }
    }

    private static final char[] HEXVALUES = {'0', '1', '2', '3', '4', '5',
                     '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String bytes2HexString(byte[] array) {
        return bytes2HexString(array, array.length);
    }
    public static String bytes2HexString(byte[] array, int length) {
        try {
            if (array.length < length) {
                length = array.length;
            }
            String str = "length: " +length + " -";
            for (int i = 0; i < length; i++) {
                int nibble1 = (array[i] >> 4) & 0x0F;
                int nibble2 = array[i] & 0x0F;
                str += " " + HEXVALUES[nibble1] + HEXVALUES[nibble2];
            }
            return str;
        } catch (NullPointerException e) {
            return "<null>";
        }
    }
}

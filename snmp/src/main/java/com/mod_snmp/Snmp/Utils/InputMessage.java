package com.mod_snmp.Snmp.Utils;
import java.io.ByteArrayInputStream;

public class InputMessage extends ByteArrayInputStream {

    public InputMessage(byte[] buffer) {
        super(buffer);
    }
    public InputMessage(byte[] buffer, int start, int end) {
        super(buffer, start, end);
    }

    public void clear(int start, int end) {
        for (int i = start; i < end; i++) {
           buf[i] = 0;
        }
    }
    public byte[] toByteArray() {
        return buf;
    }

    public static void main(String[] args) {
        byte[] buffer =  new byte[10];
        for (int i = 0; i < 10 ; i++) {
             buffer[i] = (byte) i;
        }
        InputMessage msg = new InputMessage(buffer);
        System.out.println("Admin : " + msg.count + " " + msg.mark + " " + msg.pos);
        System.out.println("Buffer: " + TypeConversions.bytes2HexString(msg.buf));
        for (int i = 0; i <  5; i++) {
            System.out.println(" Value is " + msg.read());
        }
        System.out.println("Admin : " + msg.count + " " + msg.mark + " " + msg.pos);
        System.out.println("Buffer: " + TypeConversions.bytes2HexString(msg.buf));
        buffer =  msg.toByteArray();
        for (int i = 0; i < buffer.length ; i++) {
            System.out.println(" Value is " + buffer[i]);
        }
        msg.clear(3, 6);
        System.out.println("Admin : " + msg.count + " " + msg.mark + " " + msg.pos);
        System.out.println("Buffer: " + TypeConversions.bytes2HexString(msg.buf));
    }
}

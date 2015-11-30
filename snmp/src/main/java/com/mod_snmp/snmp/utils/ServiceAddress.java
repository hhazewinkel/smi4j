package com.mod_snmp.Snmp.Utils;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServiceAddress {
    private InetAddress address;
    private int port;

    public ServiceAddress(int p) {
       try {
           address = InetAddress.getLocalHost();
           port = p;
       } catch (UnknownHostException e) {
       }
    }
    public ServiceAddress(String str) throws UnknownHostException {
        int colon = str.indexOf(':');
        if (colon > 0) {
            address = InetAddress.getByName(str.substring(0, colon));
            if (colon != str.length()) {
                port = Integer.parseInt(str.substring(colon+1, str.length()));
            } else {
                port = 161;
            }
        } else {
            address = InetAddress.getByName(str);
            port = 161;
        }
    }
    public ServiceAddress(InetAddress addr, int p) {
        address = addr;
        port = p;
    }
    public InetAddress getInetAddress() {
        return address;
    }
    public int getPort() {
        return port;
    }
    public String toString() {
        return address.toString() + ":" + port;
    }
}

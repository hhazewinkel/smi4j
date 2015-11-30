package com.mod_snmp.snmp.protocol;
/* Copyright 2000-2013 Harrie Hazewinkel. All rights reserved.*/

/**
 * The interface with which a security model must be implemented.
 */
public interface SecModelInterface extends BerInterface {
    /**
     * Return the numeric identification of the security model.
     */
    public int getSecModelNumber();
    /**
     * Return an informative string of the security model.
     */
    //public String toString();
    /**
     * Test is the security model matches.
     */
    public boolean isSecModel(int m);
    /**
     * Create the session specific security model parameters.
     * This way an SnmpSession can have its own security data
     * but still share the same model in an SnmpEngine.
     */
    public SecSessDataInterface createSessData();
    /**
     * Decode the security parameters of the SNMP message.
     */
    public void decodeSecurityParameters(BerDecoder raw_packet,
                                            SnmpMessage msg)
                                            throws BerException;
    /**
     * Compare authentication parameters.
     */
    public void decodeAuthentication(BerDecoder raw_packet,  
                                            SnmpMessage msg)
                                            throws SecModelException;
    /**
     * Encode default security parameters of the SNMP message.
     */
    public int encodeSecurityParameters(BerEncoderReverse bre,
                                            SnmpMessage msg)
                                            throws BerException;
    /**
     * Update and encode the new security parameters after the MAC is generated.
     */
    public byte[] encodeAuthentication(BerEncoderReverse bre,
                                            SnmpMessage msg)
                                            throws SecModelException;
    /**
     * Decrypt the scoped PDU.
     */
    public BerDecoder decrypt(BerDecoder raw_packet,
                                            SnmpMessage msg);
    /**
     * Encrypt the scoped PDU.
     */
    public BerEncoderReverse encrypt(BerEncoderReverse bre, SnmpMessage msg);
 
}

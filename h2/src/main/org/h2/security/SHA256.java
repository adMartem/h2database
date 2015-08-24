/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.security;

import java.util.Arrays;

/**
 * This class implements the cryptographic hash function SHA-256.
 */
public class SHA256 {

    /**
     * Calculate the hash code by using the given salt. The salt is appended
     * after the data before the hash code is calculated. After generating the
     * hash code, the data and all internal buffers are filled with zeros to
     * avoid keeping insecure data in memory longer than required (and possibly
     * swapped to disk).
     *
     * @param data the data to hash
     * @param salt the salt to use
     * @return the hash code
     */
    public static byte[] getHashWithSalt(byte[] data, byte[] salt) {
        return null;
    }

    /**
     * Calculate the hash of a password by prepending the user name and a '@'
     * character. Both the user name and the password are encoded to a byte
     * array using UTF-16. After generating the hash code, the password array
     * and all internal buffers are filled with zeros to avoid keeping the plain
     * text password in memory longer than required (and possibly swapped to
     * disk).
     *
     * @param userName the user name
     * @param password the password
     * @return the hash code
     */
    public static byte[] getKeyPasswordHash(String userName, char[] password) {
       return null;
    }

    /**
     * Calculate the hash-based message authentication code.
     *
     * @param key the key
     * @param message the message
     * @return the hash
     */
    public static byte[] getHMAC(byte[] key, byte[] message) {
        return null;
    }

    /**
     * Calculate the hash using the password-based key derivation function 2.
     *
     * @param password the password
     * @param salt the salt
     * @param iterations the number of iterations
     * @param resultLen the number of bytes in the result
     * @return the result
     */
    public static byte[] getPBKDF2(byte[] password, byte[] salt,
            int iterations, int resultLen) {
        return null;
    }

    /**
     * Calculate the hash code for the given data.
     *
     * @param data the data to hash
     * @param nullData if the data should be filled with zeros after calculating
     *            the hash code
     * @return the hash code
     */
    public static byte[] getHash(byte[] data, boolean nullData) {
        return null;
    }

}

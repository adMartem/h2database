/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.h2.api.ErrorCode;
import org.h2.engine.SysProperties;
import org.h2.message.DbException;
import org.h2.store.fs.FileUtils;
import org.h2.util.IOUtils;
import org.h2.util.StringUtils;

/**
 * A factory to create new block cipher objects.
 */
public class CipherFactory {

    /**
     * The default password to use for the .h2.keystore file
     */
    public static final String KEYSTORE_PASSWORD =
            "h2pass";

    /**
     * Get a new block cipher object for the given algorithm.
     *
     * @param algorithm the algorithm
     * @return a new cipher object
     */
    public static BlockCipher getBlockCipher(String algorithm) {
        if ("XTEA".equalsIgnoreCase(algorithm)) {
            return new XTEA();
        } else if ("AES".equalsIgnoreCase(algorithm)) {
            return new AES();
        } else if ("FOG".equalsIgnoreCase(algorithm)) {
            return new Fog();
        }
        throw DbException.get(ErrorCode.UNSUPPORTED_CIPHER, algorithm);
    }

    /**
     * Create a secure client socket that is connected to the given address and
     * port.
     *
     * @param address the address to connect to
     * @param port the port
     * @return the socket
     */
    public static Socket createSocket(InetAddress address, int port)
            throws IOException {
        return null;
    }

/**
     * Create a secure server socket. If a bind address is specified, the socket
     * is only bound to this address.
     *
     * @param port the port to listen on
     * @param bindAddress the address to bind to, or null to bind to all
     *            addresses
     * @return the server socket
     */
    public static ServerSocket createServerSocket(int port,
            InetAddress bindAddress) throws IOException {
        return null;
    }

    /**
     * Get the keystore object using the given password.
     *
     * @param password the keystore password
     * @return the keystore
     */
    public static KeyStore getKeyStore(String password) throws IOException {
        return null;
    }

}

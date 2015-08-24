/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.security;

import org.h2.util.Utils;

/**
 * A pseudo-encryption algorithm that makes the data appear to be
 * encrypted. This algorithm is cryptographically extremely weak, and should
 * only be used to hide data from reading the plain text using a text editor.
 */
public class Fog implements BlockCipher {

	@Override
	public void setKey(byte[] key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void encrypt(byte[] bytes, int off, int len) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decrypt(byte[] bytes, int off, int len) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getKeyLength() {
		// TODO Auto-generated method stub
		return 0;
	}



}

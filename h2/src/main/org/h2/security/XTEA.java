/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.security;

import org.h2.engine.SysProperties;
import org.h2.message.DbException;

/**
 * An implementation of the XTEA block cipher algorithm.
 * <p>
 * This implementation uses 32 rounds.
 * The best attack reported as of 2009 is 36 rounds (Wikipedia).
 */
public class XTEA implements BlockCipher {

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

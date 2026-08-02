/**********************************************************************************************
 Copyright (c) 2010-2021 John M. Bradley and John R. Bradley ("AUTHORS"), All Rights Reserved.

 NOTICE:  All information contained herein is, and remains, the property of AUTHORS,
 their successors, and assigns. The intellectual and technical concepts contained herein
 are proprietary to AUTHORS and may be covered by U.S. and Foreign Patents or patents
 in process or pending, and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material is strictly
 forbidden unless express prior written permission is obtained from AUTHORS.
 Access to and use of the source code contained herein is hereby forbidden to anyone
 except those to whom express, written rights to use this source code have been granted
 by AUTHORS.

 The above copyright notice above does not evidence any actual or intended publication
 or disclosure of this source code, which includes information that is confidential
 and/or proprietary, and is a trade secret, of AUTHORS. ANY REPRODUCTION, MODIFICATION,
 DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE
 CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF AUTHORS IS STRICTLY PROHIBITED, AND IN
 VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION
 OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL
 ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 **********************************************************************************************/
package org.h2.mvstore.p3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Comparator;

import org.h2.mvstore.DataUtils;
import org.h2.mvstore.WriteBuffer;
import org.h2.mvstore.type.BasicDataType;

/**
 * P3 key type: {@code [timestamp:8][ordinal:8][key bytes...]} with char-length wire prefix.
 * Payload comparison is delegated so the p3cobol library can inject {@code CobolComparator}.
 */
public class P3KeyType extends BasicDataType<byte[]> {

    public static final int KEY_OVERHEAD_OFFSET = 0;
    public static final int KEY_TIMESTAMP_OFFSET = KEY_OVERHEAD_OFFSET;
    public static final int KEY_ORDINAL_OFFSET = KEY_TIMESTAMP_OFFSET + (Long.SIZE / 8);
    public static final int KEY_VALUE_OFFSET = KEY_ORDINAL_OFFSET + (Long.SIZE / 8);
    public static final int KEY_OVERHEAD_LENGTH = KEY_VALUE_OFFSET - KEY_OVERHEAD_OFFSET;

    /**
     * Compares key payloads (bytes after the 16-byte overhead).
     * Arguments are the payload slices only (not full keys).
     */
    @FunctionalInterface
    public interface KeyPayloadComparator {
        int compare(byte[] leftPayload, byte[] rightPayload, int keyLength);
    }

    private final KeyPayloadComparator payloadComparator;
    private final int keyLength;

    public P3KeyType() {
        this((KeyPayloadComparator) null, 0);
    }

    public P3KeyType(KeyPayloadComparator payloadComparator, int keyLength) {
        this.payloadComparator = payloadComparator != null
                ? payloadComparator
                : (l, r, len) -> Arrays.compareUnsigned(l, 0, Math.min(l.length, len > 0 ? len : l.length),
                        r, 0, Math.min(r.length, len > 0 ? len : r.length));
        this.keyLength = keyLength;
    }

    /**
     * Convenience for callers that already have a {@link Comparator}{@code <byte[]>}.
     */
    public static P3KeyType withComparator(Comparator<byte[]> comparator, int keyLength) {
        if (comparator == null) {
            return new P3KeyType((KeyPayloadComparator) null, keyLength);
        }
        return new P3KeyType((l, r, len) -> {
            if (len > 0) {
                if (l.length != len) {
                    l = Arrays.copyOf(l, len);
                }
                if (r.length != len) {
                    r = Arrays.copyOf(r, len);
                }
            }
            return comparator.compare(l, r);
        }, keyLength);
    }

    @Override
    public int compare(byte[] l, byte[] r) {
        if (l == null && r == null) {
            return 0;
        } else if (l == null) {
            return -1;
        } else if (r == null) {
            return 1;
        }
        byte[] lBytes = Arrays.copyOfRange(l, KEY_VALUE_OFFSET, l.length);
        byte[] rBytes = Arrays.copyOfRange(r, KEY_VALUE_OFFSET, r.length);
        int c = payloadComparator.compare(lBytes, rBytes, keyLength);
        if (c == 0) {
            return compareUnsigned(l, r, KEY_OVERHEAD_OFFSET, KEY_OVERHEAD_LENGTH);
        }
        return c;
    }

    private static int compareUnsigned(byte[] l, byte[] r, int offset, int length) {
        for (int i = offset; i < length + offset; i++) {
            int lb = l[i] & 0xff;
            int rb = r[i] & 0xff;
            if (lb > rb) {
                return 1;
            } else if (lb < rb) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public int getMemory(byte[] o) {
        if (o == null) {
            return 0;
        }
        return o.length + Character.SIZE / 8;
    }

    @Override
    public byte[] read(ByteBuffer buffer) {
        assert buffer.order() == ByteOrder.BIG_ENDIAN;
        int length = buffer.getChar();
        if (length == Character.MAX_VALUE) {
            return null;
        } else if (length > buffer.remaining()) {
            throw DataUtils.newMVStoreException(DataUtils.ERROR_INTERNAL,
                    "Key too large ({0}) for buffer's remaining {1} bytes",
                    length, buffer.remaining());
        }
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    @Override
    public void write(WriteBuffer buffer, byte[] o) {
        if (o == null) {
            buffer.putChar(Character.MAX_VALUE);
            return;
        }
        if (o.length >= Character.MAX_VALUE) {
            throw DataUtils.newMVStoreException(DataUtils.ERROR_INTERNAL,
                    "Key too long ({0})", o.length);
        }
        buffer.putChar((char) o.length);
        buffer.put(o);
    }

    @Override
    public byte[][] createStorage(int size) {
        return new byte[size][];
    }
}

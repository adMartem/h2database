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

import org.h2.mvstore.WriteBuffer;
import org.h2.mvstore.type.BasicDataType;
import org.h2.mvstore.type.ObjectDataType;

/**
 * Length-prefixed {@code byte[]} value type used by P3 MVCC volumes.
 * Wire format matches the historical {@code P3ValueType} (int length, {@code -1} = null).
 */
public class P3ValueType extends BasicDataType<byte[]> {

    public static final P3ValueType INSTANCE = new P3ValueType();

    @Override
    public int compare(byte[] l, byte[] r) {
        if (l == null && r == null) {
            return 0;
        } else if (l == null) {
            return -1;
        } else if (r == null) {
            return 1;
        }
        if (l == r) {
            return 0;
        }
        return ObjectDataType.compareNotNull(l, r);
    }

    @Override
    public int getMemory(byte[] o) {
        if (o == null) {
            return 0;
        }
        return o.length + Integer.SIZE / 8;
    }

    @Override
    public byte[] read(ByteBuffer buffer) {
        assert buffer.order() == ByteOrder.BIG_ENDIAN;
        int length = buffer.getInt();
        if (length == -1) {
            return null;
        }
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    @Override
    public void write(WriteBuffer buffer, byte[] o) {
        if (o == null) {
            buffer.putInt(-1);
            return;
        }
        buffer.putInt(o.length);
        buffer.put(o);
    }

    @Override
    public byte[][] createStorage(int size) {
        return new byte[size][];
    }
}

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

import org.h2.mvstore.MVStore;
import org.h2.mvstore.P3Builder;
import org.h2.mvstore.tx.TransactionStore;

/**
 * Minimal P3 volume open helpers for the 2.4 baseline (Phase 1a).
 * Full {@code P3Store} / XA / file locks remain in the p3cobol library until later phases.
 */
public final class P3Volume {

    /**
     * P3 logical volume version stamped in {@link #VOLUME_VERSION_MAP}.
     * <ul>
     *   <li>1 – 17.1/25.0-era volumes (H2 MVStore format 1)</li>
     *   <li>2 – H2 2.4 baseline volumes (MVStore format 3), after Phase 2 rewrite</li>
     * </ul>
     */
    public static final int VOLUME_VERSION = 2;

    public static final String VOLUME_VERSION_MAP = "p3.volume_version";

    private P3Volume() {
    }

    /**
     * Open a volume file (created if missing), initialize the transaction store,
     * and stamp {@link #VOLUME_VERSION_MAP} when absent.
     */
    public static Opened open(P3Builder builder, String fileName) {
        if (builder == null) {
            builder = new P3Builder();
        }
        builder.fileName(fileName);
        MVStore store = builder.open();
        if (builder.getRetentionTime() != null) {
            store.setRetentionTime(builder.getRetentionTime());
        }
        TransactionStore txStore = new TransactionStore(store);
        txStore.init();
        stampVolumeVersion(store);
        return new Opened(store, txStore, builder.getCompressionLevel(), builder.isCompactOnClose());
    }

    public static void stampVolumeVersion(MVStore store) {
        if (!store.hasMap(VOLUME_VERSION_MAP)) {
            store.openMap(VOLUME_VERSION_MAP).put(0, VOLUME_VERSION);
            store.commit();
        }
    }

    public static boolean hasP3Files(MVStore store) {
        return store.hasMap(VOLUME_VERSION_MAP);
    }

    public static final class Opened implements AutoCloseable {
        private final MVStore store;
        private final TransactionStore transactionStore;
        private final int compressionLevel;
        private final boolean compactOnClose;

        Opened(MVStore store, TransactionStore transactionStore, int compressionLevel,
                boolean compactOnClose) {
            this.store = store;
            this.transactionStore = transactionStore;
            this.compressionLevel = compressionLevel;
            this.compactOnClose = compactOnClose;
        }

        public MVStore getStore() {
            return store;
        }

        public TransactionStore getTransactionStore() {
            return transactionStore;
        }

        public int getCompressionLevel() {
            return compressionLevel;
        }

        public boolean isCompactOnClose() {
            return compactOnClose;
        }

        @Override
        public void close() {
            transactionStore.close();
            if (compactOnClose && !store.isClosed()) {
                store.close(0);
            } else if (!store.isClosed()) {
                store.close();
            }
        }
    }
}

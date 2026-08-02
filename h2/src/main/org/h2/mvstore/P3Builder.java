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
package org.h2.mvstore;

/**
 * P3/COBOL MVStore builder. Wraps {@link MVStore.Builder} (final in H2 2.4+) and tracks
 * compression level, compact-on-close, and retention time (applied after {@link #open()}
 * via {@link MVStore#setRetentionTime(int)}).
 */
public class P3Builder {

    private final MVStore.Builder delegate = new MVStore.Builder();
    private int compressionLevel = 0;
    private boolean compactOnClose = false;
    private Integer retentionTime = null;

    public P3Builder autoCommitDisabled() {
        delegate.autoCommitDisabled();
        return this;
    }

    public P3Builder autoCommitBufferSize(int kb) {
        delegate.autoCommitBufferSize(kb);
        return this;
    }

    public P3Builder autoCompactFillRate(int percent) {
        delegate.autoCompactFillRate(percent);
        return this;
    }

    public P3Builder fileName(String fileName) {
        delegate.fileName(fileName);
        return this;
    }

    public P3Builder encryptionKey(char[] password) {
        delegate.encryptionKey(password);
        return this;
    }

    public P3Builder readOnly() {
        delegate.readOnly();
        return this;
    }

    public P3Builder keysPerPage(int keyCount) {
        delegate.keysPerPage(keyCount);
        return this;
    }

    public P3Builder recoveryMode() {
        delegate.recoveryMode();
        return this;
    }

    public P3Builder cacheSize(int mb) {
        delegate.cacheSize(mb);
        return this;
    }

    public P3Builder cacheConcurrency(int concurrency) {
        delegate.cacheConcurrency(concurrency);
        return this;
    }

    public P3Builder compress() {
        compressionLevel = 1;
        delegate.compress();
        return this;
    }

    public P3Builder compressHigh() {
        compressionLevel = 2;
        delegate.compressHigh();
        return this;
    }

    public P3Builder pageSplitSize(int pageSplitSize) {
        delegate.pageSplitSize(pageSplitSize);
        return this;
    }

    public P3Builder backgroundExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler) {
        delegate.backgroundExceptionHandler(exceptionHandler);
        return this;
    }

    public P3Builder compactOnClose() {
        compactOnClose = true;
        return this;
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public boolean isCompactOnClose() {
        return compactOnClose;
    }

    public P3Builder retentionTime(int retentionTime) {
        this.retentionTime = retentionTime;
        return this;
    }

    public Integer getRetentionTime() {
        return retentionTime;
    }

    public MVStore open() {
        return delegate.open();
    }

    @Override
    public String toString() {
        return "P3Builder{" + delegate + ", compressionLevel=" + compressionLevel
                + ", compactOnClose=" + compactOnClose + ", retentionTime=" + retentionTime + '}';
    }
}

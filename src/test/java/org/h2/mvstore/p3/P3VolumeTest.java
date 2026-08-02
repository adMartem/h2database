package org.h2.mvstore.p3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.P3Builder;
import org.h2.mvstore.tx.Transaction;
import org.h2.mvstore.tx.TransactionMap;
import org.h2.mvstore.type.ByteArrayDataType;
import org.h2.value.VersionedValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Phase 1a/1c gates: empty volume open and sorted singleWriter append load.
 */
public class P3VolumeTest {

    @TempDir
    Path tempDir;

    @Test
    public void openEmptyVolumeStampsVersion() throws Exception {
        Path volume = tempDir.resolve("empty.mvc");
        P3Builder builder = new P3Builder();
        builder.pageSplitSize(4096);
        builder.cacheSize(4);

        try (P3Volume.Opened opened = P3Volume.open(builder, volume.toString())) {
            assertTrue(Files.exists(volume));
            assertTrue(P3Volume.hasP3Files(opened.getStore()));
            MVMap<Integer, Integer> versionMap = opened.getStore().openMap(P3Volume.VOLUME_VERSION_MAP);
            assertEquals(P3Volume.VOLUME_VERSION, versionMap.get(0).intValue());
        }

        // Re-open existing empty volume
        try (P3Volume.Opened opened = P3Volume.open(new P3Builder(), volume.toString())) {
            assertTrue(P3Volume.hasP3Files(opened.getStore()));
            MVMap<Integer, Integer> versionMap = opened.getStore().openMap(P3Volume.VOLUME_VERSION_MAP);
            assertEquals(P3Volume.VOLUME_VERSION, versionMap.get(0).intValue());
        }
    }

    @Test
    public void openMapWithP3VersionedValueType() throws Exception {
        Path volume = tempDir.resolve("typed.mvc");
        try (P3Volume.Opened opened = P3Volume.open(new P3Builder(), volume.toString())) {
            MVStore store = opened.getStore();
            MVMap.Builder<byte[], VersionedValue<byte[]>> mapBuilder = new MVMap.Builder<>();
            mapBuilder.keyType(ByteArrayDataType.INSTANCE);
            mapBuilder.valueType(new P3VersionedValueType<>());
            MVMap<byte[], VersionedValue<byte[]>> map = store.openMap("p3.smoke", mapBuilder);

            Transaction tx = opened.getTransactionStore().begin();
            TransactionMap<byte[], byte[]> txMap = tx.openMapX(map);
            byte[] key = new byte[] { 1, 2, 3 };
            byte[] value = new byte[] { 9, 8, 7 };
            txMap.put(key, value);
            tx.commit();
            store.commit();

            assertEquals(1, map.sizeAsLong());
        }
    }

    @Test
    public void addCommittedAppendsOnEmptySingleWriterMap() throws Exception {
        Path volume = tempDir.resolve("append.mvc");
        try (P3Volume.Opened opened = P3Volume.open(new P3Builder(), volume.toString())) {
            MVStore store = opened.getStore();
            MVMap.Builder<byte[], VersionedValue<byte[]>> mapBuilder = new MVMap.Builder<>();
            mapBuilder.keyType(ByteArrayDataType.INSTANCE);
            mapBuilder.valueType(new P3VersionedValueType<>());
            mapBuilder.singleWriter();
            MVMap<byte[], VersionedValue<byte[]>> map = store.openMap("p3.append", mapBuilder);
            assertTrue(map.isSingleWriter());

            final int n = 5_000;
            List<MVMap.KeyValue<byte[], byte[]>> entries = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                byte[] key = ByteBuffer.allocate(4).putInt(i).array();
                byte[] value = ByteBuffer.allocate(4).putInt(i ^ 0x5a5a).array();
                entries.add(new MVMap.KeyValue<>() {
                    @Override
                    public byte[] getKey() {
                        return key;
                    }

                    @Override
                    public byte[] getValue() {
                        return value;
                    }
                });
            }

            Transaction tx = opened.getTransactionStore().begin();
            TransactionMap<byte[], byte[]> txMap = tx.openMapX(map);
            txMap.addCommitted(entries);
            tx.commit();
            store.commit();

            assertEquals(n, map.sizeAsLong());

            Transaction readTx = opened.getTransactionStore().begin();
            TransactionMap<byte[], byte[]> readMap = readTx.openMapX(map);
            assertArrayEquals(intKey(0 ^ 0x5a5a), readMap.get(intKey(0)));
            assertArrayEquals(intKey((n - 1) ^ 0x5a5a), readMap.get(intKey(n - 1)));
            assertArrayEquals(intKey(1234 ^ 0x5a5a), readMap.get(intKey(1234)));
            readTx.commit();
        }
    }

    private static byte[] intKey(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }
}

package ru.savulchik.references.soft;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.savulchik.references.soft.impl.SoftReferenceCache;
import ru.savulchik.references.utils.MemoryUtils;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Тест ru.savulchik.references.soft.impl.SoftReferenceCache.
 */
public class SoftReferenceCacheTest {

    @Before
    public void setUp() throws Exception {
        MemoryUtils.freeConsumedMemory();
    }

    @After
    public void tearDown() throws Exception {
        MemoryUtils.freeConsumedMemory();
    }

    @Test
    public void testEntriesAreClearedOnMemoryDemand() throws Exception {
        Cache<Integer, byte[]> cache = new SoftReferenceCache<>();

        final int entriesCount = 8;
        for (int i = 0; i < entriesCount; i++) {
            cache.put(i, new byte[MemoryUtils.MEGABYTE]);
            assertNotNull(cache.get(i));
        }

        try {
            MemoryUtils.consumeAllMemory();
        } catch (OutOfMemoryError ignored) { }

        for (int i = 0; i < entriesCount; i++) {
            assertNull(cache.get(i));
        }
    }
}

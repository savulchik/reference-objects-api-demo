package ru.savulchik.references.soft;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.savulchik.references.utils.MemoryUtils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import static junit.framework.Assert.*;

/**
 * Тест java.lang.ref.SoftReference.
 */
public class SoftReferenceTest {

    @Before
    public void setUp() throws Exception {
        MemoryUtils.freeConsumedMemory();
    }

    @After
    public void tearDown() throws Exception {
        MemoryUtils.freeConsumedMemory();
    }

    @Test
    public void testSoftReferenceIsClearedOnMemoryDemand() throws Exception {
        final int memoryBlockSize = 16 * MemoryUtils.MEGABYTE;

        SoftReference<byte[]> softReference = new SoftReference<>(new byte[memoryBlockSize]);
        assertNotNull(softReference.get());

        try {
            MemoryUtils.consumeAllMemory();
            fail();
        } catch (OutOfMemoryError ignored) { }

        assertNull(softReference.get());
    }

    @Test
    public void testSoftReferenceIsEnqueuedWhenReferentBecomesSoftlyReachable() throws Exception {
        final int memoryBlockSize = 16 * MemoryUtils.MEGABYTE;

        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        SoftReference<byte[]> softReference = new SoftReference<>(new byte[memoryBlockSize], referenceQueue);

        assertFalse(softReference.isEnqueued());
        assertNull(referenceQueue.poll());

        try {
            MemoryUtils.consumeAllMemory();
            fail();
        } catch (OutOfMemoryError ignore) { }

        assertTrue(softReference.isEnqueued());
        assertEquals(softReference, referenceQueue.poll());
    }
}

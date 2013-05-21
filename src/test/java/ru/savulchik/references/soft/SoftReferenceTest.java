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

    /**
     * Мягкая ссылка очищается при нехватке памяти.
     *
     * java -Xms32m -Xmx32m -XX:+PrintGC -XX:+PrintGCTimeStamps
     *
     * 0,641: [GC 24149K->21264K(31424K), 0,0122790 secs]
     * 0,653: [Full GC 21264K->21238K(31424K), 0,0352540 secs]
     * 0,695: [Full GC 28853K->28152K(31424K), 0,0281610 secs]
     * 0,723: [Full GC 28152K->11738K(31424K), 0,0418310 secs]
     * 0,782: [Full GC 28200K->28123K(31424K), 0,0205200 secs]
     * 0,803: [Full GC 28123K->28123K(31424K), 0,0143070 secs]
     *
     * @throws Exception
     */
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

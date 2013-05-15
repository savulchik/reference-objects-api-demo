package ru.savulchik.references.phantom;

import com.google.common.testing.GcFinalization;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import static junit.framework.Assert.*;

/**
 * Тест java.lang.ref.PhantomReference.
 */
public class PhantomReferenceTest {
    @Test
    public void testCantGetReferentOfPhantomReference() throws Exception {
        Object object = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(object, referenceQueue);

        assertNull(phantomReference.get());

        object = null; /*очищаем единственную strong reference на объект*/
        GcFinalization.awaitFullGc();

        assertNull(phantomReference.get());
    }

    @Test
    public void testPhantomReferenceIsEnqueuedWhenReferentBecomesPhantomReachable() throws Exception {
        Object object = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(object, referenceQueue);

        assertFalse(phantomReference.isEnqueued());
        assertNull(referenceQueue.poll());

        object = null; /*очищаем единственную strong reference на объект*/
        GcFinalization.awaitFullGc();

        assertTrue(phantomReference.isEnqueued());
        assertEquals(phantomReference, referenceQueue.poll());
    }
}

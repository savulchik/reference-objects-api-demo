package ru.savulchik.references.weak;

import com.google.common.testing.GcFinalization;
import org.junit.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import static junit.framework.Assert.*;

/**
 * Тест java.lang.ref.WeakReference.
 */
public class WeakReferenceTest {
    @Test
    public void testWeakReferenceIsClearedWhenReferentIsWeaklyReachable() throws Exception {
        Object object = new Object();
        WeakReference<Object> weakReference = new WeakReference<>(object);

        assertNotNull(weakReference.get());

        object = null; /*очищаем единственную strong reference на объект*/
        GcFinalization.awaitFullGc();

        assertNull(weakReference.get());
    }

    @Test
    public void testWeakReferenceIsEnqueuedWhenReferentBecomesWeaklyReachable() throws Exception {
        Object object = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        WeakReference<Object> weakReference = new WeakReference<>(object, referenceQueue);

        assertFalse(weakReference.isEnqueued());
        assertNull(referenceQueue.poll());

        object = null; /*очищаем единственную strong reference на объект*/
        GcFinalization.awaitFullGc();

        assertTrue(weakReference.isEnqueued());
        assertEquals(weakReference, referenceQueue.poll());
    }
}

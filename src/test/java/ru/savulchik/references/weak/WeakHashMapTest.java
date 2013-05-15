package ru.savulchik.references.weak;

import com.google.common.testing.GcFinalization;
import org.junit.Test;

import java.util.WeakHashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Тест java.util.WeakHashMap.
 */
public class WeakHashMapTest {
    @Test
    public void testEntryIsRemovedWhenKeyBecomesWeaklyReachable() throws Exception {
        WeakHashMap<Object, Object> weakHashMap = new WeakHashMap<>();

        assertTrue(weakHashMap.isEmpty());

        Object key = new Object();
        weakHashMap.put(key, new Object());

        assertEquals(1, weakHashMap.size());

        key = null; /*очищаем единственную strong reference на объект*/
        GcFinalization.awaitFullGc();

        assertTrue(weakHashMap.isEmpty());
    }
}

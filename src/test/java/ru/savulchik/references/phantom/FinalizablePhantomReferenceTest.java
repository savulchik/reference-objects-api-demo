package ru.savulchik.references.phantom;

import com.google.common.testing.GcFinalization;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Тест ru.savulchik.references.phantom.FinalizablePhantomReference.
 */
public class FinalizablePhantomReferenceTest {
    @Test
    public void testReferentFinalizationWhenItBecomesPhantomReachable() throws Exception {
        class Result {
            boolean finalizationPerformed;
        }

        Object object = new Object();
        final Result result = new Result();

        FinalizablePhantomReference<Object> reference = new FinalizablePhantomReference<Object>(object) {
            @Override
            protected void finalizeReferent() {
                result.finalizationPerformed = true;
            }
        };

        assertFalse(result.finalizationPerformed);

        object = null;
        GcFinalization.awaitFullGc();

        assertTrue(result.finalizationPerformed);
    }
}

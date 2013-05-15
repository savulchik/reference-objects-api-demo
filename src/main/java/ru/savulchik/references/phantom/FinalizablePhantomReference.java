package ru.savulchik.references.phantom;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * Фантомная ссылка, реализующая финализацию референта.
 */
public abstract class FinalizablePhantomReference<T> extends PhantomReference<T> {

    private static final ReferenceQueue<Object> REFERENCE_QUEUE = new ReferenceQueue<>();

    static {
        new ReferenceHandler().start();
    }

    public FinalizablePhantomReference(T referent) {
        super(referent, REFERENCE_QUEUE);
    }

    /**
     * Метод финализации референта.
     */
    protected abstract void finalizeReferent();

    /**
     * Поток для финализации референтов и очистки фантомных ссылок.
     */
    private static class ReferenceHandler extends Thread {

        private ReferenceHandler() {
            super("FinalizablePhantomReferenceHandler");
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    FinalizablePhantomReference<?> reference =
                            (FinalizablePhantomReference<?>) REFERENCE_QUEUE.remove();
                    reference.finalizeReferent();
                    reference.clear();
                } catch (InterruptedException ignore) { }
            }
        }
    }
}

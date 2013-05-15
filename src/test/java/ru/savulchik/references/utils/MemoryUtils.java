package ru.savulchik.references.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Вспомогательный класс для тестов.
 */
public class MemoryUtils {

    public static final int MEGABYTE = 1024 * 1024; // 1Мб

    private static List<byte[]> data = new LinkedList<>();

    private MemoryUtils() {}

    /**
     * Использовать всю доступную память.
     */
    public static void consumeAllMemory() {
        while (true) {
            data.add(new byte[MEGABYTE]);
        }
    }

    /**
     * Освободить занятую память.
     */
    public static void freeConsumedMemory() {
        data.clear();
    }
}

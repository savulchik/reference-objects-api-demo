package ru.savulchik.references.soft.impl;

import ru.savulchik.references.soft.Cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Простая реализация кэша на основе java.utils.HashMap и java.lang.ref.SoftReference.
 *
 * Сохраняемое в кэше значение оборачивается в мягкую ссылку,
 * которая будет очищена сборщиком мусора при нехватке памяти.
 */
public class SoftReferenceCache<K, V> implements Cache<K, V> {

    private final Map<K, SoftReference<V>> storage = new HashMap<>();

    @Override
    public void put(K key, V value) {
        storage.put(key, new SoftReference<>(value));
    }

    @Override
    public V get(K key) {
        SoftReference<V> softReference = storage.get(key);
        if (softReference == null) {
            return null;
        }

        return softReference.get();
    }
}

package ru.savulchik.references.soft;


/**
 * Интерфейс примитивного кэша.
 */
public interface Cache<K, V> {
    /**
     * Сохранить значение по ключу.
     *
     * @param key ключ
     * @param value значение
     */
    void put(K key, V value);

    /**
     * @param key ключ
     * @return сохраненное значение по ключу или null, если значение отсутствует
     */
    V get(K key);
}

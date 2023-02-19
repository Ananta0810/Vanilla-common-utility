package ananta.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ananta0810
 * This class provides some common methods related to map
 * such as creation, getting, checking,...
 * Most methods can handle NULL input well.
 */
public final class MapEx {

    private MapEx() {
    }

    /**
     * Get the size of a map.
     *
     * @param map can be null.
     * @return 0 if map is null. Otherwise, return its size.
     */
    public static int sizeOf(@Nullable final Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    /**
     * Check if a map is empty or not.
     * @param map can be null.
     * @return true if map is null or is empty. Otherwise, return false.
     */
    public static boolean isEmpty(@Nullable final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    /**
     * Check if a map is not empty.
     * @param map can be null.
     * @return true if map has items. Otherwise, return false.
     */
    public static boolean isNotEmpty(@Nullable final Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    
    /**
     * @return a modifiable empty map.
     */
    @NotNull
    public static <K, V> Map<K, V> emptyMap() {
        return new HashMap<>();
    }
    
    /**
     * @return a modifiable empty linked map.
     */
    @NotNull
    public static <K, V> Map<K, V> emptyLinkedMap() {
        return new LinkedHashMap<>();
    }
    
    /**
     * @return a modifiable empty tree map.
     */
    @NotNull
    public static <K, V> Map<K, V> emptyTreeMap() {
        return new TreeMap<>();
    }
    
    /**
     * @return a modifiable empty list.
     */
    @NotNull
    public static <K, V> Map<K, V> emptyMapIfNull(@Nullable final Map<K, V> map) {
        if (map == null) {
            return emptyMap();
        }
        return map;
    }
    
    /**
     * Get all items of other collections and put it into a map with provided key and value.
     * This method will ignore the occurrences if found.
     * @param collection a collections. Can be null.
     * @param keyProvider how the key will be extracted. Must be not null.
     * @param valueProvider how the value will be extracted. Must be not null.
     * @return empty map if input collection is null or empty. Otherwise, return new map.
     * @throws IllegalArgumentException if keyProvider or valueProvider is null.
     */
    @NotNull
    public static <E, KEY, VALUE> Map<KEY, VALUE> mapOf(@Nullable final Collection<E> collection, @Nullable final Function<E, KEY> keyProvider, @Nullable final Function<E, VALUE> valueProvider) {
        Guardians.checkNull(keyProvider, "Key provider must not be null.");
        Guardians.checkNull(valueProvider, "Value provider must not be null.");

        if (CollectionEx.isEmpty(collection)) {
            return emptyMap();
        }
        return collection.stream().collect(Collectors.toMap(keyProvider, valueProvider, (origin, duplicated) -> origin));
    }
    
    /**
     * Get all items of other collections and put it into a map with provided key.
     * This method will ignore the occurrences if found.
     * @param collection a collections. Can be null.
     * @param keyProvider how the key will be extracted. Must be not null.
     * @return empty map if input collection is null or empty. Otherwise, return new map.
     * @throws IllegalArgumentException if keyProvider is null.
     */
    @NotNull
    public static <K, V> Map<K, V> mapOf(@Nullable final Collection<V> collection, @Nullable final Function<V, K> keyProvider) {
        return mapOf(collection, keyProvider, item -> item);
    }
    
    /**
     * Get all items of other collections and put it into a map with provided key and value.
     * While extract key and value, this method will skip null key or null value.
     * This method will ignore the occurrences if found.
     * @param collection a collections. Can be null.
     * @param keyProvider how the key will be extracted. Must be not null.
     * @param valueProvider how the value will be extracted. Must be not null.
     * @return empty map if input collection is null or empty. Otherwise, return new map with both non-null key and value.
     * @throws IllegalArgumentException if keyProvider or valueProvider is null.
     */
    @NotNull
    public static <E, KEY, VALUE> Map<KEY, VALUE> nonNullMapOf(@Nullable final Collection<E> collection, @Nullable final Function<E, KEY> keyProvider, @Nullable final Function<E, VALUE> valueProvider) {
        Guardians.checkNull(keyProvider, "Key provider must not be null.");
        Guardians.checkNull(valueProvider, "Value provider must not be null.");

        if (CollectionEx.isEmpty(collection)) {
            return emptyMap();
        }
        final Function<E, KEY> memoizedKeyProvider = memoize(keyProvider);
        final Function<E, VALUE> memoizedValueProvider = memoize(valueProvider);
        return collection.stream()
            .filter(item -> memoizedKeyProvider.apply(item) != null)
            .filter(item -> memoizedValueProvider.apply(item) != null)
            .collect(Collectors.toMap(keyProvider, valueProvider, (origin, duplicated) -> origin));
    }
    
    // Visit following link for more information: https://stackoverflow.com/a/49634611
    private static <K, V> Function<K, V> memoize(final Function<K, V> provider) {
        final Map<K, V> map = emptyMap();
        return key -> map.computeIfAbsent(key, provider);
    }
    
    /**
     * Get all items of other collections and put it into a map with provided key and value.
     * While extract key and value, this method will skip null key or null value.
     * This method will ignore the occurrences if found.
     * @param collection a collections. Can be null.
     * @param keyProvider how the key will be extracted. Must be not null.
     * @return empty map if input collection is null or empty. Otherwise, return new map with value.
     * @throws IllegalArgumentException if keyProvider is null.
     */
    @NotNull
    public static <K, V> Map<K, V> nonNullMapOf(@Nullable final Collection<V> collection, @Nullable final Function<V, K> keyProvider) {
        return mapOf(collection, keyProvider, item -> item);
    }
    
}

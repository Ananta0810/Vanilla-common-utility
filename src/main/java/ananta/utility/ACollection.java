package ananta.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: Update class document.
/**
 * @author Ananta0810
 * This class provides some common methods related to collection
 * such as creation, checking,...
 * Most methods can handle NULL input well.
 */
public final class ACollection {
    
    private ACollection() {}
    
    /**
     * @return a modifiable empty list.
     */
    @NotNull
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }
    
    /**
     * @return a modifiable empty set.
     */
    @NotNull
    public static <T> Set<T> emptySet() {
        return new HashSet<>();
    }
    
    /**
     * @return a modifiable empty map.
     */
    @NotNull
    public static <K, V> Map<K, V> emptyMap() {
        return new HashMap<>();
    }
    
    /**
     *
     * @return a modifiable list that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> List<T> listOf(@Nullable final T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    /**
     * Get all items of other collections and put it into a list.
     * @return a modifiable list that contains all items of other collection.
     */
    @Contract("_ -> new")
    @NotNull
    public static <T> List<T> listOf(@Nullable final Collection<T> collection) {
        return isEmpty(collection) ? emptyList() : new ArrayList<>(collection);
    }
    
    /**
     *
     * @return a modifiable list that contains all non-null input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> List<T> listNonNullOf(@Nullable final T... items) {
        return Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toList());
    }
    

    /**
     * Get all non-null items of other collections and put it into a list.
     * @return a modifiable list that contains all non-null items of other collection.
     */
    @Contract("_ -> new")
    @NotNull
    public static <T> List<T> listNonNullOf(@Nullable final Collection<T> collection) {
        return isEmpty(collection)
            ? emptyList()
            : collection.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    /**
     * @return a modifiable set that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> setOf(@Nullable final T... items) {
        return new HashSet<>(Arrays.asList(items));
    }
    
    /**
     * Get all items of other collections and put it into a set.
     * @return a modifiable set that contains all items of other collection.
     */
    @NotNull
    public static <T> Set<T> setOf(@Nullable final Collection<T> collection) {
        return isEmpty(collection) ? emptySet() : new HashSet<>(collection);
    }
    
    /**
     * @return a modifiable set that contains all non-null input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> setNonNullOf(@Nullable final T... items) {
        return Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    
    /**
     * Get all non-null items of other collections and put it into a set.
     * @return a modifiable set that contains all non-null items of other collection.
     */
    @NotNull
    public static <T> Set<T> setNonNullOf(@Nullable final Collection<T> collection) {
        return isEmpty(collection)
            ? emptySet()
            : collection.stream().filter(Objects::nonNull).collect(Collectors.toSet());
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
        if (keyProvider == null) {
            throw new IllegalArgumentException("Key provider must not be null.");
        }
        if (valueProvider == null) {
            throw new IllegalArgumentException("Value provider must not be null.");
        }
        if (isEmpty(collection)) {
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
    public static <E, KEY, VALUE> Map<KEY, VALUE> mapNonNullOf(@Nullable final Collection<E> collection, @Nullable final Function<E, KEY> keyProvider, @Nullable final Function<E, VALUE> valueProvider) {
        if (keyProvider == null) {
            throw new IllegalArgumentException("Key provider must not be null.");
        }
        if (valueProvider == null) {
            throw new IllegalArgumentException("Value provider must not be null.");
        }
        if (isEmpty(collection)) {
            return emptyMap();
        }
        Function<E, KEY> memoizedKeyProvider = memoize(keyProvider);
        Function<E, VALUE> memoizedValueProvider = memoize(valueProvider);
        return collection.stream()
            .filter(item -> memoizedKeyProvider.apply(item) != null)
            .filter(item -> memoizedValueProvider.apply(item) != null)
            .collect(Collectors.toMap(keyProvider, valueProvider, (origin, duplicated) -> origin));
    }
    
    // Visit following link for more information: https://stackoverflow.com/a/49634611
    private static <K, V> Function<K, V> memoize(Function<K, V> provider) {
        Map<K, V> map = emptyMap();
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
    public static <K, V> Map<K, V> mapNonNullOf(@Nullable final Collection<V> collection, @Nullable final Function<V, K> keyProvider) {
        return mapOf(collection, keyProvider, item -> item);
    }
    
    /**
     * @param collection can be null.
     * @return If collection is null then return empty list.
     * If collection is a list, return itself.
     * Otherwise, return list that contains all items of collection
     */
    @NotNull
    public static <T> List<T> emptyListIfNull(@Nullable Collection<T> collection) {
        if (collection == null) {
            return emptyList();
        }
        if (collection instanceof List<?>) {
            return (List<T>) collection;
        }
        return listOf(collection);
    }
    
    /**
     * @param collection can be null.
     * @return If collection is null then return empty set.
     * If collection is a set, return itself.
     * Otherwise, return set that contains all items of collection
     */
    @NotNull
    public static <T> Set<T> emptySetIfNull(@Nullable Collection<T> collection) {
        if (collection == null) {
            return emptySet();
        }
        if (collection instanceof Set<?>) {
            return (Set<T>) collection;
        }
        return setOf(collection);
    }
    
    /**
     * @return a modifiable empty list.
     */
    @NotNull
    public static <K, V> Map<K, V> emptyMapIfNull(@Nullable Map<K, V> map) {
        if (map == null) {
            return emptyMap();
        }
        return map;
    }
    
    /**
     * Get the size of a collection.
     * @param collection can be null.
     * @return 0 if collection is null. Otherwise, return its size.
     */
    public static int sizeOf(@Nullable Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }
    
    /**
     * Check if a collection is empty or not.
     * @param collection can be null.
     * @return true if collection is null or is empty. Otherwise, return false.
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * Check if a collection is not empty.
     * @param collection can be null.
     * @return true if collection has items. Otherwise, return false.
     */
    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * Get first element of a list.
     * @param list can be null.
     * @return empty if list is empty. Otherwise, return optional of first element.
     */
    @NotNull
    public static <T> Optional<T> firstOf(@Nullable List<T> list) {
        if (isEmpty(list)) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(0));
    }
    
    /**
     * Get last element of a list.
     * @param list can be null.
     * @return empty if list is empty. Otherwise, return optional of last element.
     */
    @NotNull
    public static <T> Optional<T> lastOf(@Nullable List<T> list) {
        if (isEmpty(list)) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(list.size() - 1));
    }

}

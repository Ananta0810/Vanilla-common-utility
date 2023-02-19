package ananta.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ananta0810
 * This class provides some common methods related to set
 * such as creation, getting, checking,...
 * Most methods can handle NULL input well and return empty set instead of NULL.
 */
public final class SetEx {

    private SetEx() {
    }

    /**
     * Get the size of a set.
     *
     * @param set can be null.
     * @return 0 if set is null. Otherwise, return its size.
     */
    public static int sizeOf(@Nullable final Set<?> set) {
        return CollectionEx.sizeOf(set);
    }
    
    /**
     * Check if a set is empty or not.
     * @param set can be null.
     * @return true if set is null or is empty. Otherwise, return false.
     */
    public static boolean isEmpty(@Nullable final Set<?> set) {
        return CollectionEx.isEmpty(set);
    }
    
    /**
     * Check if a set is not empty.
     * @param set can be null.
     * @return true if set has items. Otherwise, return false.
     */
    public static boolean isNotEmpty(@Nullable final Set<?> set) {
        return CollectionEx.isNotEmpty(set);
    }
    
    /**
     * @return a modifiable empty set.
     */
    @NotNull
    public static <T> Set<T> emptySet() {
        return new HashSet<>();
    }
    
    /**
     * @return a modifiable empty linked set.
     */
    @NotNull
    public static <T> Set<T> emptyLinkedSet() {
        return new LinkedHashSet<>();
    }
    
    /**
     * @return a modifiable empty tree set.
     */
    @NotNull
    public static <T> Set<T> emptyTreeSet() {
        return new TreeSet<>();
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
        return CollectionEx.isEmpty(collection) ? emptySet() : new HashSet<>(collection);
    }

    /**
     * Get all items of other collections and put it into a set with the extracted key.
     * @return a modifiable set that contains all items of other collection.
     */
    @NotNull
    public static <T, R> Set<R> setOf(@Nullable final Collection<T> collection, final Function<T, R> keyProvider) {
        return setOf(collection)
            .stream()
            .map(item -> Optional.ofNullable(item).map(keyProvider).orElse(null))
            .collect(Collectors.toSet());
    }

    /**
     * @return a modifiable set that contains all non-null input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> nonNullSetOf(@Nullable final T... items) {
        return Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    
    /**
     * Get all non-null items of other collections and put it into a set.
     * @return a modifiable set that contains all non-null items of other collection.
     */
    @NotNull
    public static <T> Set<T> nonNullSetOf(@Nullable final Collection<T> collection) {
        return CollectionEx.isEmpty(collection)
            ? emptySet()
            : collection.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * Get all non-null items of other collections and put it into a set with the extracted key.
     * @return a modifiable set that contains all items of other collection.
     */
    @NotNull
    public static <T, R> Set<R> nonNullSetOf(@Nullable final Collection<T> collection, final Function<T, R> keyProvider) {
        return setOf(collection)
            .stream()
            .filter(Objects::nonNull)
            .map(keyProvider)
            .collect(Collectors.toSet());
    }

    /**
     * @param set can be null.
     * @return If set is null then return empty list.
     * If set is a set, return itself.
     * Otherwise, return set that contains all items of set
     */
    @NotNull
    public static <T> Set<T> emptySetIfNull(@Nullable final Set<T> set) {
        return set == null ? emptySet() : set;
    }

    /**
     * @return a modifiable linked set that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> linkedSetOf(@Nullable final T... items) {
        return new LinkedHashSet<>(Arrays.asList(items));
    }
    
    /**
     * Get all items of other collections and put it into a linked set.
     * @return a modifiable set that contains all items of other collection.
     */
    @NotNull
    public static <T> Set<T> linkedSetOf(@Nullable final Collection<T> collection) {
        return CollectionEx.isEmpty(collection)
            ? emptyLinkedSet()
            : new LinkedHashSet<>(collection);
    }

    /**
     * Get all items of other collections and put it into a linked set with the extracted key.
     * @return a modifiable set that contains all items of other collection.
     */
    @NotNull
    public static <T, R> Set<R> linkedSetOf(@Nullable final Collection<T> collection, final Function<T, R> keyProvider) {
        return setOf(collection)
            .stream()
            .map(item -> Optional.ofNullable(item).map(keyProvider).orElse(null))
            .collect(Collectors.toSet());
    }

    /**
     * @return a modifiable linked set that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> nonNullLinkedSetOf(@Nullable final T... items) {
        return linkedSetOf(ListEx.nonNullListOf(items));
    }
    
    /**
     * Get all items of other collections and put it into a linked set.
     * @return a modifiable set that contains all items of other collection.
     */
    @NotNull
    public static <T> Set<T> nonNullLinkedSetOf(@Nullable final Collection<T> collection) {
        return linkedSetOf(ListEx.nonNullListOf(collection));
    }
    
    /**
     * @return a modifiable tree set that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> treeSetOf(@Nullable final T... items) {
        return new TreeSet<>(Arrays.asList(items));
    }
    
    /**
     * Get all items of other collections and put it into a tree set.
     * @return a modifiable tree set that contains all items of other collection.
     */
    @NotNull
    public static <T> Set<T> treeSetOf(@Nullable final Collection<T> collection) {
        return CollectionEx.isEmpty(collection) ? emptyTreeSet() : new TreeSet<>(collection);
    }
    
    /**
     * @return a modifiable tree set that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> nonNullTreeSetOf(@Nullable final T... items) {
        return treeSetOf(ListEx.nonNullListOf(items));
    }
    
    /**
     * Get all items of other collections and put it into a tree set.
     * @return a modifiable tree set that contains all items of other collection.
     */
    @NotNull
    public static <T> Set<T> nonNullTreeSetOf(@Nullable final Collection<T> collection) {
        return treeSetOf(ListEx.nonNullListOf(collection));
    }
    
}

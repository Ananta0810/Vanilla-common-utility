package ananta.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

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
     * @return a modifiable list that contains input value with no duplicated.
     */
    @SafeVarargs
    @NotNull
    public static <T> List<T> uniqueListOf(@Nullable final T... elements) {
        return uniqueListOf(listOf(elements));
    }
    
    /**
     * Get all unique items of other collections and put it into a list.
     * @return a modifiable list that contains all items of other collection.
     */
    @NotNull
    public static <T> List<T> uniqueListOf(final @Nullable Collection<T> list) {
        return emptyListIfNull(list).stream().distinct().collect(Collectors.toList());
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
     *
     * @return a modifiable list that contains all non-null and unique input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> List<T> uniqueListNonNullOf(@Nullable final T... elements) {
        return uniqueListNonNullOf(listOf(elements));
    }
    
    /**
     * Get all non-null and unique items of other collections and put it into a list.
     * @return a modifiable list that contains all non-null and unique items of other collection.
     */
    @NotNull
    public static <T> List<T> uniqueListNonNullOf(final @Nullable Collection<T> collection) {
        return emptyListIfNull(collection)
            .stream()
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
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
        return isEmpty(collection) ? emptyLinkedSet() : new LinkedHashSet<>(collection);
    }
    
    /**
     * @return a modifiable linked set that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> linkedSetNonNullOf(@Nullable final T... items) {
        return linkedSetOf(listNonNullOf(items));
    }
    
    /**
     * Get all items of other collections and put it into a linked set.
     * @return a modifiable set that contains all items of other collection.
     */
    @NotNull
    public static <T> Set<T> linkedSetNonNullOf(@Nullable final Collection<T> collection) {
        return linkedSetOf(listNonNullOf(collection));
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
        return isEmpty(collection) ? emptyTreeSet() : new TreeSet<>(collection);
    }
    
    /**
     * @return a modifiable tree set that contains all input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> Set<T> treeSetNonNullOf(@Nullable final T... items) {
        return treeSetOf(listNonNullOf(items));
    }
    
    /**
     * Get all items of other collections and put it into a tree set.
     * @return a modifiable tree set that contains all items of other collection.
     */
    @NotNull
    public static <T> Set<T> treeSetNonNullOf(@Nullable final Collection<T> collection) {
        return treeSetOf(listNonNullOf(collection));
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
        AGuard.checkNull(keyProvider, "Key provider must not be null.");
        AGuard.checkNull(valueProvider, "Value provider must not be null.");
        
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
        AGuard.checkNull(keyProvider, "Key provider must not be null.");
        AGuard.checkNull(valueProvider, "Value provider must not be null.");
        
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
    
    /**
     * Move a element in a list to a certain index.
     * If new index is smaller than 0 or larger than the size of list,
     * this method will return the origin list.
     * @param index new index of the move element. Can be null.
     * @param element element that you want to move. Can be null.
     * @param list list contains element you want to move. Can be empty.
     * @return empty list if input list is null.
     * Return input list if element is not found or the index is lower than 0 or larger than size of the list.
     * Otherwise, return the moved list.
     */
    @NotNull
    public static <T> List<T>  moveElementTo(final int index, @Nullable T element, @Nullable List<T> list) {
        if (isEmpty(list) || element == null || index < 0 || index >= sizeOf(list)) {
            return listOf(list);
        }
        List<T> result = list.stream().filter(item_ -> !Objects.equals(item_, element)).collect(Collectors.toList());
        result.add(index, element);
    
        return result;
    }
    
    /**
     * Move a element to the head of a list.
     * @param element element that you want to move. Can be null.
     * @param list a list that contains the element. Can be null.
     * @return empty list if input list is null. Return input list if the list doesn't contains the element.
     * Otherwise, return the list in which element is in the head.
     */
    @NotNull
    public static <T> List<T>  moveElementToHead(@Nullable T element, @Nullable List<T> list) {
        return moveElementTo(0, element, list);
    }
    
    /**
     * Move a element to the tail of a list.
     * @param element element that you want to move. Can be null.
     * @param list a list that contains the element. Can be null.
     * @return empty list if input list is null. Return input list if the list doesn't contains the element.
     * Otherwise, return the list in which element is in the tail.
     */
    @NotNull
    public static <T> List<T>  moveElementToTail(@Nullable T element, @Nullable List<T> list) {
        return moveElementTo(sizeOf(list) - 1, element, list);
    }
    
    /**
     * Concatenate two list into a new list.
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A, A, B, B, null, B, B, C, C, null]
     * </pre>
     * @param head the begin of the new list. Can be null.
     * @param tail the end of the new list. Can be null.
     * @return new merged list of 2 input list.
     */
    @NotNull
    public static <T> List<T> concat(@Nullable List<T> head, @Nullable List<T> tail) {
        if (isEmpty(head)) {
            return listOf(tail);
        }
        if (isEmpty(tail)) {
            return listOf(head);
        }
        List<T> result = listOf(head);
        result.addAll(tail);
        return result;
    }
    
    /**
     * Concatenate two list into a new list. The return list will contains only unique elements.
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A, B, null, C]
     * </pre>
     * @param head the begin of the new list. Can be null.
     * @param tail the end of the new list. Can be null.
     * @return new unique merged list of 2 input list.
     */
    @NotNull
    public static <T> List<T> uniqueConcat(@Nullable List<T> head, @Nullable List<T> tail) {
        if (isEmpty(head)) {
            return uniqueListOf(tail);
        }
        if (isEmpty(tail)) {
            return uniqueListOf(head);
        }
        List<T> result = emptyListIfNull(head);
        result.addAll(tail);
        return uniqueListOf(result);
    }
    
    /**
     * Merge two list into a new list.
     * This only merge those unique items (items which are not existed in the other one).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A, A, B, B, null, C, C]
     * </pre>
     * @param head the begin of the new list. Can be null.
     * @param tail the end of the new list. Can be null.
     * @return new merged list of 2 input list.
     */
    @NotNull
    public static <T> List<T> merge(@Nullable List<T> head, @Nullable List<T> tail) {
        if (isEmpty(head)) {
            return listOf(tail);
        }
        if (isEmpty(tail)) {
            return listOf(head);
        }
        
        List<T> headList = listOf(head);
        Set<T> headSet = setOf(head);
        
        List<T> itemsThatNotExistedInHead = tail.stream().filter(item -> !headSet.contains(item)).collect(Collectors.toList());
        headList.addAll(itemsThatNotExistedInHead);
        
        return headList;
    }
    
    /**
     * Get a list contains all elements existing in both list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [B, B, null, B, B, null]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> innerJoin(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> leftSet = setOf(left);
        Set<T> rightSet = setOf(right);
        
        Predicate<T> elementsInBothList = element -> leftSet.contains(element) && rightSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(elementsInBothList)
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all unique elements existing in both list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [B, null]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> uniqueInnerJoin(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> leftSet = setOf(left);
        Set<T> rightSet = setOf(right);
        
        Predicate<T> elementsInBothList = element -> leftSet.contains(element) && rightSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(elementsInBothList)
            .distinct()
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all elements existing in only the left list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A, A]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> leftExcludeJoin(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> leftSet = setOf(left);
        Set<T> rightSet = setOf(right);
        
        Predicate<T> elementsInLeftOnly = element -> leftSet.contains(element) && !rightSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(elementsInLeftOnly)
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all unique elements existing in only the left list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> uniqueLeftExcludeJoin(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> leftSet = setOf(left);
        Set<T> rightSet = setOf(right);
    
        Predicate<T> elementsInLeftOnly = element -> leftSet.contains(element) && !rightSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(elementsInLeftOnly)
            .distinct()
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all elements existing in only the right list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [C, C]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> rightExcludeJoin(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> leftSet = setOf(left);
        Set<T> rightSet = setOf(right);
        
        Predicate<T> elementsInRightOnly = element -> !leftSet.contains(element) && rightSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(elementsInRightOnly)
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all unique elements existing in only the right list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [C]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> uniqueRightExcludeJoin(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> leftSet = setOf(left);
        Set<T> rightSet = setOf(right);
    
        Predicate<T> elementsInRightOnly = element -> !leftSet.contains(element) && rightSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(elementsInRightOnly)
            .distinct()
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all unique elements existing in only the right list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A, A, C, C]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> different(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> listSet = setOf(left);
        Set<T> otherSet = setOf(right);
        
        Predicate<T> inBothList = element -> listSet.contains(element) && otherSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(not(inBothList))
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all unique elements existing in only the right list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A, C]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> uniqueDifferent(@Nullable List<T> left, @Nullable List<T> right) {
        Set<T> listSet = setOf(left);
        Set<T> otherSet = setOf(right);
        
        Predicate<T> inBothList = element -> listSet.contains(element) && otherSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(not(inBothList))
            .distinct()
            .collect(Collectors.toList());
    }
}

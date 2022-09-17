package ananta.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * @author Ananta0810
 * This class provides some common methods related to list
 * such as creation, getting, checking,...
 * Most methods can handle NULL input well.
 */
public final class AList {
    
    private AList() {}
    
    /**
     * Get the size of a list.
     * @param list can be null.
     * @return 0 if list is null. Otherwise, return its size.
     */
    public static int sizeOf(@Nullable List<?> list) {
        return ACollection.sizeOf(list);
    }
    
    /**
     * Check if a list is empty or not.
     * @param list can be null.
     * @return true if list is null or is empty. Otherwise, return false.
     */
    public static boolean isEmpty(@Nullable List<?> list) {
        return ACollection.isEmpty(list);
    }
    
    /**
     * Check if a list is not empty.
     * @param list can be null.
     * @return true if list has items. Otherwise, return false.
     */
    public static boolean isNotEmpty(@Nullable List<?> list) {
        return ACollection.isNotEmpty(list);
    }
    
    /**
     * @return a modifiable empty list.
     */
    @NotNull
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
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
        return ACollection.isEmpty(collection) ? emptyList() : new ArrayList<>(collection);
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
        return ACollection.isEmpty(collection)
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
        Set<T> headSet = ASet.setOf(head);
        
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
        Set<T> leftSet = ASet.setOf(left);
        Set<T> rightSet = ASet.setOf(right);
        
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
        Set<T> leftSet = ASet.setOf(left);
        Set<T> rightSet = ASet.setOf(right);
        
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
        Set<T> leftSet = ASet.setOf(left);
        Set<T> rightSet = ASet.setOf(right);
        
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
        Set<T> leftSet = ASet.setOf(left);
        Set<T> rightSet = ASet.setOf(right);
        
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
        Set<T> leftSet = ASet.setOf(left);
        Set<T> rightSet = ASet.setOf(right);
        
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
        Set<T> leftSet = ASet.setOf(left);
        Set<T> rightSet = ASet.setOf(right);
        
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
        Set<T> listSet = ASet.setOf(left);
        Set<T> otherSet = ASet.setOf(right);
        
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
        Set<T> listSet = ASet.setOf(left);
        Set<T> otherSet = ASet.setOf(right);
        
        Predicate<T> inBothList = element -> listSet.contains(element) && otherSet.contains(element);
        
        return concat(left, right)
            .stream()
            .filter(not(inBothList))
            .distinct()
            .collect(Collectors.toList());
    }
}

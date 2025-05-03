package ananta.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * @author Ananta0810
 * This class provides some common methods related to list
 * such as creation, getting, checking,...
 * Most methods can handle NULL input well.
 */
public final class MoreList {
    
    private MoreList() {
    }

    /**
     * Get the size of a list.
     *
     * @param list can be null.
     * @return 0 if list is null. Otherwise, return its size.
     */
    public static int sizeOf(@Nullable final List<?> list) {
        return MoreCollection.sizeOf(list);
    }
    
    /**
     * Check if a list is empty or not.
     * @param list can be null.
     * @return true if list is null or is empty. Otherwise, return false.
     */
    public static boolean isEmpty(@Nullable final List<?> list) {
        return MoreCollection.isEmpty(list);
    }
    
    /**
     * Check if a list is not empty.
     * @param list can be null.
     * @return true if list has items. Otherwise, return false.
     */
    public static boolean isNotEmpty(@Nullable final List<?> list) {
        return MoreCollection.isNotEmpty(list);
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
        return MoreCollection.isEmpty(collection) ? emptyList() : new ArrayList<>(collection);
    }

    /**
     * Get all items of other collections and put it into a list with the extracted key.
     * @return a modifiable list that contains all items of other collection.
     */
    @NotNull
    public static <T, R> List<R> listOf(@Nullable final Collection<T> collection, Function<T, R> keyProvider) {
        if (MoreCollection.isEmpty(collection)) {
            return emptyList();
        }
        if (collection instanceof List<?>) {
            return collection
                .stream()
                .map(item -> Optional.ofNullable(item).map(keyProvider).orElse(null))
                .collect(Collectors.toList());
        }
        return listOf(collection)
            .stream()
            .map(item -> Optional.ofNullable(item).map(keyProvider).orElse(null))
            .collect(Collectors.toList());
    }

    /**
     *
     * @return a modifiable list that contains all non-null input value.
     */
    @SafeVarargs
    @NotNull
    public static <T> List<T> nonNullListOf(@Nullable final T... items) {
        return Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    
    /**
     * Get all non-null items of other collections and put it into a list.
     * @return a modifiable list that contains all non-null items of other collection.
     */
    @Contract("_ -> new")
    @NotNull
    public static <T> List<T> nonNullListOf(@Nullable final Collection<T> collection) {
        return MoreCollection.isEmpty(collection)
            ? emptyList()
            : collection.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    /**
     * @param collection can be null.
     * @return If collection is null then return empty list.
     * If collection is a list, return itself.
     * Otherwise, return list that contains all items of collection
     */
    @NotNull
    public static <T> List<T> emptyListIfNull(@Nullable final Collection<T> collection) {
        if (collection == null) {
            return emptyList();
        }
        if (collection instanceof List<?>) {
            return (List<T>) collection;
        }
        return listOf(collection);
    }
    
    /**
     * Find index of the first element that meet the specific condition.
     * @param condition condition to find the element.
     * @param collection list that you want to find element from.
     * @return -1 if no item met the condition. Otherwise, return index of first element that meet the condition.
     */
    public static <T> int firstIndexMatch(final Predicate<T> condition, final List<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return -1;
        }
        
        for (int index = 0; index < collection.size(); index++) {
            if (condition.test(collection.get(index))) {
                return index;
            }
        }
        
        return -1;
    }
    
    /**
     * Find index of the last element that meet the specific condition.
     * @param condition condition to find the element.
     * @param collection list that you want to find element from.
     * @return -1 if no item met the condition. Otherwise, return index of last element that meet the condition.
     */
    public static <T> int lastIndexMatch(final Predicate<T> condition, final List<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return -1;
        }
        
        for (int index = collection.size() - 1; index >= 0; index--) {
            if (condition.test(collection.get(index))) {
                return index;
            }
        }
        
        return -1;
    }

    /**
     * Get element at a certain index.
     * @param index index of the element that you want to get.
     * If the index is less than 0. Then return the element at the end of the list.
     * <pre> Example:
     * - List: [1, 5, 8, 12]
     * - index: 0 => return 1
     * - index: 2 => return 8
     * - index: 5 => return null
     * - index: -1 => return 12
     * - index: -3 => return 1
     * - index: -4 => return null
     * </pre>
     * @param list list that contains the element.
     * @return empty if list is empty or no element at input index. Otherwise, return element.
     */
    public static <T> Optional<T> findElementAt(int index, List<T> list) {
        return Optional.ofNullable(elementAt(index, list));
    }

    /**
     * Get element at a certain index.
     * @param index index of the element that you want to get.
     * If the index is less than 0. Then return the element at the end of the list.
     * <pre> Example:
     * - List: [1, 5, 8, 12]
     * - index: 0 => return 1
     * - index: 2 => return 8
     * - index: 5 => return null
     * - index: -1 => return 12
     * - index: -3 => return 1
     * - index: -4 => return null
     * </pre>
     * @param list list that contains the element.
     * @return null if list is empty or no element at input index. Otherwise, return element.
     */
    public static <T> T elementAt(int index, List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        
        if (index < 0) {
            index = list.size() + index;
        }
        
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get first element of a list.
     * @param list can be null.
     * @return empty if list is empty. Otherwise, return optional of first element.
     */
    @NotNull
    public static <T> Optional<T> findFirstOf(@Nullable final List<T> list) {
        return findElementAt(0, list);
    }
    
    /**
     * Get first element of a list.
     * @param list can be null.
     * @return null if list is empty. Otherwise, return optional of first element.
     */
    public static <T> T firstOf(@Nullable final List<T> list) {
        return elementAt(0, list);
    }
    
    /**
     * Get last element of a list.
     * @param list can be null.
     * @return empty if list is empty. Otherwise, return optional of last element.
     */
    @NotNull
    public static <T> Optional<T> findLastOf(@Nullable final List<T> list) {
        if (isEmpty(list)) {
            return Optional.empty();
        }
        return findElementAt(list.size() - 1, list);
    }
    
    /**
     * Get last element of a list.
     * @param list can be null.
     * @return null if list is empty. Otherwise, return optional of last element.
     */
    public static <T> T lastOf(@Nullable final List<T> list) {
        return elementAt(- 1, list);
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
    public static <T> List<T>  moveElementTo(final int index, @Nullable final T element, @Nullable final List<T> list) {
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
    public static <T> List<T>  moveElementToHead(@Nullable final T  element, @Nullable final List<T> list) {
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
    public static <T> List<T>  moveElementToTail(@Nullable final T element, @Nullable final List<T> list) {
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
    public static <T> List<T> concat(@Nullable final List<T> head, @Nullable final List<T> tail) {
        if (isEmpty(head)) {
            return listOf(tail);
        }
        if (isEmpty(tail)) {
            return listOf(head);
        }
        List<T> result = listOf(head);
        result.addAll(tail);
        return listOf(result);
    }
    
    /**
     * Merge two list into a new list by adding elements which are not existed in the head into the head.
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
    public static <T> List<T> merge(@Nullable final List<T> head, @Nullable final List<T> tail) {
        if (isEmpty(head)) {
            return listOf(tail);
        }
        if (isEmpty(tail)) {
            return listOf(head);
        }

        List<T> headList = listOf(head);
        Set<T> headSet = MoreSet.setOf(head);
        
        List<T> itemsThatNotExistedInHead = tail.stream().filter(item -> !headSet.contains(item)).collect(Collectors.toList());
        headList.addAll(itemsThatNotExistedInHead);
        
        return headList;
    }
    
    /**
     * Get a list contains all elements existing in both list (including null).
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [B, B, null]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> inBothList(@Nullable final List<T> left, @Nullable final List<T> right) {
        Set<T> rightSet = MoreSet.setOf(right);
        
        Predicate<T> elementsThatAlsoInTheRight = rightSet::contains;
        
        return listOf(left)
            .stream()
            .filter(elementsThatAlsoInTheRight)
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
    public static <T> List<T> inLeftListOnly(@Nullable final List<T> left, @Nullable final List<T> right) {
        Set<T> leftSet = MoreSet.setOf(left);
        Set<T> rightSet = MoreSet.setOf(right);

        Predicate<T> elementsInLeftOnly = element -> leftSet.contains(element) && !rightSet.contains(element);

        return concat(left, right)
            .stream()
            .filter(elementsInLeftOnly)
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
    public static <T> List<T> inRightListOnly(@Nullable final List<T> left, @Nullable final List<T> right) {
        Set<T> leftSet = MoreSet.setOf(left);
        Set<T> rightSet = MoreSet.setOf(right);

        Predicate<T> elementsInRightOnly = element -> !leftSet.contains(element) && rightSet.contains(element);

        return concat(left, right)
            .stream()
            .filter(elementsInRightOnly)
            .collect(Collectors.toList());
    }
    
    /**
     * Get a list contains all elements which two list are not shared.
     * <pre> Example:
     * - left: [A, A, B, B, null]
     * - right: [B, B, C, C, null]
     * - return: [A, A, C, C]
     * </pre>
     * @param left first list, can be null.
     * @param right second list, can be null.
     */
    @NotNull
    public static <T> List<T> different(@Nullable final List<T> left, @Nullable final List<T> right) {
        Set<T> listSet = MoreSet.setOf(left);
        Set<T> otherSet = MoreSet.setOf(right);

        Predicate<T> inBothList = element -> listSet.contains(element) && otherSet.contains(element);

        return concat(left, right)
            .stream()
            .filter(not(inBothList))
            .collect(Collectors.toList());
    }
    
}

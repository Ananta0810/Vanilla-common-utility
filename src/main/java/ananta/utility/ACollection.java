package ananta.utility;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ananta0810
 * This class provides some common methods related to collection
 * such as getting, checking,...
 * Most methods can handle NULL input well.
 */
public final class ACollection {
    
    private ACollection() {}
    
    /**
     * Get the size of a collection.
     * @param collection can be null.
     * @return 0 if collection is null. Otherwise, return its size.
     */
    public static int sizeOf(@Nullable final Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }
    
    /**
     * Check if a collection is empty or not.
     * @param collection can be null.
     * @return true if collection is null or is empty. Otherwise, return false.
     */
    public static boolean isEmpty(@Nullable final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * Check if a collection is not empty.
     * @param collection can be null.
     * @return true if collection has items. Otherwise, return false.
     */
    public static boolean isNotEmpty(@Nullable final Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T> boolean hasAnyOf(@Nullable final Collection<T> collection, @Nullable final Collection<T> parentCollection) {
        if (isEmpty(parentCollection) || isEmpty(collection)) {
            return false;
        }
        Set<T> set = ASet.setOf(collection);
        return parentCollection.stream().anyMatch(set::contains);
    }
}

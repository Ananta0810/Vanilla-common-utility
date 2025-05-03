package ananta.utility;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Ananta0810
 * This class provides some common methods related to entry in map.
 */
public class MoreEntry {
    
    private MoreEntry() {}
    
    /**
     * Convert a BiConsumer to Consumer.
     * This method is useful when you want to use BiConsumer in lambda expression like some methods in Stream API
     * such as forEach, collect, etc.
     */
    public static <K, V> Consumer<Map.Entry<K, V>> consume(final BiConsumer<K, V> consumer) {
        return (entry) -> consumer.accept(entry.getKey(), entry.getValue());
    }
    
    /**
     * Convert a BiPredicate to Predicate.
     * This method is useful when you want to use BiPredicate in lambda expression like some methods in Stream API
     * such as anyMatch, allMatch, noneMatch, filter, etc.
     */
    public static <K, V> Predicate<Map.Entry<K, V>> predicate(final BiPredicate<K, V> predicate) {
        return (entry) -> predicate.test(entry.getKey(), entry.getValue());
    }
    
    /**
     * Convert a BiFunction to Function.
     * This method is useful when you want to use BiFunction in lambda expression like some methods in Stream API
     * such as map, flatMap, etc.
     */
    public static <K, V, R> Function<Map.Entry<K, V>, R> function(final BiFunction<K, V, R> function) {
        return (entry) -> function.apply(entry.getKey(), entry.getValue());
    }
    
    /**
     * This method is a short way to convert a stream of Map.Entry to Map.
     */
    public static <S, T> Collector<Map.Entry<S, T>, ?, Map<S, T>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }
    
    
}

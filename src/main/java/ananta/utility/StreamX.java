package ananta.utility;

import ananta.utility.type.Couple;
import ananta.utility.type.CoupleStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
public class StreamX {

    private StreamX() {}

    /**
     * Extract stream from iterator.
     *
     * @param iterator Iterator that you want, can be null.
     * @return Empty stream if iterator is null. Otherwise, return stream of iterator.
     */
    public static <T> Stream<T> from(final @Nullable Iterator<T> iterator) {
        if (iterator == null) {
            return Stream.empty();
        }
        return Stream.generate(() -> null).takeWhile($ -> iterator.hasNext()).map($ -> iterator.next());
    }

    /**
     * Extract stream from collection.
     *
     * @param collection Collection that you want, can be null.
     * @return Empty stream if collection is null. Otherwise, return stream of collection.
     */
    public static <T> Stream<T> from(final @Nullable Collection<T> collection) {
        if (collection == null) {
            return Stream.empty();
        }
        return collection.stream();
    }

    /**
     * <pre>
     * Result example:
     *      - Collection 1: [1, 2, 3]
     *      - Collection 2: ["A", "B", "C"]
     *      - Result:
     *      [
     *          (1, "A"),
     *          (2, "B"),
     *          (3, "C")
     *      ]
     * </pre>
     *
     * @param keyCollection   The collection which each item will be key of the created pair. Can be null.
     * @param valueCollection The collection which each item will be value of the created pair. Can be null.
     * @return A CoupleStream in which each element will be a pair. This return CoupleStream has some methods which use
     * lambda function with 2 parameters.
     */
    public static <T, R> CoupleStream<T, R> zip(final @Nullable Collection<T> keyCollection, @Nullable final Collection<R> valueCollection) {
        return CoupleStream.from(zip(keyCollection, valueCollection, Couple::of));
    }

    /**
     * <pre>
     * Result example:
     *      - Collection 1: [{1, 2}, {3, 4}, {5, 6}]
     *      - Provider: (arr -> arr[1]) => get second item in array.
     *      - Result:
     *      [
     *          ({1, 2}, 2),
     *          ({3, 4}, 4),
     *          ({5, 6}, 6)
     *      ]
     * </pre>
     *
     * @param keyCollection The collection which each item will be key of the created pair. Can be null.
     * @param valueProvider The way we extract value collection which each item will be value of the created pair. Can be null.
     * @return A CoupleStream in which each element will be a pair. This return CoupleStream has some methods which use
     * lambda function with 2 parameters.
     */
    public static <T, R> CoupleStream<T, R> zip(final @Nullable Collection<T> keyCollection, @NotNull final Function<T, R> valueProvider) {
        final List<R> valueCollection = StreamX.from(keyCollection).map(valueProvider).collect(Collectors.toList());
        return zip(keyCollection, valueCollection);
    }

    /**
     * <pre>
     * Result example:
     *      - Key: 5
     *      - ValueCollection: [1, 2, 3, 4, 5]
     *      - Result:
     *      [
     *          (5, 1),
     *          (5, 2),
     *          (5, 3),
     *          (5, 4),
     *          (5, 5),
     *      ]
     * </pre>
     *
     * @param key The value that will be key of all created pairs. Can be null.
     * @param valueCollection The collection which each item will be value of the created pair. Can be null.
     * @return A CoupleStream in which each element will be a pair. This return CoupleStream has some methods which use
     * lambda function with 2 parameters.
     */
    public static <T, R> CoupleStream<T, R> zip(final @Nullable T key, @NotNull final Collection<R> valueCollection) {
        if (key == null) {
            return CoupleStream.empty();
        }
        final List<T> keyCollection = Collections.nCopies(valueCollection.size(), key);

        return zip(keyCollection, valueCollection);
    }

    /**
     * <pre>
     * Result example:
     *      - KeyCollection:
     *      [
     *          {x: 5, y: [1, 2, 3]},
     *          {x: 3, y: [1]},
     *          {x: 2, y: []},
     *      ]
     *      - ValueCollectionProvider: (item -> item.y)
     *      - Result:
     *      [
     *          ({x: 5, y: [1, 2, 3]}, 1),
     *          ({x: 5, y: [1, 2, 3]}, 2),
     *          ({x: 5, y: [1, 2, 3]}, 3),
     *          ({x: 3, y: [1]}, 1),
     *      ]
     * </pre>
     *
     * @param keyCollection The collection which each item will be key of the created pair. Can be null.
     *                      Each key can be the key of many pairs depends on how many values that we can find from each one.
     * @param valueCollectionProvider The way we extract value collection which each item will be value of the created pair. Can be null.
     * @return A CoupleStream in which each element will be a pair. This return CoupleStream has some methods which use
     * lambda function with 2 parameters.
     */
    public static <X, Y> CoupleStream<X, Y> flatZip(final @Nullable Collection<X> keyCollection, @NotNull final Function<X, Collection<Y>> valueCollectionProvider) {
        if (keyCollection == null || keyCollection.isEmpty()) {
            return CoupleStream.empty();
        }
        return CoupleStream.from(flatZip(keyCollection, valueCollectionProvider, Couple::of));
    }

    private static <X, Y, R> Stream<R> flatZip(
        @Nullable final Collection<X> keyCollection,
        @NotNull final Function<X, Collection<Y>> valueCollectionProvider,
        @NotNull final BiFunction<? super X, ? super Y, ? extends R> zipper
    ) {
        final Iterator<X> keyIterator = StreamX.from(keyCollection).iterator();

        final SupportTypes.FlatZipIterator<X, Y> helperIterator = new SupportTypes.FlatZipIterator<>(keyIterator, valueCollectionProvider);
        final Iterator<R> zipIterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return helperIterator.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(helperIterator.getLeft(), helperIterator.getRight());
            }
        };

        final Spliterator<R> zipSpliterator = Spliterators.spliteratorUnknownSize(zipIterator, Spliterator.ORDERED);
        return StreamSupport.stream(zipSpliterator.trySplit(), false);
    }

    /**
     * Click following link for more information: <a href="https://stackoverflow.com/a/23529010/16810646">...</a>
     */
    private static <L, R, T> Stream<T> zip(
        @Nullable final Collection<? extends L> left,
        @Nullable final Collection<? extends R> right,
        @NotNull final BiFunction<? super L, ? super R, ? extends T> zipper
    ) {
        final Spliterator<? extends L> leftSpliterator = StreamX.from(left).spliterator();
        final Spliterator<? extends R> rightSpliterator = StreamX.from(right).spliterator();

        final Iterator<L> leftIterator = Spliterators.iterator(leftSpliterator);
        final Iterator<R> rightIterator = Spliterators.iterator(rightSpliterator);

        final Iterator<T> zipIterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return leftIterator.hasNext() && rightIterator.hasNext();
            }

            @Override
            public T next() {
                return zipper.apply(leftIterator.next(), rightIterator.next());
            }
        };

        // Zipping looses DISTINCT and SORTED characteristics
        final int characteristics =
            leftSpliterator.characteristics() &
                rightSpliterator.characteristics() &
                ~(Spliterator.DISTINCT | Spliterator.SORTED);

        final long zipSize = ((characteristics & Spliterator.SIZED) != 0)
            ? Math.min(leftSpliterator.getExactSizeIfKnown(), rightSpliterator.getExactSizeIfKnown())
            : -1;

        final Spliterator<T> spliterator = Spliterators.spliterator(zipIterator, zipSize, characteristics);
        return StreamSupport.stream(spliterator, false);
    }

}

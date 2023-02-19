package ananta.utility.type;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


@SuppressWarnings("unused")
public class CoupleStream<X, Y> implements Stream<Couple<X, Y>> {

    private final Stream<Couple<X, Y>> delegate;

    private CoupleStream(final Stream<Couple<X, Y>> delegate) {

        this.delegate = delegate;
    }

    public static <X, Y> CoupleStream<X, Y> from(final Stream<Couple<X, Y>> stream) {
        return new CoupleStream<>(stream);
    }

    public static <X, Y> CoupleStream<X, Y> empty() {

        return new CoupleStream<>(Stream.empty());
    }

    public <R> Stream<R> map(final BiFunction<X, Y, R> function) {
        return map(pair -> function.apply(pair.getLeft(), pair.getRight()));
    }

    public Stream<Couple<X, Y>> filter(final BiPredicate<X, Y> predicate) {
        return CoupleStream.from(filter(pair -> predicate.test(pair.getLeft(), pair.getRight())));
    }

    public void forEach(final BiConsumer<X, Y> consumer) {
        forEach(pair -> consumer.accept(pair.getLeft(), pair.getRight()));
    }

    public List<Couple<X, Y>> toList() {
        return delegate.collect(Collectors.toUnmodifiableList());
    }

    public static <T> Builder<T> builder() {
        return Stream.builder();
    }

    public static <T> Stream<T> of(T t) {
        return Stream.of(t);
    }

    public static <T> Stream<T> ofNullable(T t) {
        return Stream.ofNullable(t);
    }

    @SafeVarargs
    public static <T> Stream<T> of(T... values) {
        return Stream.of(values);
    }

    public static <T> Stream<T> iterate(T seed, UnaryOperator<T> f) {
        return Stream.iterate(seed, f);
    }

    public static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next) {
        return Stream.iterate(seed, hasNext, next);
    }

    public static <T> Stream<T> generate(Supplier<? extends T> s) {
        return Stream.generate(s);
    }

    public static <T> Stream<T> concat(@NotNull Stream<? extends T> a, @NotNull Stream<? extends T> b) {
        return Stream.concat(a, b);
    }

    @Override
    public Stream<Couple<X, Y>> filter(Predicate<? super Couple<X, Y>> predicate) {
        return delegate.filter(predicate);
    }

    @Override
    public <R> Stream<R> map(Function<? super Couple<X, Y>, ? extends R> mapper) {
        return delegate.map(mapper);
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super Couple<X, Y>> mapper) {
        return delegate.mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super Couple<X, Y>> mapper) {
        return delegate.mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super Couple<X, Y>> mapper) {
        return delegate.mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super Couple<X, Y>, ? extends Stream<? extends R>> mapper) {
        return delegate.flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(Function<? super Couple<X, Y>, ? extends IntStream> mapper) {
        return delegate.flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super Couple<X, Y>, ? extends LongStream> mapper) {
        return delegate.flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super Couple<X, Y>, ? extends DoubleStream> mapper) {
        return delegate.flatMapToDouble(mapper);
    }

    @Override
    public Stream<Couple<X, Y>> distinct() {
        return delegate.distinct();
    }

    @Override
    public Stream<Couple<X, Y>> sorted() {
        return delegate.sorted();
    }

    @Override
    public Stream<Couple<X, Y>> sorted(Comparator<? super Couple<X, Y>> comparator) {
        return delegate.sorted(comparator);
    }

    @Override
    public Stream<Couple<X, Y>> peek(Consumer<? super Couple<X, Y>> action) {
        return delegate.peek(action);
    }

    @Override
    public Stream<Couple<X, Y>> limit(long maxSize) {
        return delegate.limit(maxSize);
    }

    @Override
    public Stream<Couple<X, Y>> skip(long n) {
        return delegate.skip(n);
    }

    @Override
    public Stream<Couple<X, Y>> takeWhile(Predicate<? super Couple<X, Y>> predicate) {
        return delegate.takeWhile(predicate);
    }

    @Override
    public Stream<Couple<X, Y>> dropWhile(Predicate<? super Couple<X, Y>> predicate) {
        return delegate.dropWhile(predicate);
    }

    @Override
    public void forEach(Consumer<? super Couple<X, Y>> action) {
        delegate.forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super Couple<X, Y>> action) {
        delegate.forEachOrdered(action);
    }

    @Override
    public Object @NotNull [] toArray() {
        return delegate.toArray();
    }

    @Override
    public <A> A @NotNull [] toArray(IntFunction<A[]> generator) {
        return delegate.toArray(generator);
    }

    @Override
    public Couple<X, Y> reduce(Couple<X, Y> identity, BinaryOperator<Couple<X, Y>> accumulator) {
        return delegate.reduce(identity, accumulator);
    }

    @Override
    public Optional<Couple<X, Y>> reduce(BinaryOperator<Couple<X, Y>> accumulator) {
        return delegate.reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super Couple<X, Y>, U> accumulator, BinaryOperator<U> combiner) {
        return delegate.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Couple<X, Y>> accumulator, BiConsumer<R, R> combiner) {
        return delegate.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super Couple<X, Y>, A, R> collector) {
        return delegate.collect(collector);
    }

    @Override
    public Optional<Couple<X, Y>> min(Comparator<? super Couple<X, Y>> comparator) {
        return delegate.min(comparator);
    }

    @Override
    public Optional<Couple<X, Y>> max(Comparator<? super Couple<X, Y>> comparator) {
        return delegate.max(comparator);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public boolean anyMatch(Predicate<? super Couple<X, Y>> predicate) {
        return delegate.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super Couple<X, Y>> predicate) {
        return delegate.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super Couple<X, Y>> predicate) {
        return delegate.noneMatch(predicate);
    }

    @Override
    public Optional<Couple<X, Y>> findFirst() {
        return delegate.findFirst();
    }

    @Override
    public Optional<Couple<X, Y>> findAny() {
        return delegate.findAny();
    }

    @Override
    public Iterator<Couple<X, Y>> iterator() {
        return delegate.iterator();
    }

    @Override
    public Spliterator<Couple<X, Y>> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public boolean isParallel() {
        return delegate.isParallel();
    }

    @Override
    public Stream<Couple<X, Y>> sequential() {
        return delegate.sequential();
    }

    @Override
    public Stream<Couple<X, Y>> parallel() {
        return delegate.parallel();
    }

    @Override
    public Stream<Couple<X, Y>> unordered() {
        return delegate.unordered();
    }

    @Override
    public Stream<Couple<X, Y>> onClose(Runnable closeHandler) {
        return delegate.onClose(closeHandler);
    }

    @Override
    public void close() {
        delegate.close();
    }
}

package ananta.utility.lambdas;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class MorePredicates<T> {

    protected final boolean negate;
    protected final Function<T, T> mapper;

    protected MorePredicates(final boolean negate, final Function<T, T> mapper) {
        this.negate = negate;
        this.mapper = mapper;
    }

    protected Predicate<String> valueOf(@NotNull final Predicate<String> predicate) {
        Objects.requireNonNull(predicate, "Predicate should not be null");
        return this.negate ? predicate.negate() : predicate;
    }

    protected T map(final T input) {
        return mapper.apply(input);
    }

    public abstract <Y extends MorePredicates<T>> Y cloneWithMapper(Function<T, T> mapper);
}


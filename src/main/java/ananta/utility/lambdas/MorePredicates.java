package ananta.utility.lambdas;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

class MorePredicates<T> {

    protected final boolean negate;

    protected MorePredicates(final boolean negate) {
        this.negate = negate;
    }

    protected Predicate<String> valueOf(@NotNull final Predicate<String> predicate) {
        Objects.requireNonNull(predicate, "Predicate should not be null");
        return this.negate ? predicate.negate() : predicate;
    }

}


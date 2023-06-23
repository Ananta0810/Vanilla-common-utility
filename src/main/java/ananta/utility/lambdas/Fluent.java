package ananta.utility.lambdas;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Fluent<T, PREDICATE extends MorePredicates<T>> {

    private final T target;
    private final FluentPredicate<T, PREDICATE> fluentPredicate;

    public Fluent(final T target, final FluentPredicate<T, PREDICATE> fluentPredicate) {
        this.target = target;
        this.fluentPredicate = fluentPredicate;
    }

    public boolean as(@NotNull final Function<FluentPredicate<T, PREDICATE>, Predicate<T>> function) {
        Objects.requireNonNull(function, "Function should not be null.");
        return function.apply(fluentPredicate).test(target);
    }
}

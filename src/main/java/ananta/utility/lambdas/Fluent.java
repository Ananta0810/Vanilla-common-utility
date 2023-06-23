package ananta.utility.lambdas;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Fluent<T, PREDICATE extends MorePredicates<T>> {

    private final T target;
    private final FluentStatement<T, PREDICATE> fluentStatement;

    public Fluent(final T target, final FluentStatement<T, PREDICATE> fluentStatement) {
        this.target = target;
        this.fluentStatement = fluentStatement;
    }

    public boolean test(@NotNull final Function<FluentStatement<T, PREDICATE>, Predicate<T>> function) {
        Objects.requireNonNull(function, "Function should not be null.");
        return function.apply(fluentStatement).test(target);
    }

    public static Fluent<String, StringMorePredicates> of(@Nullable final String target) {
        return new Fluent<>(target, StringMorePredicates.INSTANCES);
    }
}

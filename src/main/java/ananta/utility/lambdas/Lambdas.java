package ananta.utility.lambdas;

import ananta.utility.Guardians;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Lambdas {
    private Lambdas() {
    }

    public static <T> boolean match(@Nullable final T target, @NotNull final Predicate<T> predicate) {
        Guardians.checkNull(predicate, "Predicate should not be null.");
        return predicate.test(target);
    }

    public static <T> Predicate<T> of(@NotNull final Predicate<T> predicate){
        Guardians.checkNull(predicate, "Predicate should not be null.");
        return predicate;
    }

    public static <T> Consumer<T> of(@NotNull final Consumer<T> consumer){
        Guardians.checkNull(consumer, "Consumer should not be null.");
        return consumer;
    }

    public static <T> Supplier<T> of(@NotNull final Supplier<T> supplier){
        Guardians.checkNull(supplier, "Supplier should not be null.");
        return supplier;
    }

    public static <T, R> Function<T, R> of(@NotNull final Function<T, R> function){
        Guardians.checkNull(function, "Function should not be null.");
        return function;
    }
}

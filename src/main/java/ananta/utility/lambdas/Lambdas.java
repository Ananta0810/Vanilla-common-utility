package ananta.utility.lambdas;

import ananta.utility.ExNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Lambdas {
    private Lambdas() {
    }

    public static <T> boolean test(@Nullable final T target, @NotNull final Predicate<T> predicate) {
        ExNull.checkNull(predicate, "Predicate should not be null.");
        return predicate.test(target);
    }

    public static <T> Predicate<T> whether(@NotNull final Predicate<T> predicate){
        ExNull.checkNull(predicate, "Predicate should not be null.");
        return predicate;
    }

    public static <T> Predicate<T> predicate(@NotNull final Predicate<T> predicate){
        ExNull.checkNull(predicate, "Predicate should not be null.");
        return predicate;
    }

    public static <T> Consumer<T> consumer(@NotNull final Consumer<T> consumer){
        ExNull.checkNull(consumer, "Consumer should not be null.");
        return consumer;
    }

    public static <T> Supplier<T> supplier(@NotNull final Supplier<T> supplier){
        ExNull.checkNull(supplier, "Supplier should not be null.");
        return supplier;
    }

    public static <T, R> Function<T, R> function(@NotNull final Function<T, R> function){
        ExNull.checkNull(function, "Function should not be null.");
        return function;
    }

}

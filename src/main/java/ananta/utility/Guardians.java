package ananta.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Ananta0810
 * This class provides some methods to check the input
 * such as check null, check value of number... and throw
 * exceptions if the check failed.
 * Most methods can handle NULL input well.
 * Method name convention:
 * - Starting with 'check': Method will check input and throw exception.
 * - Starting with 'is': Method will check input and return boolean value.
 */
public final class Guardians {

    private Guardians() {
    }

    public static Object checkNull(@Nullable final Object object, @Nullable final String message, @Nullable final Object... args) {
        if (object == null) {
            throw new IllegalArgumentException(StringX.format(message, args));
        }
        return object;
    }

    @Contract("null -> fail")
    public static Object checkNull(@Nullable final Object object) {
        checkNull(object, "Value must be not null.");
        return object;
    }

    public static boolean isAnyNull(@Nullable final Object... objects) {
        return ListX.listOf(objects).stream().anyMatch(Objects::isNull);
    }

    public static boolean isAllNull(@Nullable final Object... objects) {
        return ListX.listOf(objects).stream().allMatch(Objects::isNull);
    }

    public static boolean isNoneNull(@Nullable final Object... objects) {
        return ListX.listOf(objects).stream().noneMatch(Objects::isNull);
    }

}

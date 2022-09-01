package ananta.utility;

import org.jetbrains.annotations.Nullable;

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
public final class AGuard {
    
    private AGuard() {}
    
    public static void checkNull(@Nullable final Object object, @Nullable final String message, @Nullable final Object... args) {
        if (object == null) {
            throw new IllegalArgumentException(AString.format(message, args));
        }
    }
    
    public static void checkNull(@Nullable final Object object) {
        checkNull(object, "Value must be not null.");
    }
}

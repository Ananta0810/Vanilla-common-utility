package ananta.utility.lambdas;

import ananta.utility.StringX;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class StringPredicates extends MorePredicates {

    private static final StringPredicates IS_INSTANCE = new StringPredicates(false);
    private static final StringPredicates IS_NOT_INSTANCE = new StringPredicates(true);

    StringPredicates(final boolean negate) {
        super(negate);
    }

    public static StringPredicates is() {
        return IS_INSTANCE;
    }

    public static StringPredicates isNot() {
        return IS_NOT_INSTANCE;
    }

    public Predicate<String> empty() {
        return valueOf(StringX::isEmpty);
    }

    public Predicate<String> nullValue() {
        return valueOf(Objects::isNull);
    }

    public Predicate<String> blank() {
        return valueOf(StringX::isBlank);
    }

    public Predicate<String> hasLength(@Nullable final Number length) {
        return valueOf((input) -> input != null && length != null && input.length() == length.intValue());
    }

    public Predicate<String> longerThan(@Nullable final Number length) {
        return valueOf((input) -> input != null && length != null && input.length() > length.intValue());
    }

    public Predicate<String> shorterThan(@Nullable final Number length) {
        return valueOf((input) -> input != null && length != null && input.length() < length.intValue());
    }

    public Predicate<String> equalsTo(@Nullable final String value) {
        return valueOf((input) -> StringX.isEquals(value, input));
    }

    public Predicate<String> equalsIgnoreCaseTo(@Nullable final String value) {
        return valueOf((input) -> value != null && value.equalsIgnoreCase(input));
    }

    public Predicate<String> has(@Nullable final String value) {
        return valueOf((input) -> input != null && value != null && input.contains(value));
    }

    public Predicate<String> in(@Nullable final String value) {
        return valueOf((input) -> value != null && input != null && value.contains(input));
    }

    public Predicate<String> startingWith(@Nullable final String value) {
        return valueOf((input) -> input != null && value != null && input.startsWith(value));
    }

    public Predicate<String> endingWith(@Nullable final String value) {
        return valueOf((input) -> input != null && value != null && input.endsWith(value));
    }

    public static Predicate<String> matching(@Nullable final String regex) {
        return input -> input != null && regex != null && input.matches(regex);
    }

    public static Predicate<String> matching(@Nullable final Pattern regex) {
        return input -> input != null && regex != null && regex.matcher(input).matches();
    }

    public Predicate<String> allCharactersMatch(@NotNull final Predicate<Character> predicate) {
        return input -> StringX.allCharactersMatch(predicate, input);
    }

    public Predicate<String> anyCharactersMatch(@NotNull final Predicate<Character> predicate) {
        return input -> StringX.anyCharactersMatch(predicate, input);
    }

    public Predicate<String> nonCharactersMatch(@NotNull final Predicate<Character> predicate) {
        return input -> StringX.noCharactersMatch(predicate, input);
    }

}
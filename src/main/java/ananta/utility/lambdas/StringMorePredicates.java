package ananta.utility.lambdas;

import ananta.utility.StringX;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class StringMorePredicates extends MorePredicates<String> {

    public static final FluentStatement<String, StringMorePredicates> INSTANCES = new FluentStatement<>(new StringMorePredicates(false), new StringMorePredicates(true));

    StringMorePredicates(final boolean negate) {
        super(negate, value -> value);
    }

    StringMorePredicates(final boolean negate, @NotNull final Function<String, String> function) {
        super(negate, Objects.requireNonNull(function, "Function should not be null"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Y extends MorePredicates<String>> Y cloneWithMapper(@NotNull final Function<String, String> mapper) {
        return (Y) new StringMorePredicates(this.negate, mapper);
    }

    public Predicate<String> empty() {
        return valueOf(input -> StringX.isEmpty(map(input)));
    }

    public Predicate<String> nullValue() {
        return valueOf(input -> Objects.isNull(map(input)));
    }

    public Predicate<String> blank() {
        return valueOf(input -> StringX.isBlank(map(input)));
    }

    public Predicate<String> hasLength(@Nullable final Number length) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && length != null && mappedInput.length() == length.intValue();
        });
    }

    public Predicate<String> hasLengthMoreThan(@Nullable final Number length) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && length != null && mappedInput.length() > length.intValue();
        });
    }

    public Predicate<String> hasLengthLessThan(@Nullable final Number length) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && length != null && mappedInput.length() < length.intValue();
        });
    }

    public Predicate<String> equalsTo(@Nullable final String value) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return StringX.isEquals(value, mappedInput);
        });
    }

    public Predicate<String> equalsIgnoreCaseTo(@Nullable final String value) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && mappedInput.equalsIgnoreCase(value);
        });
    }

    public Predicate<String> containing(@Nullable final String value) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && value != null && mappedInput.contains(value);
        });
    }

    public Predicate<String> in(@Nullable final String value) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && value != null && value.contains(input);
        });
    }

    public Predicate<String> startingWith(@Nullable final String value) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && value != null && mappedInput.startsWith(value);
        });
    }

    public Predicate<String> endingWith(@Nullable final String value) {
        return valueOf((input) -> {
            final String mappedInput = map(input);
            return mappedInput != null && value != null && mappedInput.endsWith(value);
        });
    }

    public Predicate<String> matching(@Nullable final String regex) {
        return valueOf(input -> {
            final String mappedInput = map(input);
            return mappedInput != null && regex != null && mappedInput.matches(regex);
        });
    }

    public Predicate<String> matching(@Nullable final Pattern regex) {
        return valueOf(input -> {
            final String mappedInput = map(input);
            return mappedInput != null && regex != null && regex.matcher(mappedInput).matches();
        });
    }

    public Predicate<String> allCharactersMatch(@NotNull final Predicate<Character> predicate) {
        return valueOf(input -> {
            final String mappedInput = map(input);
            return StringX.allCharactersMatch(predicate, mappedInput);
        });
    }

    public Predicate<String> anyCharactersMatch(@NotNull final Predicate<Character> predicate) {
        return valueOf(input -> {
            final String mappedInput = map(input);
            return StringX.anyCharactersMatch(predicate, mappedInput);
        });
    }

    public Predicate<String> nonCharactersMatch(@NotNull final Predicate<Character> predicate) {
        return valueOf(input -> {
            final String mappedInput = map(input);
            return StringX.noCharactersMatch(predicate, mappedInput);
        });
    }



}
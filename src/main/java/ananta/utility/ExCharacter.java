package ananta.utility;

import ananta.utility.lambdas.Fluent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ananta.utility.lambdas.Lambdas.whether;

public final class ExCharacter {

    private static final Set<Character> SPECIAL_CHARACTERS = Arrays
        .stream("!#$%&'()*+,-./:;<=>?@[]^_`{|}~".split(""))
        .map(c -> c.charAt(0))
        .collect(Collectors.toSet());

    private ExCharacter() {}

    public static Predicate<Character> isDigit() {
        return character -> character != null && Character.isDigit(character);
    }

    public static Predicate<Character> isLetter() {
        return character -> character != null && Character.isLetter(character);
    }

    public static Predicate<Character> isSpace() {
        return character -> character != null && Character.isSpaceChar(character);
    }

    public static Predicate<Character> isSpecial() {
        return (character) -> character != null && SPECIAL_CHARACTERS.contains(character);
    }

    public static void main(final String[] args) {
        final String text = "Hello";
        final boolean hello = Fluent
            .of(text)
            .test(value -> whether(value.isNot.blank())
                .and(value.is.containing("H"))
                .and(value.is.allCharactersMatch(isLetter().or(isSpace())))
                .and(value.map(toLowerCase()).is.containing("h"))
                .and(value.map(toUpperCase()).is.equalsTo("HELLO"))
            );
        System.out.println(hello);
    }
    @NotNull
    private static Function<String, String> toUpperCase() {
        return String::toUpperCase;
    }
    @NotNull
    private static Function<String, String> toLowerCase() {
        return String::toLowerCase;
    }

}

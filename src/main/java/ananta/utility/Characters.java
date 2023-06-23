package ananta.utility;

import ananta.utility.lambdas.Lambdas;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Characters {

    private static final Set<Character> SPECIAL_CHARACTERS = Arrays
        .stream("!#$%&'()*+,-./:;<=>?@[]^_`{|}~".split(""))
        .map(c -> c.charAt(0))
        .collect(Collectors.toSet());

    private Characters() {}

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
        final boolean hello = Lambdas.test(text).as(value -> value.isNot.blank().and(value.is.containing("h")));
        System.out.println(hello);
    }
}

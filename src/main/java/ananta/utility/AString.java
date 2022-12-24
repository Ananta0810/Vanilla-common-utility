package ananta.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Ananta0810
 * This class provides some methods related to type String.
 * Instead of returning NULL, methods will return a empty String.
 * Most methods can handle NULL input well.
 */
public final class AString {

    private final static String EMPTY = "";
    private static final StringBuilder STRING_BUILDER = new StringBuilder();

    private AString() {}
    
    /**
     * Check whether a string is empty or not.
     * @param value can be null.
     * @return true if input is null or is empty. Otherwise, return false.
     */
    public static boolean isEmpty(@Nullable final String value) {
        return value == null || value.isEmpty();
    }
    
    /**
     * Check whether a string has characters aside from empty space.
     * @param value can be null.
     * @return true if input contains any other character aside from empty space.
     */
    public static boolean isNotEmpty(@Nullable final String value) {
        return !isEmpty(value);
    }
    
    /**
     * Check whether a string is blank or not.
     * @param value can be null.
     * @return true if input is null or is blank. Otherwise, return false.
     */
    public static boolean isBlank(@Nullable final String value) {
        return value == null || value.isBlank();
    }
    
    /**
     * Check whether a string has characters aside from blank space.
     * @param value can be null.
     * @return true if input contains any other character aside from blank space.
     */
    public static boolean isNotBlank(@Nullable final String value) {
        return !isBlank(value);
    }

    @NotNull
    public static String emptyIfNull(@Nullable String string) {
        return string == null ? EMPTY : string;
    }

    /**
     * Check if a string contains only digit (0 -> 9).
     * This method will consider space and empty as non-digit character.
     * @param value can be null.
     * @return false if input is empty or has any non-digit character (Include space).
     */
    public static boolean hasDigitOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return false;
        }
        return value.chars().allMatch(Character::isDigit);
    }
    
    /**
     * Check if a string contains only digit (0 -> 9) or space.
     * @param value can be null.
     * @return false if input is empty or has any non-digit character (Exclude space).
     */
    public static boolean hasDigitAndSpaceOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return true;
        }
        return value.chars().allMatch(character -> Character.isDigit(character) || Character.isSpaceChar(character));
    }
    
    /**
     * Check if a string contains only letter (a -> z, A -> Z).
     * This method will consider space and empty as non-letter character.
     * @param value can be null.
     * @return false if input is empty or has any non-letter character (Include space).
     */
    public static boolean hasLetterOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return false;
        }
        return value.chars().allMatch(Character::isLetter);
    }
    
    /**
     * Check if a string contains only letter (a -> z, A -> Z) and space.
     * @param value can be null.
     * @return false if input is empty or has any non-letter character (Exclude space).
     */
    public static boolean hasLetterAndSpaceOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return true;
        }
        return value.chars().allMatch(character -> Character.isLetter(character) || Character.isSpaceChar(character));
    }
    
    /**
     * Check if a string contains only letter (a -> z, A -> Z).
     * This method will consider space and empty as non-letter character.
     * @param value can be null.
     * @return false if input is empty or has any non-letter character (Include space).
     */
    public static boolean hasLetterAndDigitOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return false;
        }
        return value.chars().allMatch(character -> Character.isLetter(character) || Character.isDigit(character));
    }
    
    /**
     * Check if a string contains only letter (a -> z, A -> Z).
     * This method will consider space and empty as non-letter character.
     * @param value can be null.
     * @return false if input is empty or has any non-letter character (Include space).
     */
    public static boolean hasLetterAndDigitAndSpaceOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return true;
        }
        return value.chars().allMatch(AString::isLetterOrDigitOrSpace);
    }
    
    /**
     * Check if a string contains only letter (a -> z, A -> Z).
     * This method will consider space and empty as non-letter character.
     * @param value can be null.
     * @return false if input is empty or has any non-letter character (Include space).
     */
    public static boolean hasUniqueCharacterOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return false;
        }
        return value.chars().noneMatch(AString::isLetterOrDigitOrSpace);
    }
    
    
    /**
     * Check if a string contains only letter (a -> z, A -> Z).
     * This method will consider space and empty as non-letter character.
     * @param value can be null.
     * @return false if input is empty or has any non-letter character (Include space).
     */
    public static boolean hasUniqueCharacterAndSpaceOnly(@Nullable final String value) {
        if (isBlank(value)) {
            return true;
        }
        return value.chars().noneMatch(character -> Character.isLetter(character) || Character.isDigit(character));
    }
    
    private static boolean isLetterOrDigitOrSpace(final int character) {
        return Character.isLetter(character) || Character.isDigit(character) || Character.isSpaceChar(character);
    }
    
    /**
     * Get a substring before the first occurrence of a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: "hell"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right after the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring before the input word.
     */
    @NotNull
    public static String beforeOf(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        int index = parent.indexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(0, index);
    }
    
    /**
     * Get a substring that does not include the substring after a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: "hello"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right after the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring that no contains substring after the word.
     */
    @NotNull
    public static String beforeOfIncluding(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        int index = parent.indexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(0, index + word.length());
    }

    /**
     * Get a substring before the first occurrence of a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: "hell"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right after the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring before the input word.
     */
    @NotNull
    public static String beforeLastOf(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        int index = parent.lastIndexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(0, index);
    }

    /**
     * Get a substring that does not include the substring after a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: "hello"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right after the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring that no contains substring after the word.
     */
    @NotNull
    public static String beforeLastOfIncluding(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        int index = parent.lastIndexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(0, index + word.length());
    }

    /**
     * Get a substring after the first occurrence of a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: " word"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right before the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring after the input word.
     */
    @NotNull
    public static String afterOf(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        if (word.isEmpty()) {
            return parent;
        }
        int index = parent.indexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(index + word.length());
    }
    
    /**
     * Get a substring that does not include the substring before a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: "o word"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right before the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring that no contains substring before the word.
     */
    @NotNull
    public static String afterOfIncluding(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        if (word.isEmpty()) {
            return parent;
        }
        int index = parent.indexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(index - word.length() + 1);
    }

    /**
     * Get a substring after the first occurrence of a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: " word"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right before the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring after the input word.
     */
    @NotNull
    public static String afterLastOf(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        if (word.isEmpty()) {
            return parent;
        }
        int index = parent.lastIndexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(index + word.length());
    }

    /**
     * Get a substring that does not include the substring before a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: "o word"
     * </pre>
     * This method will not check blank for the word.
     * @param word the substring which is right before the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring that no contains substring before the word.
     */
    @NotNull
    public static String afterLastOfIncluding(@Nullable final String word, @Nullable final String parent) {
        if (word == null || parent == null) {
            return EMPTY;
        }
        if (word.isEmpty()) {
            return parent;
        }
        int index = parent.lastIndexOf(word);
        if (index < 0) {
            return EMPTY;
        }
        return parent.substring(index - word.length() + 1);
    }

    /**
     * Get a substring after the first occurrence of a specific word.
     * <pre>Example:
     *     parent: "hello world."
     *     child: "o"
     *     return: " word"
     * </pre>
     * This method will not check blank for the word.
     * @param start the substring which is right before the result.
     * @param parent can be null.
     * @return empty String if inputs are null or when word is not present in the parent string.
     * Otherwise, return the substring after the input word.
     */
    @NotNull
    public static String between(@Nullable final String start, @Nullable final String end, @Nullable final String parent) {
        if (isEmpty(parent)) {
            return EMPTY;
        }
        if (isEmpty(start) && isEmpty(end)) {
            return emptyIfNull(parent);
        }
        if (isEmpty(start)) {
            return beforeOf(end, parent);
        }
        if (isEmpty(end)) {
            return afterOf(start, parent);
        }
        int startIndex = parent.indexOf(start);
        if (startIndex < 0) {
            return EMPTY;
        }
        int endIndex = parent.indexOf(end);
        if (endIndex < 0) {
            return EMPTY;
        }
        if (startIndex + start.length() >= endIndex) {
            return EMPTY;
        }
        return parent.substring(startIndex + start.length(), endIndex);
    }

    @NotNull
    public static String betweenLastOf(@Nullable final String start, @Nullable final String end, @Nullable final String parent) {
        if (isEmpty(parent)) {
            return EMPTY;
        }
        if (isEmpty(start) && isEmpty(end)) {
            return emptyIfNull(parent);
        }
        if (isEmpty(start)) {
            return beforeOf(end, parent);
        }
        if (isEmpty(end)) {
            return afterOf(start, parent);
        }
        int startIndex = parent.lastIndexOf(start);
        if (startIndex < 0) {
            return EMPTY;
        }
        int endIndex = parent.lastIndexOf(end);
        if (endIndex < 0) {
            return EMPTY;
        }
        if (startIndex + start.length() >= endIndex) {
            return EMPTY;
        }
        return parent.substring(startIndex + start.length(), endIndex);
    }

    @NotNull
    public static String largestBetween(@Nullable final String start, @Nullable final String end, @Nullable final String parent) {
        if (isEmpty(parent)) {
            return EMPTY;
        }
        if (isEmpty(start) && isEmpty(end)) {
            return emptyIfNull(parent);
        }
        if (isEmpty(start)) {
            return beforeOf(end, parent);
        }
        if (isEmpty(end)) {
            return afterOf(start, parent);
        }
        int startIndex = parent.indexOf(start);
        if (startIndex < 0) {
            return EMPTY;
        }
        int endIndex = parent.lastIndexOf(end);
        if (endIndex < 0) {
            return EMPTY;
        }
        if (startIndex + start.length() >= endIndex) {
            return EMPTY;
        }
        return parent.substring(startIndex + start.length(), endIndex);
    }

    /**
     * Append non-null variables into a string.
     * @param messagePattern a format text. can be null.
     * This messagePattern will contains '{}' which will then replace by following arguments.
     * Example: My name is {}.
     * For more information, please visit this page: https://stackoverflow.com/a/43262120
     * @param args values which will be used to fill in messagePattern.
     * @return empty if messagePattern is null. Otherwise, return a string which contains the arguments.
     */
    @NotNull
    public static String format(String messagePattern, Object... args)
    {
        if (Pattern.matches(".*\\{\\d+,.+,.+}.*", messagePattern))
        {
            return MessageFormat.format(messagePattern, args);
        }
        String r = "\\{\\d}";

        if(!messagePattern.matches(".*" + r + ".*"))
        {
            return MessageFormatter.arrayFormat(messagePattern, args).getMessage();
        }

        Pattern pattern = Pattern.compile(r);
        Matcher matcher = pattern.matcher(messagePattern);
        StringBuilder stringBuilder = new StringBuilder();
        int parsingValue = 0;

        while(matcher.find())
        {
            matcher.appendReplacement(stringBuilder, args[parsingValue].toString());
            parsingValue++;
        }
        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }


    @NotNull
    public static String concat(@Nullable final String... words) {
        if (words.length == 0) {
            return EMPTY;
        }
        AList.listOf(words).forEach(STRING_BUILDER::append);
        String result = STRING_BUILDER.toString();
        STRING_BUILDER.setLength(0);
        return result;
    }

    @NotNull
    public static String concatNonNull(@Nullable final String... words) {
        if (words.length == 0) {
            return EMPTY;
        }
        AList.nonNullListOf(words).forEach(STRING_BUILDER::append);
        String result = STRING_BUILDER.toString();
        STRING_BUILDER.setLength(0);
        return result;
    }

    /**
     * Join list of string. This method will join only non-null string.
     * @param delimiter can be null. Will be turn to empty string if null.
     * @param words can be null.
     * @return empty if words is null. Otherwise, return the joined string.
     */
    @NotNull
    public static String join(@Nullable final String delimiter, @Nullable final Collection<String> words) {
        if (ACollection.isEmpty(words)) {
            return EMPTY;
        }
        return String.join(
            Optional.ofNullable(delimiter).orElse(EMPTY),
            words.stream().filter(Objects::nonNull).toArray(CharSequence[]::new)
        );
    }
    
    /**
     * Join list of string. This method will join only non-null string.
     * @param delimiter can be null. Will be turn to empty string if null.
     * @param words can be null.
     * @return empty if words is null. Otherwise, return the joined string.
     */
    @SafeVarargs
    @NotNull
    public static String join(@Nullable final String delimiter, @Nullable final String... words) {
        return join(delimiter, AList.listOf(words));
    }
    
    /**
     * Join list of string. This method will join only non-null string.
     * @param delimiter can be null. Will be turn to empty string if null.
     * @param words can be null.
     * @return empty if words is null. Otherwise, return the joined string.
     */
    @SafeVarargs
    @NotNull
    public static String join(@Nullable final String delimiter, @Nullable final Object... words) {
        return join(delimiter, AList.listOf(words).stream().map(String::valueOf).collect(Collectors.toList()));
    }
    
    public static String secondsOf(final long milliseconds) {
        final double MILLISECONDS_PER_SECOND = 1000.0;
        return String.format("%.2f", (float) milliseconds / MILLISECONDS_PER_SECOND);
    }
}

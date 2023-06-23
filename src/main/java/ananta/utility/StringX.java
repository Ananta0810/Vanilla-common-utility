package ananta.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Ananta0810
 * This class provides some methods related to type String.
 * Instead of returning NULL, methods will return a empty String.
 * Most methods can handle NULL input well.
 */
public final class StringX {

    public final static String EMPTY = "";
    private static final StringBuilder STRING_BUILDER = new StringBuilder();
    public static final String SECURITY_CHAR = "*";
    public static final int SECURITY_MIN_LENGTH = 8;
    public static final int SECURITY_HINT_LENGTH = 3;

    private StringX() {
    }

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
    public static String emptyIfNull(@Nullable final String string) {
        return string == null ? EMPTY : string;
    }

    /**
     * Get length of a string.
     * @param input String that you want to get length, can be null.
     * @return 0 if input is null, Otherwise, return length of input.
     */
    public static int lengthOf(@Nullable final String input) {
        if (input == null) {
            return 0;
        }
        return input.length();
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
        return value.chars().allMatch(StringX::isLetterOrDigitOrSpace);
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
        return value.chars().noneMatch(StringX::isLetterOrDigitOrSpace);
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
        final int index = parent.indexOf(word);
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
        final int index = parent.indexOf(word);
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
        final int index = parent.lastIndexOf(word);
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
        final int index = parent.lastIndexOf(word);
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
        final int index = parent.indexOf(word);
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
        final int index = parent.indexOf(word);
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
        final int index = parent.lastIndexOf(word);
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
        final int index = parent.lastIndexOf(word);
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
        final int startIndex = parent.indexOf(start);
        if (startIndex < 0) {
            return EMPTY;
        }
        final int endIndex = parent.indexOf(end);
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
        final int startIndex = parent.lastIndexOf(start);
        if (startIndex < 0) {
            return EMPTY;
        }
        final int endIndex = parent.lastIndexOf(end);
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
        final int startIndex = parent.indexOf(start);
        if (startIndex < 0) {
            return EMPTY;
        }
        final int endIndex = parent.lastIndexOf(end);
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
    public static String format(final String messagePattern, final Object... args)
    {
        if (Pattern.matches(".*\\{\\d+,.+,.+}.*", messagePattern))
        {
            return MessageFormat.format(messagePattern, args);
        }
        final String r = "\\{\\d}";

        if(!messagePattern.matches(".*" + r + ".*"))
        {
            return MessageFormatter.arrayFormat(messagePattern, args).getMessage();
        }

        final Pattern pattern = Pattern.compile(r);
        final Matcher matcher = pattern.matcher(messagePattern);
        final StringBuilder stringBuilder = new StringBuilder();
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
        ListX.listOf(words).forEach(STRING_BUILDER::append);
        final String result = STRING_BUILDER.toString();
        STRING_BUILDER.setLength(0);
        return result;
    }

    @NotNull
    public static String concatNonNull(@Nullable final String... words) {
        if (words.length == 0) {
            return EMPTY;
        }
        ListX.nonNullListOf(words).forEach(STRING_BUILDER::append);
        final String result = STRING_BUILDER.toString();
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
        if (CollectionX.isEmpty(words)) {
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
        return join(delimiter, ListX.listOf(words));
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
        return join(delimiter, ListX.listOf(words).stream().map(String::valueOf).collect(Collectors.toList()));
    }

    /**
     * Hide your input with {@value #SECURITY_CHAR} to reduce security risk.
     * @param input String that you want to hide from others.
     * @param minLength total minimum of {@value #SECURITY_CHAR} that you want to appear in your output.
     *                  NOTE: This value must be larger or equals to zero.
     *                  If your input has total length smaller than this length, then your output
     *                  will have this minLength {@value #SECURITY_CHAR} character.<br/>
     *                  Example: <br/>
     *                  - Your input is null, minLength is 8 then output will be "********" <br/>
     *                  - Your input is "123456", minLength is 8 then output will be "********"<br/>
     *                  - Your input is "1234567890", minLength is 8, hintLength is 1 then output will be "1*********"
     * @param hintLength total characters that you want to show to others as a hint.<br/>
     *                   Example: <br/>
     *                   - Your input is "123456", hintLength is 3 then output will be "123*****"
     * @return the text that has part of it hidden.
     * @throws IllegalArgumentException if minLength or hintLength smaller than zero.
     */
    @NotNull
    public static String hiddenTextOf(@Nullable final String input, final int minLength, final int hintLength) {
        if (input == null) {
            return SECURITY_CHAR.repeat(minLength);
        }
        if (minLength < 0) {
            throw new IllegalArgumentException("Min Length must be larger or equals to zero.");
        }
        if (hintLength < 0) {
            throw new IllegalArgumentException("Hint Length must be larger or equals to zero.");
        }
        final int length = input.length();
        if (length <= minLength) {
            return SECURITY_CHAR.repeat(minLength);
        }
        final String end = input.substring(hintLength);
        return input.replace(end, SECURITY_CHAR.repeat(end.length()));
    }

    /**
     * Hide your input with {@value #SECURITY_CHAR} to reduce security risk. <br/>
     * The output will have {@value #SECURITY_MIN_LENGTH} as minimum length
     * and will show {@value #SECURITY_HINT_LENGTH} characters at start as a hint.
     * @param input String that you want to hide from others.
     * @return the text that has part of it hidden.
     */
    @NotNull
    public static String hiddenTextOf(@Nullable final String input) {
        return hiddenTextOf(input, SECURITY_MIN_LENGTH, SECURITY_HINT_LENGTH);
    }
}

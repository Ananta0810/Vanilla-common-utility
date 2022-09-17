package ananta.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Ananta0810
 * This class provides some methods related to type String.
 * Instead of returning NULL, methods will return a empty String.
 * Most methods can handle NULL input well.
 */
public final class AString {
    
    private final static String EMPTY = "";
    
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
     * Get a substring after the first occurrence of a specific word.
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
        return parent.substring(index + 1);
    }
    
    /**
     * Append non-null variables into a string.
     * @param pattern a format text. can be null.
     * This pattern will contains '{}' which will then replace by following arguments.
     * Example: My name is {}.
     * @param args values which will be used to fill in pattern.
     * @return empty if pattern is null. Otherwise, return a string which contains the arguments.
     */
    @NotNull
    public static String format(@Nullable final String pattern, @Nullable final Object... args) {
        if (pattern == null) {
            return EMPTY;
        }
        return MessageFormat.format(pattern, AList.nonNullListOf(args));
    }
    
    /**
     * Join list of string. This method will join only non-null string.
     * @param delimiter can be null. Will be turn to empty string if null.
     * @param words can be null.
     * @return empty if words is null. Otherwise, return the joined string.
     */
    @NotNull
    public static String join(@Nullable final String delimiter, @Nullable final List<Object> words) {
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
    @NotNull
    public static String join(@Nullable final String delimiter, @Nullable final Object... words) {
        return join(delimiter, AList.listOf(words));
    }
}

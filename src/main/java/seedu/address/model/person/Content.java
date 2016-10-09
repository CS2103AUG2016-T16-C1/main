package seedu.address.model.person;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's Content in the Content book.
 * Guarantees: immutable; is valid as declared in {@link #isValidContent(String)}
 */
public class Content {
    
    public static final String MESSAGE_CONTENT_CONSTRAINTS = "Task Content can be in any format";
    public static final String CONTENT_VALIDATION_REGEX = ".+";

    public final String value;

    /**
     * Validates given Content.
     *
     * @throws IllegalValueException if given Content string is invalid.
     */
    public Content(String contentString) throws IllegalValueException {
        assert contentString != null;
        if (!isValidContent(contentString)) {
            throw new IllegalValueException(MESSAGE_CONTENT_CONSTRAINTS);
        }
        this.value = contentString;
    }

    /**
     * Returns true if a given string is a valid task content.
     */
    public static boolean isValidContent(String test) {
        return test.matches(CONTENT_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Content // instanceof handles nulls
                && this.value.equals(((Content) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

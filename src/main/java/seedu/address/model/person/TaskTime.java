package seedu.address.model.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import seedu.address.commons.exceptions.IllegalValueException;

public class TaskTime {

    public static final String MESSAGE_TIME_CONSTRAINTS = "Task time should only follow this format HH:MM";
    public static final String TASKTIME_VALIDATION_REGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    public Date value;
    public String timeString;
    
    public TaskTime() {
    	timeString = "";
    }
    /**
     * Validates given date.
     *
     * @throws IllegalValueException if given date string is invalid.
     * @throws ParseException 
     */
    public TaskTime(String timeString) throws IllegalValueException, ParseException {
        assert timeString != null;
        this.timeString = timeString.trim();
        if (!isValidTaskTime(timeString)) {
            throw new IllegalValueException(MESSAGE_TIME_CONSTRAINTS);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        Date time = simpleDateFormat.parse(timeString);
        this.value = time;

    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidTaskTime(String test) {
        return test.matches(TASKTIME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        //return value.toString();
    	return timeString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskTime // instance of handles nulls
                && this.value.equals(((TaskTime) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

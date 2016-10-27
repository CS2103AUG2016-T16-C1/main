package seedu.address.model.person;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import seedu.address.commons.exceptions.IllegalValueException;
//@@author A0139523E
public class TaskDate {

	public static final String MESSAGE_DATE_CONSTRAINTS = "Task date should only follow this format dd-mm-yyyy";
	public static final String TASKDATE_VALIDATION_REGEX = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";
	public static final String MESSAGE_ENDDATE_CONSTRAINTS = "End date should only follow this format dd-mm-yyyy";
	public static final String MESSAGE_STARTENDDATE_CONSTRAINTS = "Start date must be added";
	
	public Date value;
	public Date endDate;
	public String dateString;
	public String enddateString;
	
	public TaskDate() {
		this.dateString = "";
		this.enddateString = "";
	}

	/**
	 * Validates given date.
	 *
	 * @throws IllegalValueException
	 *             if given date string is invalid.
	 * @throws ParseException
	 */
	public TaskDate(String dateString, String enddateString) throws IllegalValueException, ParseException {
		if(enddateString != null) {
			if(dateString == null) {
				throw new IllegalValueException(MESSAGE_STARTENDDATE_CONSTRAINTS);
			}
		
		this.dateString = dateString.trim();
		this.enddateString = enddateString.trim();
		
		if (!isValidTaskDate(dateString)) {
			throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
		}
		
		if(!isValidEndDate(enddateString)) {
			throw new IllegalValueException(MESSAGE_ENDDATE_CONSTRAINTS);
			
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date date = simpleDateFormat.parse(dateString);
		Date date2 = simpleDateFormat.parse(enddateString);
		this.value = date;
		this.endDate = date2;
		
		}
		
		else {
			enddateString = "";
			this.dateString = dateString.trim();
			if(!isValidTaskDate(dateString)) {
				throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
			}
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			Date date = simpleDateFormat.parse(dateString);
			this.value = date;
		}
	}
	
	
	/**
	 * Returns true if a given string is a valid date.
	 */
	//@@author A0135787N
	public static boolean isValidTaskDate(String test) {
		return test.matches(TASKDATE_VALIDATION_REGEX);
	}

	public static boolean isValidEndDate(String test) {
		return test.matches(TASKDATE_VALIDATION_REGEX);
	}
	
	public Date getValue() {
		return value;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		// return value.toString();
		return dateString;

	}

	@Override
    public boolean equals(Object other) {
    	if (other != null) {
    		return other == this // short circuit if same object
                || (other instanceof TaskDate // instance of handles nulls
                && this.value.equals(((TaskDate) other).value)); // state check
    	}
    	else 
    		return false;
    }

	@Override
	public int hashCode() {
		return value.hashCode();
	}

}

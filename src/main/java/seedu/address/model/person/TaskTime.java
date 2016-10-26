package seedu.address.model.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import seedu.address.commons.exceptions.IllegalValueException;

public class TaskTime {

    public static final String MESSAGE_TIME_CONSTRAINTS = "Task time should only follow this format HH:MM";
    public static final String TASKTIME_VALIDATION_REGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final String MESSAGE_ENDTIME_CONSTRAINTS = "End time should only follow this format HH:MM";
    public static final String MESSAGE_STARTENDTIME_CONSTRAINTS = "Start time must be added";
    
    public Date value;
    public Date endTime;
    public String endtimeString;
    public String timeString;
    
    public TaskTime() {
    	timeString = "";
    	endtimeString = "";
    }
    
    /**
     * Validates given date.
     *
     * @throws IllegalValueException if given date string is invalid.
     * @throws ParseException 
     */
    public TaskTime(String timeString, String endtimeString) throws IllegalValueException, ParseException {
    	if(endtimeString != null) {
    	   if(timeString == null) {
    		   throw new IllegalValueException(MESSAGE_STARTENDTIME_CONSTRAINTS);
    	   }
    	   
        this.timeString = timeString.trim();
        this.endtimeString = endtimeString.trim();
        
        if (!isValidTaskTime(timeString)) {
            throw new IllegalValueException(MESSAGE_TIME_CONSTRAINTS);
        }
        
        if(!isValidEndTime(endtimeString)) {
        	throw new IllegalValueException(MESSAGE_ENDTIME_CONSTRAINTS);
        }
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        Date time = simpleDateFormat.parse(timeString);
        Date time2 = simpleDateFormat.parse(endtimeString);
        this.value = time;
        this.endTime = time2;

       }
       
       else {
    	   endtimeString = "";
    	   this.timeString = timeString.trim();
    	   if(!isValidTaskTime(timeString)) {
    		   throw new IllegalValueException(MESSAGE_TIME_CONSTRAINTS);
    	   }
    	   
    	   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    	   
    	   Date time = simpleDateFormat.parse(timeString);
    	   this.value = time;
       }
    }
    
    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidTaskTime(String test) {
        return test.matches(TASKTIME_VALIDATION_REGEX);
    }
    
    public static boolean isValidEndTime(String test) {
    	return test.matches(TASKTIME_VALIDATION_REGEX);
    }
    
    public Date getValue() {
        return value;
    }
    
    public Date getEndTime() {
    	return endTime;
    }
    
    @Override
    public String toString() {    	
    	if(!endtimeString.isEmpty() && !timeString.isEmpty()) {
    		return timeString + "-" + endtimeString;
    	}
    	else if(endtimeString.isEmpty() && timeString.isEmpty())
    		return "";
    	else {
    		return timeString;
    	}

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

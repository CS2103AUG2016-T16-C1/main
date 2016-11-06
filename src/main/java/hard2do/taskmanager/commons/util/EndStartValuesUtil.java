package hard2do.taskmanager.commons.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hard2do.taskmanager.commons.exceptions.IllegalValueException;

/**
 * Validates the given start and end time values.
 * 
 * @@author A0135787N
 */

public class EndStartValuesUtil {
	
	private final static Pattern DATE_STRING_FORMAT = Pattern.compile("(?<day>\\d+)-(?<month>\\d+)-(?<year>\\d+)");
	private final static Pattern TIME_STRING_FORMAT = Pattern.compile("(?<hour>\\d+):(?<min>\\d+)");
	
	private static final String MESSAGE_ENDDATE_CONSTRAINT = "End date cannot be earlier than start date";
	private static final String MESSAGE_ENDTIME_CONSTRAINT = "End time cannot be earlier than start time";
	private static final String MESSAGE_SAME_TIME_CONSTRAINT = "End time cannot be same as start time";
	private static final String MESSAGE_DATE_FORMAT = "Dates must be in [dd-mm-yyyy] format";
	private static final String MESSAGE_TIME_FORMAT = "Time must be in [HH:mm] format";
	

	/**
	 * Determines if end date is earlier than start date.
	 * 
	 * @param startDate
	 * @param endDate
	 * @throws IllegalValueException
	 */
	public static void dateRangeValid(String startDate, String endDate) throws IllegalValueException {
		final Matcher matchStart = DATE_STRING_FORMAT.matcher(startDate);
		final Matcher matchEnd = DATE_STRING_FORMAT.matcher(endDate);
		
		if (!matchStart.matches() || !matchEnd.matches()) {
			throw new IllegalValueException(MESSAGE_DATE_FORMAT);
		}
		int startYear = Integer.parseInt(matchStart.group("year"));
		int startMonth = Integer.parseInt(matchStart.group("month"));
		int startDay = Integer.parseInt(matchStart.group("day"));
		
		int endYear = Integer.parseInt(matchEnd.group("year"));
		int endMonth = Integer.parseInt(matchEnd.group("month"));
		int endDay = Integer.parseInt(matchEnd.group("day"));
		
		if (endYear < startYear) {
			throw new IllegalValueException(MESSAGE_ENDDATE_CONSTRAINT);
		}
		if (endMonth < startMonth && endYear == startYear) {
			throw new IllegalValueException(MESSAGE_ENDDATE_CONSTRAINT);
		}
		if (endDay < startDay && endMonth == startMonth && endYear == startYear) {
			throw new IllegalValueException(MESSAGE_ENDDATE_CONSTRAINT);
		}
		
	}
	
	/**
	 * Determines if end time is earlier or same as start time.
	 * 
	 * @param startTime
	 * @param endTime
	 * @throws IllegalValueException
	 */
	public static void timeRangeValid(String startTime, String endTime) throws IllegalValueException {
		final Matcher matchStart = TIME_STRING_FORMAT.matcher(startTime);
		final Matcher matchEnd = TIME_STRING_FORMAT.matcher(endTime);
		
		if (!matchStart.matches() || !matchEnd.matches()) {
			throw new IllegalValueException(MESSAGE_TIME_FORMAT);
		}
		int startHour = Integer.parseInt(matchStart.group("hour"));
		int startMin = Integer.parseInt(matchStart.group("min"));
		
		int endHour = Integer.parseInt(matchEnd.group("hour"));
		int endMin = Integer.parseInt(matchEnd.group("min"));
		
		if (endHour < startHour) {
			throw new IllegalValueException(MESSAGE_ENDTIME_CONSTRAINT);
		}
		if (endMin < startMin && endHour == startHour) {
			throw new IllegalValueException(MESSAGE_ENDTIME_CONSTRAINT);
		}
		if (endMin == startMin && endHour == startHour) {
			throw new IllegalValueException(MESSAGE_SAME_TIME_CONSTRAINT);
		}	
	}
	
}

package hard2do.taskmanager.logic.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.commons.util.InferDateUtil;
import hard2do.taskmanager.commons.util.InferTimeUtil;
import hard2do.taskmanager.model.tag.Tag;
import hard2do.taskmanager.model.tag.UniqueTagList;
import hard2do.taskmanager.model.task.*;

/**
 * Adds a task to the task manager.
 */
// @@author A0135787N-reused
public class AddCommand extends Command {

	public static final String COMMAND_WORD = "add";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. \n"
			+ "Parameters: CONTENT sd/DATE[dd-mm-yyyy] ed/DATE[dd-mm-yyyy] st/time[HH:mm] et/endTime[HH:mm] [#TAG]...\n"
			+ "Note: order and presence of parameters after CONTENT do not matter. \n" + "Example: " + COMMAND_WORD
			+ " do this task manager sd/20-10-2016 ed/20-10-2016 st/13:00 et/16:00 #shaglife #wheregottime";

	public static final String MESSAGE_SUCCESS = "New task added: %1$s";
	public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";
	public static final String MESSAGE_STARTENDDATE_CONSTRAINTS = "Start date must be added";
	public static final String MESSAGE_STARTENDTIME_CONSTRAINTS = "Start time must be added";
	public static final String MESSAGE_ENDDATETIME_CONSTRAINTS = "End date must have a corresponding end time, vice versa";
	public static final String MESSAGE_ENDDATE_CONSTRAINT = "End date cannot be earlier than start date";
	
	private final static Pattern DATE_STRING_FORMAT = Pattern.compile("(?<day>\\d+)-(?<month>\\d+)-(?<year>\\d+)");
	private final static Pattern TIME_STRING_FORMAT = Pattern.compile("(?<hours>\\d+):(?<mins>\\d+)");

	private final ReadOnlyTask toAdd;

	private TaskDate dateToAdd;
	private TaskTime timeToAdd;
	private TaskDate enddateToAdd;
	private TaskTime endtimeToAdd;

	/**
	 * Convenience constructor using raw values.
	 *
	 * @throws IllegalValueException
	 *             if any of the raw values are invalid
	 * @throws ParseException
	 */

	public AddCommand(String content, String date, String endDate, String time, String endTime, Integer duration,
			Set<String> tags) throws IllegalValueException, ParseException {
		assert content != null;

		isValidTimeDate(date, endDate, time, endTime);

		final Set<Tag> tagSet = new HashSet<>();
		for (String tagName : tags) {
			tagSet.add(new Tag(tagName));
		}

		// Infer date and time
		if (date != null) {
			dateToAdd = new TaskDate(date);
		} else if (date == null) {
			InferDateUtil idu = new InferDateUtil(content);
			if (idu.findDate()) {
				dateToAdd = new TaskDate(idu.getDate());
			} else {
				dateToAdd = new TaskDate();
			}
		}
		// check null for date and time
		if (endDate == null) {
			enddateToAdd = new TaskDate();
		} else
			enddateToAdd = new TaskDate(endDate);

		if (time != null) {
			timeToAdd = new TaskTime(time);
			if (endTime != null) {
				endtimeToAdd = new TaskTime(endTime);
			}
		} else if (time == null && endTime == null) {
			InferTimeUtil itu = new InferTimeUtil(content);
			if (itu.findTimeToTime()) {
				timeToAdd = new TaskTime(itu.getStartTime());
				endtimeToAdd = new TaskTime(itu.getEndTime());
			} else if (itu.findTime()) {
				timeToAdd = new TaskTime(itu.getTime());
				endtimeToAdd = new TaskTime();
			} else {
				timeToAdd = new TaskTime();
				endtimeToAdd = new TaskTime();
			}
		}

		if (duration != null) {
			this.toAdd = new RecurringTask(new Content(content), dateToAdd, timeToAdd, duration,
					new UniqueTagList(tagSet));
		} else if(endtimeToAdd != null || enddateToAdd != null) {
				this.toAdd = new Task(new Content(content), dateToAdd, enddateToAdd, timeToAdd, endtimeToAdd,
						new UniqueTagList(tagSet));
			}
		else {
			this.toAdd = new Task(new Content(content), dateToAdd, timeToAdd, new UniqueTagList(tagSet));
		}
	}

	@Override
	public CommandResult execute() {
		assert model != null;
		if (true){};
		try {
			model.addTask(toAdd);
			return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
		} catch (UniqueTaskList.DuplicateTaskException e) {
			return new CommandResult(MESSAGE_DUPLICATE_TASK);
		}

	}

	public static void isValidTimeDate(String startDate, String endDate, String startTime, String endTime)
			throws IllegalValueException {
		if ((endDate != null && endTime == null) || (endDate == null && endTime != null)) {
			throw new IllegalValueException(MESSAGE_ENDDATETIME_CONSTRAINTS);
		}

		if (endDate != null && endTime != null) {
			hasStartDate(startDate);
			hasStartTime(startTime);
		}
		if (endDate != null && startDate != null) {
			dateRangeValid(startDate, endDate);
		}
		
	}

	public static void hasStartDate(String startDate) throws IllegalValueException {
		if (startDate == null)
			throw new IllegalValueException(MESSAGE_STARTENDDATE_CONSTRAINTS);
	}

	public static void hasStartTime(String startTime) throws IllegalValueException {
		if (startTime == null) {
			throw new IllegalValueException(MESSAGE_STARTENDTIME_CONSTRAINTS);
		}
	}
	
	public static void dateRangeValid(String startDate, String endDate) throws IllegalValueException {
		final Matcher matchStart = DATE_STRING_FORMAT.matcher(startDate);
		final Matcher matchEnd = DATE_STRING_FORMAT.matcher(endDate);
		
		if (!matchStart.matches() || !matchEnd.matches()) {
			throw new IllegalValueException(MESSAGE_USAGE);
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
	
}

package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.InferDateUtil;
import seedu.address.commons.util.InferTimeUtil;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Adds a task to the task manager.
 */
//@@author A0135787N-reused
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. \n"
            + "Parameters: CONTENT sd/DATE[dd-mm-yyyy] ed/DATE[dd-mm-yyyy] st/time[HH:mm] et/endTime[HH:mm] [#TAG]...\n"
    		    + "Note: order and presence of parameters after CONTENT do not matter. \n"
            + "Example: " + COMMAND_WORD
            + " do this task manager sd/20-10-2016 ed/20-10-2016 st/13:00 et/16:00 #shaglife #wheregottime";


    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";
    private final Task toAdd;

    private TaskDate dateToAdd;
    private TaskTime timeToAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     * @throws ParseException
     */

    public AddCommand(String content, String date, String enddate, String time, String endTime, Integer duration, Set<String> tags)
            throws IllegalValueException, ParseException {
    	assert content != null;

        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }

        if(date != null ){
        	dateToAdd = new TaskDate(date, enddate);
        	
        }else if (date == null && enddate == null){
        	InferDateUtil idu = new InferDateUtil(content);
        	
        	if(idu.findDate()){
        		String inferredDate = idu.getDate();
        		dateToAdd = new TaskDate(inferredDate, enddate);
        	}else{
        		dateToAdd = new TaskDate();
        	}
        }

        if(time == null && endTime == null){
        	InferTimeUtil itu = new InferTimeUtil(content);
        	if(itu.findTime()){
        		timeToAdd = new TaskTime(itu.getTime(), endTime);
        	}else{
        		timeToAdd = new TaskTime();
        	}
        }
        else
        	timeToAdd = new TaskTime(time, endTime);


        this.toAdd = new Task(
                new Content(content),
                dateToAdd,
                timeToAdd,
                duration,
                new UniqueTagList(tagSet)
        );
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }

    }

}

package hard2do.taskmanager.logic.commands;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.core.UnmodifiableObservableList;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.commons.util.EndStartValuesUtil;
import hard2do.taskmanager.model.tag.Tag;
import hard2do.taskmanager.model.tag.UniqueTagList;
import hard2do.taskmanager.model.task.*;
import hard2do.taskmanager.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0135787N
/**
 * Edits a task in the task manager.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a task listed the task manager. "
            + "Parameters: INDEX[MUST BE POSITIVE INTEGER] c/CONTENT sd/DATE[dd-mm-yyyy] ed/DATE[dd-mm-yyyy] st/time[HH:mm] et/time[HH:mm]\n"
            + "Example: " + COMMAND_WORD
            + " 1 c/do this task manager sd/20-10-2016 ed/ 20-10-2016 st/13:00 et/17:00";

    public static final String MESSAGE_SUCCESS = "Task edited: %1$s";
 
    private int targetIndex;
    private String newDate;
    private String newTime;
    private String newContent;
    private String newEndTime;
    private String newEndDate;
    boolean valid = false;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     * @throws ParseException 
     */
    @SuppressWarnings("resource")
	public EditCommand(String index, String taskDetails)
            	throws IllegalValueException, ParseException {
    		this.targetIndex = Integer.parseInt(index.trim());
    		
    		
    		Scanner sc = new Scanner(taskDetails);
    		if (sc.findInLine("sd/") != null) {
    			newDate = sc.next();
    			valid = true;
    			sc = new Scanner(taskDetails);	
    		}
    		if (sc.findInLine("ed/") != null) {
    			newEndDate = sc.next();
    			valid = true;
    			sc = new Scanner(taskDetails);
    		}
    		if (sc.findInLine("st/") != null) {
    			newTime = sc.next();
    			valid = true;
    			sc = new Scanner(taskDetails);
    		}	
    		if (sc.findInLine("et/") != null) {
    			newEndTime = sc.next();
    			valid = true;
    			sc = new Scanner(taskDetails);
    		}
    		if (sc.findInLine("c/") != null) {
    			valid = true;
    			StringBuilder data = new StringBuilder();
    			while(sc.hasNext()){
    				String check = sc.next();
    				if (check.startsWith("sd/") || check.startsWith("st/") || 
    						check.startsWith("et/") || check.startsWith("ed/")) {
    					break;
    				} else {
    					data.append(" " + check);
    				}
    			}
    			newContent = data.toString().trim(); 
    		}
    		
    		sc.close();
        if (!valid) {
        	throw new IllegalValueException(MESSAGE_USAGE);
        }
    }

    @Override
    public CommandResult execute() {
    	UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        ReadOnlyTask taskToEdit = lastShownList.get(targetIndex - 1);
        try {
        	isValidTimeDate(taskToEdit);
            model.editTask(taskToEdit, newDate, newEndDate, newTime, newEndTime, newContent);
        } catch (TaskNotFoundException | ParseException tnfe) {
            assert false: "The target task cannot be missing";
        } catch (IllegalValueException e) {
			return new CommandResult("Date or Time is invalid");
		} 
        UnmodifiableObservableList<ReadOnlyTask> updatedList = model.getFilteredTaskList();
        ReadOnlyTask editedTask = updatedList.get(targetIndex - 1);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedTask));
    }
    /**
     * Ensures that the new date and time values are valid
     * 
     * @param taskToEdit
     * @throws IllegalValueException
     */
    
	public void isValidTimeDate(ReadOnlyTask taskToEdit) throws IllegalValueException {
		
		if (newDate != null && newEndDate != null) {
			EndStartValuesUtil.dateRangeValid(newDate, newEndDate);
		}
		if (newDate != null && newEndDate == null && !taskToEdit.getEndDate().dateString.isEmpty()) {
			EndStartValuesUtil.dateRangeValid(newDate, taskToEdit.getEndDate().dateString);
		}
		if (newDate == null && newEndDate != null && !taskToEdit.getDate().dateString.isEmpty()) {
			EndStartValuesUtil.dateRangeValid(newEndDate, taskToEdit.getDate().dateString);
		}
		if (newTime != null && newEndTime != null && newEndDate == null 
				&& taskToEdit.getEndDate().dateString.isEmpty()) {
			EndStartValuesUtil.timeRangeValid(newTime, newEndTime);
		}
		if (newTime != null && newEndTime == null && newEndDate == null 
				&& taskToEdit.getEndDate().dateString.isEmpty() 
				&& !taskToEdit.getDate().dateString.isEmpty()
				&& !taskToEdit.getEndTime().timeString.isEmpty()) {
			
			EndStartValuesUtil.timeRangeValid(newTime, taskToEdit.getEndTime().timeString);
		}
		if (newTime == null && newEndTime != null && newEndDate == null 
				&& taskToEdit.getEndDate().dateString.isEmpty() 
				&& !taskToEdit.getDate().dateString.isEmpty()
				&& !taskToEdit.getTime().timeString.isEmpty()) {
			
			EndStartValuesUtil.timeRangeValid(taskToEdit.getTime().timeString, newEndTime);
		}
		
	}

}


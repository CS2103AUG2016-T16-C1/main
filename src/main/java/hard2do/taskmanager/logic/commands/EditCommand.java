package hard2do.taskmanager.logic.commands;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.core.UnmodifiableObservableList;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.person.*;
import hard2do.taskmanager.model.person.UniqueTaskList.TaskNotFoundException;
import hard2do.taskmanager.model.tag.Tag;
import hard2do.taskmanager.model.tag.UniqueTagList;

/**
 * Edits a task in the task manager.
 */
//@@author A0141054W
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
    		if(sc.findInLine("sd/") != null){
    			newDate = sc.next();
    			sc = new Scanner(taskDetails);	
    		}
    		if(sc.findInLine("ed/") != null){
    			newEndDate = sc.next();
    			sc = new Scanner(taskDetails);
    		}
    		if(sc.findInLine("st/") != null){
    			newTime = sc.next();
    			sc = new Scanner(taskDetails);
    		}	
    		if(sc.findInLine("et/") != null){
    			newEndTime = sc.next();
    			sc = new Scanner(taskDetails);
    		}
    		if(sc.findInLine("c/") != null){
    			StringBuilder data = new StringBuilder();
    			while(sc.hasNext()){
    				String check = sc.next();
    				if(check.startsWith("d/") || check.startsWith("st/") || 
    						check.startsWith("et/") || check.startsWith("ed/")){
    					break;
    				}
    				else{
    					data.append(" " + check);
    				}
    			}
    			newContent = data.toString().trim(); 
    		}
    		
    		sc.close();
        
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
            model.editTask(taskToEdit, newDate, newEndDate, newTime, newEndTime, newContent);
        } catch (TaskNotFoundException | ParseException tnfe) {
            assert false : "The target task cannot be missing";
        } catch (IllegalValueException e) {
			assert false : "Date or Time is invalid";
		} 
        UnmodifiableObservableList<ReadOnlyTask> updatedList = model.getFilteredTaskList();
        ReadOnlyTask editedTask = updatedList.get(targetIndex - 1);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedTask));
    }

}


package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.person.UniqueTaskList.TaskNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Edits a task in the task manager.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a task listed the task manager. "
            + "Parameters: INDEX[MUST BE POSITIVE INTEGER] c/CONTENT d/DATE[dd-mm-yyyy] st/time[HH:mm] et/time[HH:mm]\n"
            + "Example: " + COMMAND_WORD
            + " 1 c/do this task manager d/20-10-2016 st/13:00 et/17:00";

    public static final String MESSAGE_SUCCESS = "Task edited: %1$s";
 
    private int targetIndex;
    private String newDate;
    private String newTime;
    private String newContent;
    private String newEndTime;

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
    		if( sc.findInLine("d/") != null){
    			newDate = sc.next();
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
    				if(check.startsWith("d/") || check.startsWith("st/") || check.startsWith("et/"))
    					break;
    				else
    					data.append(" " + check);
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
        
        try {
            model.editTask(targetIndex - 1, newDate, newTime, newEndTime, newContent);
        } catch (TaskNotFoundException | ParseException tnfe) {
            assert false : "The target task cannot be missing";
        }
        UnmodifiableObservableList<ReadOnlyTask> updatedList = model.getFilteredTaskList();
        ReadOnlyTask editedTask = updatedList.get(targetIndex - 1);
        String[] contentArr = editedTask.getContent().value.split("\\s+"); 
        Set<String> contentSet = new HashSet<>(Arrays.asList(contentArr));
        model.updateFilteredTaskList(contentSet);
        model.updateFilteredListToShowUndone();
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedTask));
    }

}


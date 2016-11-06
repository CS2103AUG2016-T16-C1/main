package hard2do.taskmanager.logic.commands;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.core.UnmodifiableObservableList;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.person.ReadOnlyTask;
import hard2do.taskmanager.model.person.UniqueTaskList.TaskNotFoundException;

//@@author A0135787N
public class AddTagCommand extends Command {
	
    public static final String COMMAND_WORD = "addtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": adds a tag to a task listed in the task manager. \n"
            + "Parameters: INDEX[MUST BE POSITIVE INTEGER] TAGS[ANY NUMBER OF TAGS SEPARATED BY SPACE] \n"
            + "Example: " + COMMAND_WORD
            + " 1 toughlife easygame";
    
    private static final String MESSAGE_INVALID_TAG = "Tags must be alphanumerical";
    private static final String MESSAGE_NO_TAG = "No Tag can be found";
    private static final String MESSAGE_SUCCESS = "Task tags updated: %1$s";
   
    private int targetIndex;
    private ArrayList<String> tagsToAdd;


    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     * @throws ParseException 
     */
   
	public AddTagCommand(String index, String tagsString)
            	throws IllegalValueException, ParseException {
		
    		this.targetIndex = Integer.parseInt(index.trim());
    		tagsToAdd = new ArrayList<String>();
    		
    		Scanner sc = new Scanner(tagsString.trim());
    			while(sc.hasNext()){
    				String tagToAdd = sc.next();
    				tagsToAdd.add(tagToAdd);
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
        if(tagsToAdd.size() == 0)
        	return new CommandResult(MESSAGE_NO_TAG);
        
        ReadOnlyTask taskToAddTags= lastShownList.get(targetIndex - 1);
        
        try {
            model.addTags(taskToAddTags, tagsToAdd);
        } catch (TaskNotFoundException tnfe) {
            assert false : "The target task cannot be missing";
        } catch (ParseException pe){
        	return new CommandResult("ParseException");
        } catch (IllegalValueException ive){
        	return new CommandResult(MESSAGE_INVALID_TAG);
        }
       lastShownList = model.getFilteredTaskList();
        ReadOnlyTask updatedTask = lastShownList.get(targetIndex - 1);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedTask));
    }
}

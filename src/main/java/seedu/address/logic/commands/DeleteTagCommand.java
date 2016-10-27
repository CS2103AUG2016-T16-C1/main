package seedu.address.logic.commands;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.UniqueTaskList.TaskNotFoundException;
import seedu.address.model.tag.Tag;
//@@author A0135787N-reused
public class DeleteTagCommand extends Command {
    public static final String COMMAND_WORD = "deltag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": deletes a tag from a task listed in the task manager. \n"
            + "Parameters: INDEX[MUST BE POSITIVE INTEGER] TAGS[ANY NUMBER OF TAGS SEPARATED BY SPACE] \n"
            + "Example: " + COMMAND_WORD
            + " 1 toughlife easygame";

    public static final String MESSAGE_SUCCESS = "Task tags updated: %1$s";
    public static final String MESSAGE_INVALID_TAG = "Tags must be alphanumerical";
    public static final String MESSAGE_NO_TAG = "No Tag can be found";
    
    private int targetIndex;
    private ArrayList<String> tagsToDel;


    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     * @throws ParseException 
     */
   
	public DeleteTagCommand(String index, String tagsString)
            throws IllegalValueException, ParseException {
    		this.targetIndex = Integer.parseInt(index.trim());
    		tagsToDel = new ArrayList<String>();
    		
    		Scanner sc = new Scanner(tagsString.trim());
    			while(sc.hasNext()){
    				String tagToAdd = sc.next();
    				tagsToDel.add(tagToAdd);
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
        if(tagsToDel.size() == 0)
        	return new CommandResult("No Tags To Delete");
        
        ReadOnlyTask taskToDelTags= lastShownList.get(targetIndex - 1);
        for(String s : tagsToDel){
        	try {
				if(!taskToDelTags.getTags().contains(new Tag(s))){
					return new CommandResult("Task does not have Tag: " + s );
				}
			} catch (IllegalValueException e) {
				
				return new CommandResult("Tag must be alphanumerical");
			}
        	
        }
        
        try {
            model.deleteTags(taskToDelTags, tagsToDel);
        } catch (TaskNotFoundException tnfe) {
            assert false : "The target task cannot be missing";
        } catch (ParseException pe){
        	return new CommandResult("ParseException");
        } catch (IllegalValueException ive){
        	return new CommandResult("Tags must be alphanumerical");
        }
       lastShownList = model.getFilteredTaskList();
        ReadOnlyTask updatedTask = lastShownList.get(targetIndex - 1);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedTask));
    }
}
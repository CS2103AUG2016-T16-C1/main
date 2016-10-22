package seedu.address.logic.commands;


import java.text.ParseException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.History.StateNotFoundException;
import seedu.address.model.TaskManager;

/**
 * Clears the address book.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Previous command has been undone";
    public static final String UNDO_ADD_MESSAGE = "Added Task has been deleted";
	public static final String UNDO_DELETE_MESSAGE = "Removed Task has been re-added";
	public static final String EMPTY_HISTORY_MESSAGE = "No available commands can be undone";
	public static final String UNDO_SORT_MESSAGE = "Sorting has been undone";
	public static final String UNDO_EDIT_MESSAGE = "Edits has been revoked";
	public static final String UNDO_CLEAR_MESSAGE = "Deleted Tasks have been restored";
	public static final String UNDO_DONE_MESSAGE = "Task has been marked undone";
	public static final String UNDO_ADD_TAG_MESSAGE = "Tag/s have been removed";
	public static final String UNDO_DEL_TAG_MESSAGE = "Tag/s have been re-added";
	
	
    public UndoCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        
        if (model.getHistory().isEmpty() ) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(EMPTY_HISTORY_MESSAGE);
        }
        try {
			model.undo();
		} catch (StateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String messageType = model.getHistory().getMessage();
        switch (messageType){
        	case "add":
        		return new CommandResult(UNDO_ADD_MESSAGE);
        	case "clear":
        		return new CommandResult(UNDO_CLEAR_MESSAGE);
        	case "delete":
        		return new CommandResult(UNDO_DELETE_MESSAGE);
        	case "sort" :
        		return new CommandResult(UNDO_SORT_MESSAGE);
        	case "edit" :
        		return new CommandResult(UNDO_EDIT_MESSAGE);
        	case "done" :
        		return new CommandResult(UNDO_DONE_MESSAGE);
        	case "addTag" :
        		return new CommandResult(UNDO_ADD_TAG_MESSAGE);
        	case "deleteTag":
        		return new CommandResult(UNDO_DEL_TAG_MESSAGE);
        	default:
        		return new CommandResult(MESSAGE_SUCCESS);
        
        }
        	
        	
    }
}
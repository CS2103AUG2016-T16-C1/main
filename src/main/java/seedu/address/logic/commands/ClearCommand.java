package seedu.address.logic.commands;

import java.text.ParseException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Task Manager has been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        try {
			model.resetData(TaskManager.getEmptyTaskManager());
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

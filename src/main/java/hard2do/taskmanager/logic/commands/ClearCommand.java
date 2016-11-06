package hard2do.taskmanager.logic.commands;

import java.text.ParseException;
import java.util.Optional;

import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.TaskManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 * Clears the address book.
 */
// @@author A0147989B-reused
public class ClearCommand extends Command {
	
    public static final String COMMAND_WORD = "clear";
    private static final String MESSAGE_SUCCESS = "Task Manager has been cleared!";
    
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

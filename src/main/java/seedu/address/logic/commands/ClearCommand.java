package seedu.address.logic.commands;

import java.text.ParseException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;

/**
 * Clears the address book.
 */
//@@author A0147989B-reused
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    private static final String MESSAGE_SUCCESS = "Task Manager has been cleared!";
    private static final String MESSAGE_CANCEL = "Clear command has been cancelled";
    
    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Clear all tasks");
        alert.setContentText("Action cannot be undone once Hard2Do has been closed! Are you sure you want to clear Hard2Do?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            try {
    			model.resetData(TaskManager.getEmptyTaskManager());
    		} catch (IllegalValueException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        } else {
        	return new CommandResult(MESSAGE_CANCEL);
        	
        }
        
    
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

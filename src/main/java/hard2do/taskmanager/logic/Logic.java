package hard2do.taskmanager.logic;

import java.text.ParseException;

import hard2do.taskmanager.logic.commands.CommandResult;
import hard2do.taskmanager.model.person.ReadOnlyTask;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws ParseException 
     */
    CommandResult execute(String commandText) throws ParseException;

    /** Returns the filtered list of persons */
    ObservableList<ReadOnlyTask> getFilteredTaskList();

}

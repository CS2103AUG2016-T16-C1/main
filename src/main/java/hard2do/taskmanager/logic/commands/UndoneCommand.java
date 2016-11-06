package hard2do.taskmanager.logic.commands;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.core.UnmodifiableObservableList;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.task.*;
import hard2do.taskmanager.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0147989B
public class UndoneCommand extends Command {

    public static final String COMMAND_WORD = "undone";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark the task as not done identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Task marked as done: %1$s";
    
    public final int targetIndex;

    public UndoneCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }
    
    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToUndone = lastShownList.get(targetIndex - 1);

        try {
            model.undoneTask(taskToUndone);
        } catch (TaskNotFoundException tnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToUndone));

    }
    
}

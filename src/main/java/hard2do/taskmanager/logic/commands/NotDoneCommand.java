package hard2do.taskmanager.logic.commands;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.core.UnmodifiableObservableList;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.task.*;
import hard2do.taskmanager.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0147989B
/**
 * Marks as undone a task identified using it's last displayed index from the task manager.
 */
public class NotDoneCommand extends Command {

    public static final String COMMAND_WORD = "notdone";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark the task as not done identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_NOTDONE_TASK_SUCCESS = "Task marked as not done: %1$s";
    
    public final int targetIndex;

    public NotDoneCommand(int targetIndex) {
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

        return new CommandResult(String.format(MESSAGE_NOTDONE_TASK_SUCCESS, taskToUndone));

    }
    
}

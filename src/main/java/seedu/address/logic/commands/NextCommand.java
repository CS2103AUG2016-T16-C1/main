package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.person.UniqueTaskList.TaskNotFoundException;

//@@author A0147989B
public class NextCommand extends Command {

    public static final String COMMAND_WORD = "next";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edit the Date of the task as the nearest next date identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Task edited as next date: %1$s";
    
    public final int targetIndex;

    public NextCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }
    
    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToNext = lastShownList.get(targetIndex - 1);

        try {
            model.nextTask(taskToNext);
        } catch (TaskNotFoundException tnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DONE_TASK_SUCCESS, taskToNext));

    }
    
}

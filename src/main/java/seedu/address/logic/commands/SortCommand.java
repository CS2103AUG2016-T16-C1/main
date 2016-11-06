package seedu.address.logic.commands;

import java.util.Set;

//@@author A0147989B
/**
 * Lists all tasks in task manager in time order.
 * 
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sort the current task list by time.\n "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SORT_TASK_SUCCESS = "Tasks sorted by time.";

    public SortCommand() {
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskListByTime();
        System.out.println("in sort command\n");
        return new CommandResult(MESSAGE_SORT_TASK_SUCCESS);
    }

}

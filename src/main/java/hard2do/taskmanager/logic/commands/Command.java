package hard2do.taskmanager.logic.commands;

import hard2do.taskmanager.commons.core.Config;
import hard2do.taskmanager.commons.core.EventsCenter;
import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.events.ui.IncorrectCommandAttemptedEvent;
import hard2do.taskmanager.model.Model;
import hard2do.taskmanager.storage.Storage;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
    protected Config config;
    protected Storage storage;

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param displaySize used to generate summary
     * @return summary messag	e for tasks displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model, Config config, Storage storage) {
        this.model = model;
        this.config = config;
        this.storage = storage;
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
    }
}

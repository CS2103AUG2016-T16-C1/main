package hard2do.taskmanager.commons.events.ui;

import hard2do.taskmanager.commons.events.BaseEvent;
import hard2do.taskmanager.logic.commands.Command;

/**
 * Indicates an attempt to execute an incorrect command
 */
public class IncorrectCommandAttemptedEvent extends BaseEvent {

    public IncorrectCommandAttemptedEvent(Command command) {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}

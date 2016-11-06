package hard2do.taskmanager.logic.commands;

import java.util.Set;

import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.tag.Tag;

/**
 * Finds and lists all tasks in task manager whose content contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
//@@author A0135787N
public class FindTagCommand extends Command {

    public static final String COMMAND_WORD = "findtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose tags contain any of "
            + "the specified tag (alphanumeric) and displays them as a list with index numbers.\n"
            + "Parameters: NAME_OF_TAG...\n"
            + "Example: " + COMMAND_WORD + " CS2103";

    private final Tag tagToFind;

    public FindTagCommand(String tagname) throws IllegalValueException {
        this.tagToFind = new Tag(tagname);
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(tagToFind);
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }

}

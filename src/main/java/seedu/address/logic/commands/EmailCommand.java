package seedu.address.logic.commands;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import com.google.api.services.gmail.Gmail;

import seedu.address.commons.core.GmailService;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Content;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.Task;
import seedu.address.model.person.TaskDate;
import seedu.address.model.person.TaskTime;
import seedu.address.model.person.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.tag.UniqueTagList;
//@@author A0141054W

public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": adds unread email as tasks to Hard2Do. \n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Unread Email added to Hard2Do";
    
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in Hard2Do. Try to mark read those emails that you have added before!";
    
    public static final String MESSAGE_NO_CONNECTION = "Cannot connect to gmail server";

    private static final String MESSAGE_NO_UNREAD_EMAIL = "There is currently no unread email";

    public EmailCommand() {
    }

    @Override
    public CommandResult execute() {
        List<String> unreadMessages;
        Gmail service = null;
        String user = "me";
        String query = "is:unread";
        ReadOnlyTask toAdd = null;
        
        try {
            service = GmailService.getGmailService();
        } catch (IOException e) {
            return new CommandResult(MESSAGE_NO_CONNECTION);
        }

        try {
            unreadMessages = GmailService.listSubjectsMatchingQuery(service, user, query);
        } catch (IOException e) {
            return new CommandResult(MESSAGE_NO_CONNECTION);
        }

        if (unreadMessages.isEmpty())
            return new CommandResult(MESSAGE_NO_UNREAD_EMAIL);
        else {
            assert model != null;
            for (String message : unreadMessages) {
                try {
                    toAdd = new Task(
                            new Content(message),
                            new TaskDate(),
                            new TaskTime(),
                            new UniqueTagList(new HashSet<>()));
                } catch (IllegalValueException e) {
                }
                try {
                    model.addTask(toAdd);
                } catch (DuplicateTaskException e) {
                    return new CommandResult(MESSAGE_DUPLICATE_TASK);
                }
            }
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }

}

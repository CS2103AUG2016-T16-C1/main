package hard2do.taskmanager.logic.commands;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import com.google.api.services.gmail.Gmail;

import hard2do.taskmanager.commons.core.GmailService;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.person.Content;
import hard2do.taskmanager.model.person.ReadOnlyTask;
import hard2do.taskmanager.model.person.Task;
import hard2do.taskmanager.model.person.TaskDate;
import hard2do.taskmanager.model.person.TaskTime;
import hard2do.taskmanager.model.person.UniqueTaskList.DuplicateTaskException;
import hard2do.taskmanager.model.tag.UniqueTagList;

public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": adds unread email as tasks to Hard2Do. \n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Unread Email added to Hard2Do";
    
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in Hard2Do. Try to mark read those emails that you have added before!";
    
    public static final String MESSAGE_NO_CONNECTION = "Cannot connect to gmail server";

    private static final String MESSAGE_NO_UNREAD_EMAIL = "There is currently no unread email";
    
    private String email;
    
    public EmailCommand(String email) {
        this.email = email;
    }

    @Override
    public CommandResult execute() {
        List<String> unreadMessages;
        Gmail service = null;
        String user = "me";
        String query;
        if (email != "") 
            query = "from:" + email + " is:unread";
        else
            query = "is:unread";
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

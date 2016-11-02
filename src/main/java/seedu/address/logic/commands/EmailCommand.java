package seedu.address.logic.commands;

import java.io.IOException;
import java.util.List;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import seedu.address.commons.core.GmailService;

public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": adds unread email as tasks to Hard2Do. \n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Task updated with unread emails: %1$s";

    private static final String MESSAGE_NO_UNREAD_EMAIL = "There is currently no unread email";

    List<Message> unreadMessages;
    
    public EmailCommand() {}

    @Override
    public CommandResult execute() {
        Gmail service = null;        
        String user = "me";
        String query = "is:unread";
        
        try {
            service = GmailService.getGmailService();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        try {
            unreadMessages = GmailService.listMessagesMatchingQuery(service, user, query);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (unreadMessages.isEmpty())
            return new CommandResult(MESSAGE_NO_UNREAD_EMAIL);
        else {
            assert model != null;
            for (Message message:unreadMessages) {
                System.out.println(message.toPrettyString());
            }
        }
        
        

        return null;
    }

}

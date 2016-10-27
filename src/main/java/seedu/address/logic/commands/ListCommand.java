package seedu.address.logic.commands;


/**
 * Lists all persons in the address book to the user.
 */
//@@author A0141054W
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS_ALL = "Listed all tasks";
    
    public static final String MESSAGE_SUCCESS_DONE = "Listed all done tasks";
    
    public static final String MESSAGE_SUCCESS_UNDONE = "Listed all undone tasks";
  
    public static final String MESSAGE_LIST_RESTRICTION = "Hard2Do can only list all, list done all list undone";
    
    private String listModification;
    
    public ListCommand(String args) {
        this.listModification = args;
    }

    @Override
    public CommandResult execute() {
        if (listModification.compareTo("done") == 0) {
            model.updateFilteredListToShowDone();
            return new CommandResult(MESSAGE_SUCCESS_DONE);
        }
        else if (listModification.compareTo("undone") == 0) {
            model.updateFilteredListToShowUndone();
            return new CommandResult(MESSAGE_SUCCESS_UNDONE);
        }
        else {
            model.updateFilteredListToShowAll();
            return new CommandResult(MESSAGE_SUCCESS_ALL);
        }
    }
}

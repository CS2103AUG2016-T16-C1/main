package seedu.address.logic.commands;

//@@author A0141054W
/**
 * Lists all task in the task manager to the user.
 * optional to list done, undone, important, unimportant
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS_ALL = "Listed all tasks";
    
    public static final String MESSAGE_SUCCESS_DONE = "Listed all done tasks";
    
    public static final String MESSAGE_SUCCESS_UNDONE = "Listed all undone tasks";
  
    public static final String MESSAGE_LIST_RESTRICTION = "Hard2Do can only list all, list done all list undone";
    
    public static final String MESSAGE_SUCCESS_IMPORTANT = "Listed all important tasks";
    
    public static final String MESSAGE_SUCCESS_UNIMPORTANT = "Listed all unimportant tasks";
    
    private String listModification;
    
    public ListCommand(String args) {
        this.listModification = args.trim().toLowerCase();
    }

    @Override
    public CommandResult execute() {
        if (listModification.equals("done") || listModification.equals("-d")) {
            model.updateFilteredListToShowDone();
            return new CommandResult(MESSAGE_SUCCESS_DONE);
        }
        else if (listModification.equals("all") || listModification.equals("-a")) {
            model.updateFilteredListToShowAll();
            return new CommandResult(MESSAGE_SUCCESS_ALL);
        }
        else if (listModification.equals("important") || listModification.equals("-i")){
        	model.updateFilteredListToShowImportant();
            return new CommandResult(MESSAGE_SUCCESS_IMPORTANT);
        }
        else if (listModification.equals("unimportant") || listModification.equals("-ui")){
        	model.updateFilteredListToShowUnimportant();
            return new CommandResult(MESSAGE_SUCCESS_UNIMPORTANT);
        }
        else if (listModification.equals("undone") || listModification.equals("-ud")){
        	model.updateFilteredListToShowUndone();
            return new CommandResult(MESSAGE_SUCCESS_UNDONE);
        }
        else {
            model.updateFilteredListToShowUndone();
            return new CommandResult(MESSAGE_SUCCESS_UNDONE);
        }
    }
}

package seedu.address.logic.commands;

public class LoadCommand extends Command {
    
    public static final String COMMAND_WORD = "load";

    public static final String MESSAGE_SUCCESS = "New storage file loaded. Please restart Hard2Do to apply results!!!";

    public static final String MESSAGE_INVALID_FILEPATH = "The file path entered is not valid!";

    public static final String MESSAGE_DIRECTORY_FILEPATH = "The file path entered is a directory. Please enter a file!";

    private String taskManagerFilePath;
    
    public LoadCommand(String taskManagerFilePath) {
        this.taskManagerFilePath = taskManagerFilePath;
    }
    
    @Override
    public CommandResult execute() {
        assert config != null;
        
        config.setTaskManagerFilePath(taskManagerFilePath);
        System.out.println(config.getTaskManagerFilePath());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}

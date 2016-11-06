package seedu.address.logic.commands;

import java.io.IOException;

import seedu.address.commons.util.ConfigUtil;

//@@author A0141054W
/**
 * Load data from a specific file
 */
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
        try {
            ConfigUtil.saveConfig(config, config.DEFAULT_CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        	return new CommandResult(MESSAGE_INVALID_FILEPATH);
        }
        
        return new CommandResult(MESSAGE_SUCCESS);
    }

}

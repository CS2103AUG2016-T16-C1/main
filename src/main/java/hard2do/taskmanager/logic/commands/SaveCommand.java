package hard2do.taskmanager.logic.commands;

import java.io.IOException;

/**
 * Saves data to a specific file
 */
public class SaveCommand extends Command {

	public static final String COMMAND_WORD = "save";
	
	public static final String MESSAGE_SUCCESS = "File has been saved successfully";
	
	public static final String MESSAGE_INVALID_FILEPATH = "The file path entered is not valid";
	
	 public static final String MESSAGE_DIRECTORY_FILEPATH = "The file path entered is a directory. Please enter a file!";
	
	 public static final String MESSAGE_INVALID_FILENAME = "The file name entered is not valid";
	 
	private String taskManagerFilePath;
	
	public SaveCommand(String taskManagerFilePath) {
		this.taskManagerFilePath = taskManagerFilePath;
	}
	
	@Override
	public CommandResult execute() {
		assert config != null;
		
		if(!taskManagerFilePath.contains(".xml")) {
			return new CommandResult(MESSAGE_INVALID_FILENAME);
	}
		try {
			storage.saveTaskManager(model.getTaskManager(), this.taskManagerFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			return new CommandResult(MESSAGE_INVALID_FILEPATH);
		}
		return new CommandResult(MESSAGE_SUCCESS);
	}
}


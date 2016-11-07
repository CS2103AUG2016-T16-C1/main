package hard2do.taskmanager.logic.commands;

import java.io.IOException;

import hard2do.taskmanager.commons.util.ConfigUtil;

//@@author A0141054W
/**
 * Loads data from a specific file
 */
public class LoadCommand extends Command {

	public static final String COMMAND_WORD = "load";

	public static final String MESSAGE_SUCCESS = "New storage file loaded. Please restart Hard2Do to apply results!!!";

	public static final String MESSAGE_INVALID_FILEPATH = "The file path entered is not valid!";

	public static final String MESSAGE_DIRECTORY_FILEPATH = "The file path entered is a directory. Please enter a file!";

	public static final String MESSAGE_INVALID_FILENAME = "The file name entered is not valid";

	private String taskManagerFilePath;

	public LoadCommand(String taskManagerFilePath) {
		this.taskManagerFilePath = taskManagerFilePath;
	}

	@Override
	public CommandResult execute() {
		assert config != null;

		if (!taskManagerFilePath.contains(".xml")) {
			return new CommandResult(MESSAGE_INVALID_FILENAME);
		}

		if (!taskManagerFilePath.contains("/")) {
			return new CommandResult(MESSAGE_INVALID_FILEPATH);
		}

		config.setTaskManagerFilePath(taskManagerFilePath);
		try {
			ConfigUtil.saveConfig(config, config.DEFAULT_CONFIG_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new CommandResult(MESSAGE_SUCCESS);
	}

}

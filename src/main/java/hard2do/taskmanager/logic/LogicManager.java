package hard2do.taskmanager.logic;

import javafx.collections.ObservableList;

import java.text.ParseException;
import java.util.logging.Logger;

import hard2do.taskmanager.commons.core.ComponentManager;
import hard2do.taskmanager.commons.core.Config;
import hard2do.taskmanager.commons.core.LogsCenter;
import hard2do.taskmanager.logic.commands.Command;
import hard2do.taskmanager.logic.commands.CommandResult;
import hard2do.taskmanager.logic.parser.Parser;
import hard2do.taskmanager.model.Model;
import hard2do.taskmanager.model.task.ReadOnlyTask;
import hard2do.taskmanager.storage.Storage;

/**
 * The main LogicManager of the app.
 */
//@@author A0139523E-reused
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    private final Config config;
    private final Storage storage;

    public LogicManager(Model model, Storage storage, Config config) {
        this.model = model;
        this.parser = new Parser();
        this.config = config;
        this.storage = storage;
    }

    @Override
    public CommandResult execute(String commandText) throws ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model, config, storage);
        return command.execute();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }
}

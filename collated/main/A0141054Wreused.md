# A0141054Wreused
###### /java/hard2do/taskmanager/MainApp.java
``` java
    protected UserPrefs initPrefs(Config config) {
        assert config != null;

        String prefsFilePath = config.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. " +
                    "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty TaskManager");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting TaskManager " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Task Manager ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```
###### /java/hard2do/taskmanager/model/ModelManager.java
``` java
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private TaskManager taskManager;
    private FilteredList<ReadOnlyTask> filteredTasks;

    /**
     * Initializes a ModelManager with the given TaskManager
     * TaskManager and its variables should not be null
     */
    public ModelManager(TaskManager src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with task manager: " + src + " and user prefs " + userPrefs);

        taskManager = new TaskManager(src);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
    }

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
    }

    @Override
    public void resetData(ReadOnlyTaskManager newData) throws IllegalValueException, ParseException {
    	try {
			taskManager.save("clear");
		} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
        taskManager.resetData(newData);
        indicateTaskManagerChanged();
    }

    @Override
    public ReadOnlyTaskManager getTaskManager() {
        return taskManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskManagerChanged() {
        raise(new TaskManagerChangedEvent(taskManager));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
    	try {
			taskManager.save("delete");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.removeTask(target);
        indicateTaskManagerChanged();
    }

    @Override
    public synchronized void addTask(ReadOnlyTask task) throws UniqueTaskList.DuplicateTaskException {
    	try {
			taskManager.save("add");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.addTask(task);
    	updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }

    @Override
    public synchronized void editTask(ReadOnlyTask target, String newDate, String newEndDate, 
    		String newTime, String newEndTime, String newContent)
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	try {
			taskManager.save("edit");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.editTask(target, newDate, newEndDate, newTime, newEndTime, newContent);
        indicateTaskManagerChanged();

    }


    @Override
    public synchronized void addTags(ReadOnlyTask target, ArrayList<String> newTags)
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	try {
			taskManager.save("addTag");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.addTags(target, newTags);

        updateFilteredListToShowUndone();
    	indicateTaskManagerChanged();
    }

    @Override
    public synchronized void deleteTags(ReadOnlyTask target, ArrayList<String> tagsToDelete)
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	assert !(tagsToDelete.size() == 0);
    	try {
			taskManager.save("delTag");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.deleteTags(target, tagsToDelete);

        updateFilteredListToShowUndone();
    	indicateTaskManagerChanged();
    }
    
    
```
###### /java/hard2do/taskmanager/model/task/UniqueTaskList.java
``` java
public class UniqueTaskList implements Iterable<ReadOnlyTask> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate tasks");
        }
    }

    /**
     * Signals that an operation targeting a specified person in the list would fail because
     * there is no such matching person in the list.
     */
    public static class TaskNotFoundException extends Exception {}
    
    
    private ObservableList<ReadOnlyTask> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TaskList.
     */
    public UniqueTaskList() {}

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(ReadOnlyTask toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(toAdd);
    }
    /**
     * Edits a task in the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     * @throws ParseException 
     * @throws IllegalValueException 
     */
    public boolean edit(ReadOnlyTask taskToEdit, String newDate, String newEndDate, String newTime, String newEndTime, String newContent) 
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	assert taskToEdit != null;
        
        if(newDate != null){
        	taskToEdit.setDate(newDate);
        }
        
        if(newEndDate != null) {
        	taskToEdit.setEndDate(newEndDate);
        }
        
        if(newTime != null){
        	taskToEdit.setTime(newTime);
        }
        
        if(newEndTime != null){
        	taskToEdit.setEndTime(newEndTime);
        }
        
        if(newContent != null)
        	taskToEdit.setContent(newContent);
        return true;

    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }
    
    /**
     * Add tags to the equivalent task.
     * 
     * @throws TaskNotFoundException if no such task could be found in the list.
     * @throws IllegalValueException if tags are not alphanumerical.
     * @throws DuplicateTagException if task already has similar tag.
     */
    
    public boolean addTags(ReadOnlyTask target, ArrayList<String> tagsToAdd) 
    		throws DuplicateTagException, IllegalValueException, TaskNotFoundException{
    	assert target != null;
    	final boolean tagsAddedToTask = target.addTags(tagsToAdd);
    	if (!tagsAddedToTask) {
            throw new TaskNotFoundException();
        }
        return tagsAddedToTask;
    	
    }
    
    /**
     * Delete tags from the equivalent task.
     * 
     * @throws TaskNotFoundException if no such task could be found in the list.
     * @throws IllegalValueException if tags are not alphanumerical.
     * @throws DuplicateTagException if task already has similar tag.
     */
    
    public boolean deleteTags(ReadOnlyTask target, ArrayList<String> tagsToDel) 
    		throws DuplicateTagException, IllegalValueException, TaskNotFoundException{
    	assert target != null;
    	final boolean tagsDeletedFromTask = target.deleteTags(tagsToDel);
    	if (!tagsDeletedFromTask) {
            throw new TaskNotFoundException();
        }
        return tagsDeletedFromTask;
    	
    }
    
    
    /**
     * Fetch the next date of the task.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean next(ReadOnlyTask toNext) throws TaskNotFoundException {
        assert toNext != null;
        final boolean taskFoundAndMarked = toNext.setNext();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    
    /**
     * Mark the equivalent task as done.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean done(ReadOnlyTask toDone) throws TaskNotFoundException {
        assert toDone != null;
        final boolean taskFoundAndMarked = toDone.setDone();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    /**
     * Mark the equivalent task as undone.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean undone(ReadOnlyTask toUnDone) throws TaskNotFoundException {
        assert toUnDone != null;
        final boolean taskFoundAndMarked = toUnDone.setUndone();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    /**
     * Mark the equivalent task as important.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean important(ReadOnlyTask toImportant) throws TaskNotFoundException {
        assert toImportant != null;
        final boolean taskFoundAndMarked = toImportant.setImportant();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    /**
     * Mark the equivalent task as unimportant.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean unimportant(ReadOnlyTask toUnimportant) throws TaskNotFoundException {
        assert toUnimportant != null;
        final boolean taskFoundAndMarked = toUnimportant.setUnimportant();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    
   
    public ObservableList<ReadOnlyTask> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<ReadOnlyTask> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### /java/hard2do/taskmanager/model/tag/Tag.java
``` java
public class Tag {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";

    public String tagName;

    public Tag() {
    }

    /**
     * Validates given tag name.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String name) throws IllegalValueException {
        assert name != null;
        name = name.trim();
        if (!isValidTagName(name)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = name;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
```
###### /java/hard2do/taskmanager/ui/MainWindow.java
``` java
public class MainWindow extends UiPart {

    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 450;

    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private TaskDetail taskDetail;
    private TaskListPanel taskListPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private CommandBox commandBox;
    private Config config;
    private UserPrefs userPrefs;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    private String taskManagerName;

    @FXML
    private AnchorPane browserPlaceholder;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane taskListPanelPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;


    public MainWindow() {
        super();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(config.getAppTitle(), config.getTaskManagerName(), config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String taskManagerName, Config config, UserPrefs prefs,
                           Logic logic) {

        //Set dependencies
        this.logic = logic;
        this.taskManagerName = taskManagerName;
        this.config = config;
        this.userPrefs = prefs;

        //Configure the UI
        setTitle(appTitle);
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        setAccelerators();
    }

    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
    }

    void fillInnerParts() {
        taskListPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getFilteredTaskList(), logic);
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        taskDetail = TaskDetail.load(primaryStage, browserPlaceholder, resultDisplay, logic);
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getTaskManagerFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = HelpWindow.load(primaryStage);
        helpWindow.show();
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public TaskListPanel getTaskListPanel() {
        return this.taskListPanel;
    }
    
    public TaskDetail getTaskDetail() {
        return this.taskDetail;
    }

    public void loadTaskDetail(ReadOnlyTask task, int index) {
        taskDetail.loadTaskDetail(task, index);
    }

    public void releaseResources() {
    }
}
```
###### /java/hard2do/taskmanager/ui/TaskListPanel.java
``` java
public class TaskListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private static final String FXML = "TaskListPanel.fxml";
    private Logic logic;

    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<ReadOnlyTask> taskListView;

    public TaskListPanel() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static TaskListPanel load(Stage primaryStage, AnchorPane personListPlaceholder,
                                       ObservableList<ReadOnlyTask> taskList, Logic logic) {
        TaskListPanel taskListPanel =
                UiPartLoader.loadUiPart(primaryStage, personListPlaceholder, new TaskListPanel());
        taskListPanel.logic = logic;
        taskListPanel.configure(taskList);
        return taskListPanel;
    }

    private void configure(ObservableList<ReadOnlyTask> taskList) {
        setConnections(taskList);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<ReadOnlyTask> taskList) {
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue, taskListView.getSelectionModel().getSelectedIndex()));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class TaskListViewCell extends ListCell<ReadOnlyTask> {

        public TaskListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(TaskCard.load(task, getIndex() + 1, logic).getLayout());
            }
        }
    }

}
```
###### /java/hard2do/taskmanager/ui/UiManager.java
``` java
public class UiManager extends ComponentManager implements Ui {
    private static final Logger logger = LogsCenter.getLogger(UiManager.class);
    private static final String ICON_APPLICATION = "/images/address_book_32.png";

    private Logic logic;
    private Config config;
    private UserPrefs prefs;
    private MainWindow mainWindow;

    public UiManager(Logic logic, Config config, UserPrefs prefs) {
        super();
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");
        primaryStage.setTitle(config.getAppTitle());

        //Set the application icon.
        primaryStage.getIcons().add(getImage(ICON_APPLICATION));

        try {
            mainWindow = MainWindow.load(primaryStage, config, prefs, logic);
            mainWindow.show(); //This should be called before creating other UI parts
            mainWindow.fillInnerParts();

        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    @Override
    public void stop() {
        prefs.updateLastUsedGuiSetting(mainWindow.getCurrentGuiSetting());
        mainWindow.hide();
        mainWindow.releaseResources();
    }

    private void showFileOperationAlertAndWait(String description, String details, Throwable cause) {
        final String content = details + ":\n" + cause.toString();
        showAlertDialogAndWait(AlertType.ERROR, "File Op Error", description, content);
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(mainWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    //==================== Event Handling Code =================================================================

    @Subscribe
    private void handleDataSavingExceptionEvent(DataSavingExceptionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showFileOperationAlertAndWait("Could not save data", "Could not save data to file", event.exception);
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.handleHelp();
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.getTaskListPanel().scrollTo(event.targetIndex);
    }

    @Subscribe
    private void handlePanelSelectionChangedEvent(TaskPanelSelectionChangedEvent event){
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.loadTaskDetail(event.getNewSelection(), event.getNewIndex());
    }
    
```
###### /java/hard2do/taskmanager/ui/CommandBox.java
``` java
/**
 * CommandBox is in charge of prompting the user of the commands (auto complete)
 * and receive commands, passing it to Logic to handle
 */
public class CommandBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private ResultDisplay resultDisplay;
    String previousCommandTest;

    private Logic logic;

    @FXML
    private TextField commandTextField;
    private CommandResult mostRecentResult;

    public static CommandBox load(Stage primaryStage, AnchorPane commandBoxPlaceholder, ResultDisplay resultDisplay,
            Logic logic) {
        CommandBox commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandBox());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }

```
###### /java/hard2do/taskmanager/storage/TaskManagerStorage.java
``` java
public interface TaskManagerStorage {

    /**
     * Returns the file path of the data file.
     */
    String getTaskManagerFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyTaskManager}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException;

    /**
     * @see #getTaskManagerFilePath()
     */
    Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskManager} to the storage.
     * @param TaskManager cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException;

    /**
     * @see #saveTaskManager(ReadOnlyTaskManager)
     */
    void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException;

}
```
###### /java/hard2do/taskmanager/storage/XmlTaskManagerStorage.java
``` java
public class XmlTaskManagerStorage implements TaskManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskManagerStorage.class);

    private String filePath;

    public XmlTaskManagerStorage(String filePath){
        this.filePath = filePath;
    }

    public String getTaskManagerFilePath(){
        return filePath;
    }

    /**
     * Similar to {@link #readTaskManager()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;
              
        File taskManagerFile = new File(filePath);

        if (!taskManagerFile.exists()) {
            logger.info("TaskManager file "  + taskManagerFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskManager taskManagerOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(taskManagerOptional);
    }

    /**
     * Similar to {@link #saveTaskManager(ReadOnlyTaskManager)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
        assert taskManager != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskManager(taskManager));
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException {
        return readTaskManager(filePath);
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException {
        saveTaskManager(taskManager, filePath);
    }
}
```
###### /java/hard2do/taskmanager/commons/core/UnmodifiableObservableList.java
``` java
public class UnmodifiableObservableList<E> implements ObservableList<E> {

    public static final String MUTATION_OP_EXCEPTION_MESSAGE = "Attempted to modify an unmodifiable view";

    private final ObservableList<? extends E> backingList;

    public UnmodifiableObservableList(ObservableList<? extends E> backingList) {
        if (backingList == null) {
            throw new NullPointerException();
        }
        this.backingList = backingList;
    }
    
    @Override
    public final void addListener(ListChangeListener<? super E> listener) {
        backingList.addListener(listener);
    }

    @Override
    public final void removeListener(ListChangeListener<? super E> listener) {
        backingList.removeListener(listener);
    }

    @Override
    public final void addListener(InvalidationListener listener) {
        backingList.addListener(listener);
    }

    @Override
    public final void removeListener(InvalidationListener listener) {
        backingList.removeListener(listener);
    }

    @Override
    public final boolean addAll(Object... elements) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final boolean setAll(Object... elements) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final boolean setAll(Collection<? extends E> col) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }
    
    @Override
    public final boolean removeAll(Object... elements) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }
    
    @Override
    public final boolean retainAll(Object... elements) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final void remove(int from, int to) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }


    @Override
    public final FilteredList<E> filtered(Predicate<E> predicate) {
        return new FilteredList<>(this, predicate);
    }

    @Override
    public final SortedList<E> sorted(Comparator<E> comparator) {
        return new SortedList<>(this, comparator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final SortedList<E> sorted() {
        return sorted(Comparator.nullsFirst((o1, o2) -> {
            if (o1 instanceof Comparable) {
                return ((Comparable) o1).compareTo(o2);
            }
            return Collator.getInstance().compare(o1.toString(), o2.toString());
        }));
    }

    @Override
    public final int size() {
        return backingList.size();
    }

    @Override
    public final boolean isEmpty() {
        return backingList.isEmpty();
    }
    
    @Override
    public final boolean contains(Object o) {
        return backingList.contains(o);
    }
    
    @Override
    public final Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<? extends E> i = backingList.iterator();

            public final boolean hasNext() {
                return i.hasNext();
            }
            public final E next() {
                return i.next();
            }
            public final void remove() {
                throw new UnsupportedOperationException();
            }
            @Override
            public final void forEachRemaining(Consumer<? super E> action) {
                // Use backing collection version
                i.forEachRemaining(action);
            }
        };
    }

    @Override
    public final Object[] toArray() {
        return backingList.toArray();
    }

    @Override
    public final <T> T[] toArray(T[] a) {
        return backingList.toArray(a);
    }

    @Override
    public final boolean add(E o) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }
    
    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return backingList.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }
    
    @Override
    public final boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }
    
    @Override
    public final boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }
    
    @Override
    public final void clear() {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    
    @Override
    public final boolean equals(Object o) {
        return o == this || backingList.equals(o);
    }

    @Override
    public final int hashCode() {
        return backingList.hashCode();
    }

    
    @Override
    public final E get(int index) {
        return backingList.get(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Object set(int index, Object element) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final void add(int index, Object element) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @Override
    public final E remove(int index) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }
    
    @Override
    public final int indexOf(Object o) {
        return backingList.indexOf(o);
    }
    
    @Override
    public final int lastIndexOf(Object o) {
        return backingList.lastIndexOf(o);
    }

    @Override
    public final ListIterator<E> listIterator() {
        return listIterator(0);
    }
    
    @Override
    public final ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            private final ListIterator<? extends E> i = backingList.listIterator(index);

            public final boolean hasNext() {
                return i.hasNext();
            }
            public final E next() {
                return i.next();
            }
            public final boolean hasPrevious() {
                return i.hasPrevious();
            }
            public final E previous() {
                return i.previous();
            }
            public final int nextIndex() {
                return i.nextIndex();
            }
            public final int previousIndex() {
                return i.previousIndex();
            }

            public final void remove() {
                throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
            }
            public final void set(E e) {
                throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
            }
            public final void add(E e) {
                throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
            }

            @Override
            public final void forEachRemaining(Consumer<? super E> action) {
                i.forEachRemaining(action);
            }
        };
    }

    @Override
    public final List<E> subList(int fromIndex, int toIndex) {
        return Collections.unmodifiableList(backingList.subList(fromIndex, toIndex));
    }

    @Override
    public final boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException(MUTATION_OP_EXCEPTION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Stream<E> stream() {
        return (Stream<E>) backingList.stream();
    }
    
    @Override
    public final void forEach(Consumer<? super E> action) {
        backingList.forEach(action);
    }

}
```
###### /java/hard2do/taskmanager/commons/core/LogsCenter.java
``` java
public class LogsCenter {
    private static final int MAX_FILE_COUNT = 5;
    private static final int MAX_FILE_SIZE_IN_BYTES = (int) (Math.pow(2, 20) * 5); // 5MB
    private static final String LOG_FILE = "taskmanager.log";
    private static Level currentLogLevel = Level.INFO;
    private static final Logger logger = LogsCenter.getLogger(LogsCenter.class);
    private static FileHandler fileHandler;
    private static ConsoleHandler consoleHandler;

    /**
     * Initializes with a custom log level (specified in the {@code config} object)
     * Loggers obtained *AFTER* this initialization will have their logging level changed<br>
     * Logging levels for existing loggers will only be updated if the logger with the same name is requested again
     * from the LogsCenter.
     */
    public static void init(Config config) {
        currentLogLevel = config.getLogLevel();
        logger.info("currentLogLevel: " + currentLogLevel);
    }

    /**
     * Creates a logger with the given name the given name.
     */
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        removeHandlers(logger);
        addConsoleHandler(logger);
        addFileHandler(logger);

        return Logger.getLogger(name);
    }

    private static void addConsoleHandler(Logger logger) {
        if (consoleHandler == null) consoleHandler = createConsoleHandler();
        logger.addHandler(consoleHandler);
    }

    private static void removeHandlers(Logger logger) {
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            logger.removeHandler(handler);
        }
    }

    private static void addFileHandler(Logger logger) {
        try {
            if (fileHandler == null) fileHandler = createFileHandler();
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.warning("Error adding file handler for logger.");
        }
    }

    private static FileHandler createFileHandler() throws IOException {
        FileHandler fileHandler = new FileHandler(LOG_FILE, MAX_FILE_SIZE_IN_BYTES, MAX_FILE_COUNT, true);
        fileHandler.setFormatter(new SimpleFormatter());
        fileHandler.setLevel(currentLogLevel);
        return fileHandler;
    }

    private static ConsoleHandler createConsoleHandler() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(currentLogLevel);
        return consoleHandler;
    }

    /**
     * Creates a Logger for the given class name.
     */
    public static <T> Logger getLogger(Class<T> clazz) {
        if (clazz == null) return Logger.getLogger("");
        return getLogger(clazz.getSimpleName());
    }

    /**
     * Decorates the given string to create a log message suitable for logging event handling methods.
     */
    public static String getEventHandlingLogMessage(BaseEvent e, String message) {
        return "---[Event handled][" + e + "]" + message;
    }

    /**
     * @see #getEventHandlingLogMessage(BaseEvent, String)
     */
    public static String getEventHandlingLogMessage(BaseEvent e) {
        return getEventHandlingLogMessage(e,"");
    }
}
```
###### /java/hard2do/taskmanager/commons/events/ui/TaskPanelSelectionChangedEvent.java
``` java
public class TaskPanelSelectionChangedEvent extends BaseEvent {


    private final ReadOnlyTask newSelection;
    private final int newIndex;

    public TaskPanelSelectionChangedEvent(ReadOnlyTask newSelection, int newIndex){
        this.newSelection = newSelection;
        this.newIndex = newIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ReadOnlyTask getNewSelection() {
        return newSelection;
    }

    public int getNewIndex() {
        return newIndex;
    }
}
```
###### /java/hard2do/taskmanager/logic/commands/SelectCommand.java
``` java
/**
 * Selects a task identified using it's last displayed index from the task manager.
 */
public class SelectCommand extends Command {

    public final int targetIndex;

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_TASK_SUCCESS = "Selected Task: %1$s";

    public SelectCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex - 1));
        return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex));

    }

}
```
###### /resources/view/MainWindow.fxml
``` fxml
<VBox maxHeight="Infinity" maxWidth="Infinity" minHeight="-Infinity" minWidth="-Infinity" xmlns="http://javafx.com/javafx/8.0.65" xmlns:fx="http://javafx.com/fxml/1" fx:controller="hard2do.taskmanager.ui.MainWindow">
   <stylesheets>
      <URL value="@DarkTheme.css" />
      <URL value="@Extensions.css" />
   </stylesheets>
   <children>
      <MenuBar VBox.vgrow="NEVER">
         <menus>
            <Menu mnemonicParsing="false" text="File">
               <items>
                  <MenuItem mnemonicParsing="false" onAction="#handleExit" text="Exit" />
               </items>
            </Menu>
            <Menu mnemonicParsing="false" text="Help">
               <items>
                  <MenuItem fx:id="helpMenuItem" mnemonicParsing="false" onAction="#handleHelp" text="Help" />
               </items>
            </Menu>
         </menus>
      </MenuBar>
      <AnchorPane fx:id="commandBoxPlaceholder" styleClass="anchor-pane-with-border" VBox.vgrow="NEVER">
         <padding>
            <Insets bottom="5.0" left="10.0" right="10.0" top="5.0" />
         </padding>
      </AnchorPane>
       <AnchorPane fx:id="resultDisplayPlaceholder" maxHeight="100" minHeight="100" prefHeight="100" styleClass="anchor-pane-with-border" VBox.vgrow="NEVER">
           <padding>
               <Insets bottom="5.0" left="10.0" right="10.0" top="5.0" />
           </padding>
       </AnchorPane>
      <SplitPane id="splitPane" fx:id="splitPane" dividerPositions="0.4" VBox.vgrow="ALWAYS">
         <items>
            <VBox fx:id="personList" minWidth="340" prefWidth="340">
                <padding>
                    <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
                </padding>
               <children>
                  <AnchorPane fx:id="taskListPanelPlaceholder" VBox.vgrow="ALWAYS" />
               </children>
            </VBox>
            <AnchorPane fx:id="browserPlaceholder" prefWidth="340">
                <padding>
                    <Insets bottom="10" left="10" right="10" top="10" />
                </padding>
            </AnchorPane>
         </items>
      </SplitPane>
      <AnchorPane fx:id="statusbarPlaceholder" VBox.vgrow="NEVER" />
   </children>
</VBox>
```

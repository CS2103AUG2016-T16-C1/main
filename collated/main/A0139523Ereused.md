# A0139523Ereused
###### /java/seedu/address/MainApp.java
``` java
public class MainApp extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(1, 0, 0, true);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;

    public MainApp() {}

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing TaskManager ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));
        storage = new StorageManager(config.getTaskManagerFilePath(), config.getUserPrefsFilePath());

        userPrefs = initPrefs(config);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage, config);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();
    }
```
###### /java/seedu/address/model/ReadOnlyTaskManager.java
``` java
public interface ReadOnlyTaskManager {

    UniqueTagList getUniqueTagList();

    UniqueTaskList getUniqueTaskList();

    /**
     * Returns an unmodifiable view of tasks list
     */
    List<ReadOnlyTask> getTaskList();

    /**
     * Returns an unmodifiable view of tags list
     */
    List<Tag> getTagList();

}

```
###### /java/seedu/address/model/Model.java
``` java
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. 
     * @throws ParseException 
     * @throws IllegalValueException */
    void resetData(ReadOnlyTaskManager newData) throws IllegalValueException, ParseException;

    /** Returns the TaskManager */
    ReadOnlyTaskManager getTaskManager();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);
    
    /** Updates the filter of the filtered task list to filter by the closest edit distance to given string input*/
    void updateFilteredTaskList(String toFind);
    
    /** Updates the filter of the filtered task list to filter by the given Tag*/
    void updateFilteredTaskList(Tag tagToFind);
    
    /** Edit the given task. 
     * @throws ParseException */
	void editTask(int targetIndex, String newDate, String newEndDate, String newTime, String newEndTime, String newContent) 
			throws TaskNotFoundException, ParseException;

```
###### /java/seedu/address/model/TaskManager.java
``` java
public class TaskManager implements ReadOnlyTaskManager {

    private UniqueTaskList tasks;
    private UniqueTagList tags;
    private History history;

    {
        tasks = new UniqueTaskList();
        tags = new UniqueTagList();
        history = new History();
    }

    public TaskManager() {}

    /**
     * Tasks and Tags are copied into this TaskManager 
     */
    public TaskManager(ReadOnlyTaskManager toBeCopied) {
        this(toBeCopied.getUniqueTaskList(), toBeCopied.getUniqueTagList());
    }

    /**
     * Tasks and Tags are copied into this task manager
     */
    public TaskManager(UniqueTaskList tasks, UniqueTagList tags) {
        resetData(tasks.getInternalList(), tags.getInternalList());
    }

    public static ReadOnlyTaskManager getEmptyTaskManager() {
        return new TaskManager();
    }

//// list overwrite operations

    public ObservableList<Task> getTasks() {
        return tasks.getInternalList();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.getInternalList().setAll(tasks);
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.getInternalList().setAll(tags);
    }

    public void resetData(Collection<? extends ReadOnlyTask> newTasks, Collection<Tag> newTags) {
        setTasks(newTasks.stream().map(Task::new).collect(Collectors.toList()));
        setTags(newTags);
    }

    public void resetData(ReadOnlyTaskManager newData) throws IllegalValueException, ParseException {
        resetData(newData.getTaskList(), newData.getTagList());
    }
    
    public void save(String commandType) throws IllegalValueException, ParseException{
    	history.save(tasks.getInternalList(), tags.getInternalList(), commandType);
    } 
    
    public void undo() throws StateNotFoundException{
    	history.undo();
    	setTasks(history.getPreviousTasks());
    	setTags(history.getPreviousTags());
    }
//// task-level operations

    /**
     * Adds a task to Hard2Do.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Task t) throws UniqueTaskList.DuplicateTaskException {
    	syncTagsWithMasterList(t);
        tasks.add(t);
    }

    /**
     * Ensures that every tag in this task:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncTagsWithMasterList(Task task) {
        final UniqueTagList taskTags = task.getTags();
        tags.mergeFrom(taskTags);

        // Create map with values = tag object references in the master list
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        for (Tag tag : tags) {
            masterTagObjects.put(tag, tag);
        }

        // Rebuild the list of task tags using references from the master list
        final Set<Tag> commonTagReferences = new HashSet<>();
        for (Tag tag : taskTags) {
            commonTagReferences.add(masterTagObjects.get(tag));
        }
        task.setTags(new UniqueTagList(commonTagReferences));
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
    	
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
    
    public boolean editTask(int targetIndex, String newDate, String newEndDate, String newTime, String newEndTime, String newContent) 
    		throws UniqueTaskList.TaskNotFoundException, ParseException {
    	
        if (tasks.edit(targetIndex, newDate, newEndDate, newTime, newEndTime, newContent)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

```
###### /java/seedu/address/ui/ResultDisplay.java
``` java
public class ResultDisplay extends UiPart {
    public static final String RESULT_DISPLAY_ID = "resultDisplay";
    private static final String STATUS_BAR_STYLE_SHEET = "result-display";
    private TextArea resultDisplayArea;
    private final StringProperty displayed = new SimpleStringProperty("");

    private static final String FXML = "ResultDisplay.fxml";

    private AnchorPane placeHolder;

    private AnchorPane mainPane;

    public static ResultDisplay load(Stage primaryStage, AnchorPane placeHolder) {
        ResultDisplay statusBar = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ResultDisplay());
        statusBar.configure();
        return statusBar;
    }

    public void configure() {
        resultDisplayArea = new TextArea();
        resultDisplayArea.setEditable(false);
        resultDisplayArea.setId(RESULT_DISPLAY_ID);
        resultDisplayArea.getStyleClass().removeAll();
        resultDisplayArea.getStyleClass().add(STATUS_BAR_STYLE_SHEET);
        resultDisplayArea.setText("");
        resultDisplayArea.textProperty().bind(displayed);
        FxViewUtil.applyAnchorBoundaryParameters(resultDisplayArea, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(resultDisplayArea);
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public void postMessage(String message) {
        displayed.setValue(message);
    }

}
```
###### /java/seedu/address/ui/BrowserPanel.java
``` java
public class BrowserPanel extends UiPart{

    private static Logger logger = LogsCenter.getLogger(BrowserPanel.class);
    private WebView browser;

    /**
     * Constructor is kept private as {@link #load(AnchorPane)} is the only way to create a BrowserPanel.
     */
    private BrowserPanel() {

    }

    @Override
    public void setNode(Node node) {
        //not applicable
    }

    @Override
    public String getFxmlPath() {
        return null; //not applicable
    }

    /**
     * Factory method for creating a Browser Panel.
     * This method should be called after the FX runtime is initialized and in FX application thread.
     * @param placeholder The AnchorPane where the BrowserPanel must be inserted
     */
    public static BrowserPanel load(AnchorPane placeholder){
        logger.info("Initializing browser");
        BrowserPanel browserPanel = new BrowserPanel();
        browserPanel.browser = new WebView();
        placeholder.setOnKeyPressed(Event::consume); // To prevent triggering events for typing inside the loaded Web page.
        FxViewUtil.applyAnchorBoundaryParameters(browserPanel.browser, 0.0, 0.0, 0.0, 0.0);
        placeholder.getChildren().add(browserPanel.browser);
        return browserPanel;
    }

    public void loadPersonPage(ReadOnlyTask task) {
        loadPage("https://www.google.com.sg/#safe=off&q=" + task.getContent().toString().replaceAll(" ", "+"));
    }

    public void loadPage(String url){
        browser.getEngine().load(url);
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

}
```
###### /java/seedu/address/storage/XmlSerializableTaskManager.java
``` java
@XmlRootElement(name = "taskmanager")
public class XmlSerializableTaskManager implements ReadOnlyTaskManager {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
    @XmlElement
    private List<Tag> tags;

    {
        tasks = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableTaskManager() {}

    /**
     * Conversion
     */
    public XmlSerializableTaskManager(ReadOnlyTaskManager src) {
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
        tags = src.getTagList();
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        try {
            return new UniqueTagList(tags);
        } catch (UniqueTagList.DuplicateTagException e) {
            //TODO: better error handling
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedTask p : tasks) {
            try {
                lists.add(p.toModelType());
            } catch (IllegalValueException e) {
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return tasks.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags);
    }

}
```
###### /java/seedu/address/commons/core/Version.java
``` java
public class Version implements Comparable<Version> {

    public static final String VERSION_REGEX = "V(\\d+)\\.(\\d+)\\.(\\d+)(ea)?";

    private static final String EXCEPTION_STRING_NOT_VERSION = "String is not a valid Version. %s";

    private static final Pattern VERSION_PATTERN = Pattern.compile(VERSION_REGEX);

    private final int major;
    private final int minor;
    private final int patch;
    private final boolean isEarlyAccess;

    public Version(int major, int minor, int patch, boolean isEarlyAccess) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.isEarlyAccess = isEarlyAccess;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public boolean isEarlyAccess() {
        return isEarlyAccess;
    }

    /**
     * Parses a version number string in the format V1.2.3.
     * @param versionString version number string
     * @return a Version object
     */
    @JsonCreator
    public static Version fromString(String versionString) throws IllegalArgumentException {
        Matcher versionMatcher = VERSION_PATTERN.matcher(versionString);

        if (!versionMatcher.find()) {
            throw new IllegalArgumentException(String.format(EXCEPTION_STRING_NOT_VERSION, versionString));
        }

        return new Version(Integer.parseInt(versionMatcher.group(1)),
                Integer.parseInt(versionMatcher.group(2)),
                Integer.parseInt(versionMatcher.group(3)),
                versionMatcher.group(4) == null ? false : true);
    }

    @JsonValue
    public String toString() {
        return String.format("V%d.%d.%d%s", major, minor, patch, isEarlyAccess ? "ea" : "");
    }

    @Override
    public int compareTo(Version other) {
        return this.major != other.major ? this.major - other.major :
               this.minor != other.minor ? this.minor - other.minor :
               this.patch != other.patch ? this.patch - other.patch :
               this.isEarlyAccess == other.isEarlyAccess() ? 0 :
               this.isEarlyAccess ? -1 : 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Version)) {
            return false;
        }
        final Version other = (Version) obj;

        return this.compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        String hash = String.format("%03d%03d%03d", major, minor, patch);
        if (!isEarlyAccess) {
            hash = "1" + hash;
        }
        return Integer.parseInt(hash);
    }
}
```
###### /java/seedu/address/commons/core/GuiSettings.java
``` java
public class GuiSettings implements Serializable {

    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 740;

    private Double windowWidth;
    private Double windowHeight;
    private Point windowCoordinates;

    public GuiSettings() {
        this.windowWidth = DEFAULT_WIDTH;
        this.windowHeight = DEFAULT_HEIGHT;
        this.windowCoordinates = null; // null represent no coordinates
    }

    public GuiSettings(Double windowWidth, Double windowHeight, int xPosition, int yPosition) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowCoordinates = new Point(xPosition, yPosition);
    }

    public Double getWindowWidth() {
        return windowWidth;
    }

    public Double getWindowHeight() {
        return windowHeight;
    }

    public Point getWindowCoordinates() {
        return windowCoordinates;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof GuiSettings)){ //this handles null as well.
            return false;
        }

        GuiSettings o = (GuiSettings)other;

        return Objects.equals(windowWidth, o.windowWidth)
                && Objects.equals(windowHeight, o.windowHeight)
                && Objects.equals(windowCoordinates.x, o.windowCoordinates.x)
                && Objects.equals(windowCoordinates.y, o.windowCoordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowWidth, windowHeight, windowCoordinates);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Width : " + windowWidth + "\n");
        sb.append("Height : " + windowHeight + "\n");
        sb.append("Position : " + windowCoordinates);
        return sb.toString();
    }
}
```
###### /java/seedu/address/logic/LogicManager.java
``` java
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    private final Config config;

    public LogicManager(Model model, Storage storage, Config config) {
        this.model = model;
        this.parser = new Parser();
        this.config = config;
    }

    @Override
    public CommandResult execute(String commandText) throws ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model, config);
        return command.execute();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }
}
```
###### /java/seedu/address/logic/commands/DeleteCommand.java
``` java
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: %1$s";

    public final int targetIndex;

    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToDelete = lastShownList.get(targetIndex - 1);

        try {
            model.deleteTask(taskToDelete);
        } catch (TaskNotFoundException tnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
    }

}
```

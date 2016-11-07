# A0135787Nreused
###### /java/hard2do/taskmanager/MainApp.java
``` java
    private String getApplicationParameter(String parameterName){
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlyTaskManager> taskManagerOptional;
        ReadOnlyTaskManager initialData;
        try {
            taskManagerOptional = storage.readTaskManager();
            if(!taskManagerOptional.isPresent()){
                logger.info("Data file not found. Will be starting with an empty TaskManager");
            }
            initialData = taskManagerOptional.orElse(new TaskManager());
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty TaskManager");
            initialData = new TaskManager();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty TaskManager");
            initialData = new TaskManager();
        }

        return new ModelManager(initialData, userPrefs);
    }
```
###### /java/hard2do/taskmanager/model/tag/UniqueTagList.java
``` java
public class UniqueTagList implements Iterable<Tag> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTagException extends DuplicateDataException {
        protected DuplicateTagException() {
            super("Operation would result in duplicate tags");
        }
    }

    private ObservableList<Tag> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TagList.
     */
    public UniqueTagList() {}

    /**
     * Varargs/array constructor, enforces no nulls or duplicates.
     */
    public UniqueTagList(Tag... tags) throws DuplicateTagException {
        assert !CollectionUtil.isAnyNull((Object[]) tags);
        final List<Tag> initialTags = Arrays.asList(tags);
        if (!CollectionUtil.elementsAreUnique(initialTags)) {
            throw new DuplicateTagException();
        }
        internalList.addAll(initialTags);
    }

    /**
     * java collections constructor, enforces no null or duplicate elements.
     */
    public UniqueTagList(Collection<Tag> tags) throws DuplicateTagException {
        CollectionUtil.assertNoNullElements(tags);
        if (!CollectionUtil.elementsAreUnique(tags)) {
            throw new DuplicateTagException();
        }
        internalList.addAll(tags);
    }

    /**
     * java set constructor, enforces no nulls.
     */
    public UniqueTagList(Set<Tag> tags) {
        CollectionUtil.assertNoNullElements(tags);
        internalList.addAll(tags);
    }

    /**
     * Copy constructor, insulates from changes in source.
     */
    public UniqueTagList(UniqueTagList source) {
        internalList.addAll(source.internalList); // insulate internal list from changes in argument
    }

    /**
     * All tags in this list as a Set. This set is mutable and change-insulated against the internal list.
     */
    public Set<Tag> toSet() {
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the Tags in this list with those in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        this.internalList.clear();
        this.internalList.addAll(replacement.internalList);
    }

    /**
     * Adds every tag from the argument list that does not yet exist in this list.
     */
    public void mergeFrom(UniqueTagList tags) {
        final Set<Tag> alreadyInside = this.toSet();
        for (Tag tag : tags) {
            if (!alreadyInside.contains(tag)) {
                internalList.add(tag);
            }
        }
    }

    /**
     * Returns true if the list contains an equivalent Tag as the given argument.
     */
    public boolean contains(Tag toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }
    /**
     * Returns true if the list contains a Tag with the same tagname as the given argument.
     * Is case-insensitive
     */
    public boolean hasTag(Tag toCheck) {
        assert toCheck != null;
        for(Tag tag: internalList){
        	if(tag.tagName.equalsIgnoreCase(toCheck.tagName)){
        		return true;
        	}
        }return false;
    }

    /**
     * Adds a Tag to the list.
     *
     * @throws DuplicateTagException if the Tag to add is a duplicate of an existing Tag in the list.
     */
    public void add(Tag toAdd) throws DuplicateTagException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTagException();
        }
        internalList.add(toAdd);
    }
    /**
     * Removes a Tag from the list.
     *
     * @throws DuplicateTagException if the Tag to add is a duplicate of an existing Tag in the list.
     */
    public void remove(Tag tag) {
		assert tag != null;
		internalList.remove(tag);
		
	}

    @Override
    public Iterator<Tag> iterator() {
        return internalList.iterator();
    }

    public ObservableList<Tag> getInternalList() {
        return internalList;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTagList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTagList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

	
}
```
###### /java/hard2do/taskmanager/ui/StatusBarFooter.java
``` java
public class StatusBarFooter extends UiPart {
    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);
    private StatusBar syncStatus;
    private StatusBar saveLocationStatus;

    private GridPane mainPane;

    @FXML
    private AnchorPane saveLocStatusBarPane;

    @FXML
    private AnchorPane syncStatusBarPane;

    private AnchorPane placeHolder;

    private static final String FXML = "StatusBarFooter.fxml";

    public static StatusBarFooter load(Stage stage, AnchorPane placeHolder, String saveLocation) {
        StatusBarFooter statusBarFooter = UiPartLoader.loadUiPart(stage, placeHolder, new StatusBarFooter());
        statusBarFooter.configure(saveLocation);
        return statusBarFooter;
    }

    public void configure(String saveLocation) {
        addMainPane();
        addSyncStatus();
        setSyncStatus("Not updated yet in this session");
        addSaveLocation();
        setSaveLocation("./" + saveLocation);
        registerAsAnEventHandler(this);
    }

    private void addMainPane() {
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
    }

    private void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }

    private void addSaveLocation() {
        this.saveLocationStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(saveLocationStatus, 0.0, 0.0, 0.0, 0.0);
        saveLocStatusBarPane.getChildren().add(saveLocationStatus);
    }

    private void setSyncStatus(String status) {
        this.syncStatus.setText(status);
    }

    private void addSyncStatus() {
        this.syncStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(syncStatus, 0.0, 0.0, 0.0, 0.0);
        syncStatusBarPane.getChildren().add(syncStatus);
    }

    @Override
    public void setNode(Node node) {
        mainPane = (GridPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent abce) {
        String lastUpdated = (new Date()).toString();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, "Setting last updated status to " + lastUpdated));
        setSyncStatus("Last Updated: " + lastUpdated);
    }
}
```
###### /java/hard2do/taskmanager/ui/HelpWindow.java
``` java
public class HelpWindow extends UiPart {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "HelpWindow.fxml";
    private static final String TITLE = "Help";
    private static final String USERGUIDE_URL =
            "https://github.com/CS2103-AUG2016-T16-C1/main/blob/master/docs/UserGuide.md";

    private AnchorPane mainPane;

    private Stage dialogStage;

    public static HelpWindow load(Stage primaryStage) {
        logger.fine("Showing help page about the application.");
        HelpWindow helpWindow = UiPartLoader.loadUiPart(primaryStage, new HelpWindow());
        helpWindow.configure();
        return helpWindow;
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    private void configure(){
        Scene scene = new Scene(mainPane);
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMaximized(true); //TODO: set a more appropriate initial size
        setIcon(dialogStage, ICON);

        WebView browser = new WebView();
        browser.getEngine().load(USERGUIDE_URL);
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(browser);
    }

    public void show() {
        dialogStage.showAndWait();
    }
}
```
###### /java/hard2do/taskmanager/commons/core/EventsCenter.java
``` java
public class EventsCenter {
    private static final Logger logger = LogsCenter.getLogger(EventsCenter.class);
    private final EventBus eventBus;
    private static EventsCenter instance;

    public static EventsCenter getInstance() {
        if (instance == null) {
            instance = new EventsCenter();
        }
        return instance;
    }

    public static void clearSubscribers() {
        instance = null;
    }

    private EventsCenter() {
        eventBus = new EventBus();
    }

    public EventsCenter registerHandler(Object handler) {
        eventBus.register(handler);
        return this;
    }

    /**
     * Posts an event to the event bus.
     */
    public <E extends BaseEvent> EventsCenter post(E event) {
        logger.info("------[Event Posted] " + event.getClass().getCanonicalName() + ": " + event.toString());
        eventBus.post(event);
        return this;
    }

}
```
###### /java/hard2do/taskmanager/commons/events/model/TaskManagerChangedEvent.java
``` java
/** Indicates the TaskManager in the model has changed*/
public class TaskManagerChangedEvent extends BaseEvent {

    public final ReadOnlyTaskManager data;

    public TaskManagerChangedEvent(ReadOnlyTaskManager data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}
```
###### /java/hard2do/taskmanager/logic/commands/DeleteTagCommand.java
``` java
/**
 * Deletes a tag from a task identified using it's last displayed index from the task manager.
 */
public class DeleteTagCommand extends Command {
    public static final String COMMAND_WORD = "deltag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": deletes a tag from a task listed in the task manager. \n"
            + "Parameters: INDEX[MUST BE POSITIVE INTEGER] TAGS[ANY NUMBER OF TAGS SEPARATED BY SPACE] \n"
            + "Example: " + COMMAND_WORD
            + " 1 toughlife easygame";

    public static final String MESSAGE_SUCCESS = "Task tags updated: %1$s";
    public static final String MESSAGE_INVALID_TAG = "Tag must be alphanumerical";
    public static final String MESSAGE_NO_TAG = "No Tag can be found";
    
    private int targetIndex;
    private ArrayList<String> tagsToDel;


    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     * @throws ParseException 
     */
   
	public DeleteTagCommand(String index, String tagsString)
            throws IllegalValueException, ParseException {
    		this.targetIndex = Integer.parseInt(index.trim());
    		tagsToDel = new ArrayList<String>();
    		
    		Scanner sc = new Scanner(tagsString.trim());
    			while (sc.hasNext()) {
    				String tagToAdd = sc.next();
    				tagsToDel.add(tagToAdd);
    			}
    		
    		
    		sc.close();
        
    }

    @Override
    public CommandResult execute() {
    	UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        if (tagsToDel.size() == 0){
        	return new CommandResult("No Tags To Delete");
        }
        ReadOnlyTask taskToDelTags= lastShownList.get(targetIndex - 1);
        for (String s : tagsToDel) {
        	try {
				if (!taskToDelTags.getTags().contains(new Tag(s))) {
					return new CommandResult("Task does not have Tag: " + s );
				}
			} catch (IllegalValueException e) {
				
				return new CommandResult("Tag must be alphanumerical");
			}
        	
        }
        
        try {
            model.deleteTags(taskToDelTags, tagsToDel);
        } catch (TaskNotFoundException tnfe) {
            assert false : "The target task cannot be missing";
        } catch (ParseException pe) {
        	return new CommandResult("ParseException");
        } catch (IllegalValueException ive) {
        	return new CommandResult("Tags must be alphanumerical");
        }
       lastShownList = model.getFilteredTaskList();
        ReadOnlyTask updatedTask = lastShownList.get(targetIndex - 1);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedTask));
    }
}
```
###### /java/hard2do/taskmanager/logic/commands/AddCommand.java
``` java
/**
 * Adds a task to the task manager.
 */
public class AddCommand extends Command {

	public static final String COMMAND_WORD = "add";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. \n"
			+ "Parameters: CONTENT sd/DATE[dd-mm-yyyy] ed/DATE[dd-mm-yyyy] st/time[HH:mm] et/endTime[HH:mm] [#TAG]...\n"
			+ "Note: order and presence of parameters after CONTENT do not matter. \n" + "Example: " + COMMAND_WORD
			+ " do this task manager sd/20-10-2016 ed/20-10-2016 st/13:00 et/16:00 #shaglife #wheregottime";

	public static final String MESSAGE_SUCCESS = "New task added: %1$s";
	public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";
	public static final String MESSAGE_STARTENDDATE_CONSTRAINTS = "Start date must be added";
	public static final String MESSAGE_STARTENDTIME_CONSTRAINTS = "Start time must be added";
	public static final String MESSAGE_ENDDATETIME_CONSTRAINTS = "End date must have a corresponding end time";

	private final ReadOnlyTask toAdd;

	private TaskDate dateToAdd;
	private TaskTime timeToAdd;
	private TaskDate endDateToAdd;
	private TaskTime endTimeToAdd;

	/**
	 * Convenience constructor using raw values.
	 *
	 * @throws IllegalValueException
	 *             if any of the raw values are invalid
	 * @throws ParseException
	 */

	public AddCommand(String content, String date, String endDate, String time, String endTime, Integer duration,
			Set<String> tags) throws IllegalValueException, ParseException {
		assert content != null;

		isValidTimeDate(date, endDate, time, endTime);

		final Set<Tag> tagSet = new HashSet<>();
		for (String tagName : tags) {
			tagSet.add(new Tag(tagName));
		}

		// Infer date and time
		if (date != null) {
			dateToAdd = new TaskDate(date);
		} else if (date == null) {
			InferDateUtil idu = new InferDateUtil(content);
			if (idu.findDate()) {
				dateToAdd = new TaskDate(idu.getDate());
			} else {
				dateToAdd = new TaskDate();
			}
		}
		// check null for date and time
		if (endDate == null) {
			endDateToAdd = new TaskDate();
		} else
			endDateToAdd = new TaskDate(endDate);

		if (time != null) {
			timeToAdd = new TaskTime(time);
			if (endTime != null) {
				endTimeToAdd = new TaskTime(endTime);
			}
		} else if (time == null && endTime == null) {
			InferTimeUtil itu = new InferTimeUtil(content);
			if (itu.findTimeToTime()) {
				timeToAdd = new TaskTime(itu.getStartTime());
				endTimeToAdd = new TaskTime(itu.getEndTime());
			} else if (itu.findTime()) {
				timeToAdd = new TaskTime(itu.getTime());
				endTimeToAdd = new TaskTime();
			} else {
				timeToAdd = new TaskTime();
				endTimeToAdd = new TaskTime();
			}
		} 

		if (duration != null) {
			this.toAdd = new RecurringTask(new Content(content), dateToAdd, timeToAdd, duration,
					new UniqueTagList(tagSet));
		} else if (endTimeToAdd != null || endDateToAdd != null) {
				this.toAdd = new Task(new Content(content), dateToAdd, endDateToAdd, timeToAdd, endTimeToAdd,
						new UniqueTagList(tagSet));
			}
		else {
			this.toAdd = new Task(new Content(content), dateToAdd, timeToAdd, new UniqueTagList(tagSet));
		}
	}

	@Override
	public CommandResult execute() {
		assert model != null;

		try {
			model.addTask(toAdd);
			return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
		} catch (UniqueTaskList.DuplicateTaskException e) {
			return new CommandResult(MESSAGE_DUPLICATE_TASK);
		}

	}
	
	/**
	 * Validates the given values
	 * 
	 * @param endDate
	 * @param startTime
	 * @param endTime
	 * @throws IllegalValueException
	 */
	public static void isValidTimeDate(String startDate, String endDate, String startTime, String endTime)
			throws IllegalValueException {
		if (endDate != null && endTime == null) {
			throw new IllegalValueException(MESSAGE_ENDDATETIME_CONSTRAINTS);
		}

		if (endDate != null && endTime != null) {
			hasStartDate(startDate);
			hasStartTime(startTime);
		}
		if (endDate != null && startDate != null) {
			EndStartValuesUtil.dateRangeValid(startDate, endDate);
		}
		if (startTime != null && endTime != null && endDate == null){
			EndStartValuesUtil.timeRangeValid(startTime, endTime);
		}
		
	}
	
	public static void hasStartDate(String startDate) throws IllegalValueException {
		if (startDate == null)
			throw new IllegalValueException(MESSAGE_STARTENDDATE_CONSTRAINTS);
	}

	public static void hasStartTime(String startTime) throws IllegalValueException {
		if (startTime == null) {
			throw new IllegalValueException(MESSAGE_STARTENDTIME_CONSTRAINTS);
		}
	}
	

	
}
```

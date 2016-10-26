# A0139523Ereused
###### /java/seedu/address/TestApp.java
``` java
public class TestApp extends MainApp {

    public static final String SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.xml");
    protected static final String DEFAULT_PREF_FILE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("pref_testing.json");
    public static final String APP_TITLE = "Test App";
    protected static final String TASK_MANAGER_NAME = "Test";
    protected Supplier<ReadOnlyTaskManager> initialDataSupplier = () -> null;
    protected String saveFileLocation = SAVE_LOCATION_FOR_TESTING;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyTaskManager> initialDataSupplier, String saveFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            TestUtil.createDataFileWithData(
                    new XmlSerializableTaskManager(this.initialDataSupplier.get()),
                    this.saveFileLocation);
        }
    }

    @Override
    protected Config initConfig(String configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setAppTitle(APP_TITLE);
        config.setTaskManagerFilePath(saveFileLocation);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        config.setTaskManagerName(TASK_MANAGER_NAME);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(Config config) {
        UserPrefs userPrefs = super.initPrefs(config);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.updateLastUsedGuiSetting(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        return userPrefs;
    }


    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```
###### /java/seedu/address/commons/core/ConfigTest.java
``` java
public class ConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultConfigAsString = "Task Manager name: Hard2Do\n" +
                "Current log level : INFO\n" +
                "Preference file Location : preferences.json\n" +
                "Local data file location : data/taskmanager.xml\n" +
                "Task Manager name : Hard2Do";
        
        assertEquals(defaultConfigAsString, new Config().toString());
    }

    @Test
    public void equalsMethod(){
        Config defaultConfig = new Config();
        assertFalse(defaultConfig.equals(null));
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
```
###### /java/seedu/address/commons/core/VersionTest.java
``` java
public class VersionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void versionParsing_acceptableVersionString_parsedVersionCorrectly() {
        verifyVersionParsedCorrectly("V0.0.0ea", 0, 0, 0, true);
        verifyVersionParsedCorrectly("V3.10.2", 3, 10, 2, false);
        verifyVersionParsedCorrectly("V100.100.100ea", 100, 100, 100, true);
    }

    @Test
    public void versionParsing_wrongVersionString_throwIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        Version.fromString("This is not a version string");
    }

    @Test
    public void versionConstructor_correctParameter_valueAsExpected() {
        Version version = new Version(19, 10, 20, true);

        assertEquals(19, version.getMajor());
        assertEquals(10, version.getMinor());
        assertEquals(20, version.getPatch());
        assertEquals(true, version.isEarlyAccess());
    }

    @Test
    public void versionToString_validVersion_correctStringRepresentation() {
        // boundary at 0
        Version version = new Version(0, 0, 0, true);
        assertEquals("V0.0.0ea", version.toString());

        // normal values
        version = new Version(4, 10, 5, false);
        assertEquals("V4.10.5", version.toString());

        // big numbers
        version = new Version(100, 100, 100, true);
        assertEquals("V100.100.100ea", version.toString());
    }

    @Test
    public void versionComparable_validVersion_compareToIsCorrect() {
        Version one, another;

        // Tests equality
        one = new Version(0, 0, 0, true);
        another = new  Version(0, 0, 0, true);
        assertTrue(one.compareTo(another) == 0);

        one = new Version(11, 12, 13, false);
        another = new  Version(11, 12, 13, false);
        assertTrue(one.compareTo(another) == 0);

        // Tests different patch
        one = new Version(0, 0, 5, false);
        another = new  Version(0, 0, 0, false);
        assertTrue(one.compareTo(another) > 0);

        // Tests different minor
        one = new Version(0, 0, 0, false);
        another = new  Version(0, 5, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests different major
        one = new Version(10, 0, 0, true);
        another = new  Version(0, 0, 0, true);
        assertTrue(one.compareTo(another) > 0);

        // Tests high major vs low minor
        one = new Version(10, 0, 0, true);
        another = new  Version(0, 1, 0, true);
        assertTrue(one.compareTo(another) > 0);

        // Tests high patch vs low minor
        one = new Version(0, 0, 10, false);
        another = new  Version(0, 1, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests same major minor different patch
        one = new Version(2, 15, 0, false);
        another = new  Version(2, 15, 5, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access vs not early access on same version number
        one = new Version(2, 15, 0, true);
        another = new  Version(2, 15, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access lower version vs not early access higher version compare by version number first
        one = new Version(2, 15, 0, true);
        another = new  Version(2, 15, 5, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access higher version vs not early access lower version compare by version number first
        one = new Version(2, 15, 0, false);
        another = new  Version(2, 15, 5, true);
        assertTrue(one.compareTo(another) < 0);
    }

    @Test
    public void versionComparable_validVersion_hashCodeIsCorrect() {
        Version version = new Version(100, 100, 100, true);
        assertEquals(100100100, version.hashCode());

        version = new Version(10, 10, 10, false);
        assertEquals(1010010010, version.hashCode());
    }

    @Test
    public void versionComparable_validVersion_equalIsCorrect() {
        Version one, another;

        one = new Version(0, 0, 0, false);
        another = new  Version(0, 0, 0, false);
        assertTrue(one.equals(another));

        one = new Version(100, 191, 275, true);
        another = new  Version(100, 191, 275, true);
        assertTrue(one.equals(another));
    }

    private void verifyVersionParsedCorrectly(String versionString,
                                              int major, int minor, int patch, boolean isEarlyAccess) {
        assertEquals(new Version(major, minor, patch, isEarlyAccess), Version.fromString(versionString));
    }
}
```
###### /java/seedu/address/logic/LogicManagerTest.java
``` java
public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;
    private Config config;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskManager latestSavedTaskManager;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskManagerChangedEvent abce) {
        latestSavedTaskManager = new TaskManager(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempTaskManagerFile = saveFolder.getRoot().getPath() + "TempTaskManager.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTaskManagerFile, tempPreferencesFile), new Config());
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskManager = new TaskManager(model.getTaskManager()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'task manager' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, ReadOnlyTaskManager, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskManager(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal task manager data are same as those in the {@code expectedTaskManager} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedTaskManager} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyTaskManager expectedTaskManager,
                                       List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);

        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredTaskList());

        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedTaskManager, model.getTaskManager());
        assertEquals(expectedTaskManager, latestSavedTaskManager);
    }


    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskManager(), Collections.emptyList());
    }


    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior(
                "add #me", expectedMessage);
        assertCommandBehavior(
                "add st/10-20 ", expectedMessage);
    }

    @Test
    public void execute_add_invalidTaskData() throws Exception {
        //assertCommandBehavior(
                //"add []\\[;] d/20-10-2016 t/13:00 #one", Content.MESSAGE_CONTENT_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Content sd/notvaliddate st/14:00 #two", TaskDate.MESSAGE_DATE_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Content sd/20-10-2016 st/notvalidtime #three", TaskTime.MESSAGE_TIME_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Content sd/20-10-2016 st/13:00 #[][]", Tag.MESSAGE_TAG_CONSTRAINTS);

    }

    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.homework();
        TaskManager expectedTM = new TaskManager();
        expectedTM.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedTM,
                expectedTM.getTaskList());

    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.homework();
        TaskManager expectedTM = new TaskManager();
        expectedTM.addTask(toBeAdded);

        // setup starting state
        model.addTask(toBeAdded); // task already in internal task manager

        // execute command and verify result
        assertCommandBehavior(
                helper.generateAddCommand(toBeAdded),
                AddCommand.MESSAGE_DUPLICATE_TASK,
                expectedTM,
                expectedTM.getTaskList());

    }


    @Test
    public void execute_list_showsAllTasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        TaskManager expectedTM = helper.generateTaskManager(2);
        List<? extends ReadOnlyTask> expectedList = expectedTM.getTaskList();

        // prepare task manager state
        helper.addToModel(model, 2);

        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS_ALL,
                expectedTM,
                expectedList);
    }


    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage) throws Exception {
        assertCommandBehavior(commandWord , expectedMessage); //index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); //index cannot be 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> taskList = helper.generateTaskList(2);

        // set TB state to 2 tasks
        model.resetData(new TaskManager());
        for (Task p : taskList) {
            model.addTask(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getTaskManager(), taskList);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_select_jumpsToCorrectTask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(3);

        TaskManager expectedTM = helper.generateTaskManager(threeTasks);
        helper.addToModel(model, threeTasks);

        assertCommandBehavior("select 2",
                String.format(SelectCommand.MESSAGE_SELECT_TASK_SUCCESS, 2),
                expectedTM,
                expectedTM.getTaskList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredTaskList().get(1), threeTasks.get(1));
    }


    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrectTask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(3);

        TaskManager expectedTM = helper.generateTaskManager(threeTasks);
        expectedTM.removeTask(threeTasks.get(1));
        helper.addToModel(model, threeTasks);

        assertCommandBehavior("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, threeTasks.get(1)),
                expectedTM,
                expectedTM.getTaskList());
    }


    @Test
    public void execute_find_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find ", expectedMessage);
    }

    @Test
    public void execute_find_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task tTarget2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task t1 = helper.generateTaskWithName("KE Y");
        Task t2 = helper.generateTaskWithName("KEYKEYKEY sduauo");

        List<Task> fourTasks = helper.generateTaskList(t1, tTarget1, t2, tTarget2);
        TaskManager expectedTM = helper.generateTaskManager(fourTasks);
        List<Task> expectedList = helper.generateTaskList(tTarget1, tTarget2, t2);
        helper.addToModel(model, fourTasks);

        /*assertCommandBehavior("find KEY",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedTM,
                expectedList);*/
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTaskWithName("bla bla KEY bla");
        Task t2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task t3 = helper.generateTaskWithName("key key");
        Task t4 = helper.generateTaskWithName("KEy sduauo");

        List<Task> fourTasks = helper.generateTaskList(t3, t1, t4, t2);
        TaskManager expectedTM = helper.generateTaskManager(fourTasks);
        List<Task> expectedList = fourTasks;
        helper.addToModel(model, fourTasks);

        assertCommandBehavior("find KEY",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedTM,
                expectedList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task tTarget2 = helper.generateTaskWithName("bla rAnDoMkey bla bceofeia");
        Task tTarget3 = helper.generateTaskWithName("key key");
        Task t1 = helper.generateTaskWithName("sduauo");

        List<Task> fourTasks = helper.generateTaskList(tTarget1, t1, tTarget2, tTarget3);
        TaskManager expectedTM = helper.generateTaskManager(fourTasks);
        List<Task> expectedList = helper.generateTaskList(tTarget1, tTarget2, tTarget3);
        helper.addToModel(model, fourTasks);

        assertCommandBehavior("find key",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedTM,
                expectedList);
    }


    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{

        Task homework() throws Exception {
            Content content = new Content("Do Homework");
            TaskDate taskdate = new TaskDate("21-02-2016", "22-02-2016");
            TaskTime tasktime = new TaskTime("13:00", "16:00");
            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("tag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(content, taskdate, tasktime, 0, tags);
        }

        /**
         * Generates a valid person using the given seed.
         * Running this function with the same parameter values guarantees the returned person will have the same state.
         * Each unique seed will generate a unique Person object.
         *
         * @param seed used to generate the person data field values
         */
        Task generateTask(int seed) throws Exception {
      
            return new Task(
                    new Content("Content " + seed),
                    new TaskDate(("1" + Math.abs(seed) + "-0" + Math.abs(seed) + "-2016"), ("2" + Math.abs(seed) + "-0" + Math.abs(seed) + "-2016")),
                    new TaskTime(("1" + Math.abs(seed) + ":0" + Math.abs(seed)), ("1" + Math.abs(seed) + ":1" + Math.abs(seed))), 5,
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1)))
            );
        }
        

        /** Generates the correct add command based on the task given */
        String generateAddCommand(Task t) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(t.getContent().value);
            cmd.append(" sd/").append(t.getDate().dateString);
            cmd.append(" ed/").append(t.getDate().enddateString);
            cmd.append(" st/").append(t.getTime().timeString);
            cmd.append(" et/").append(t.getTime().endtimeString);

            UniqueTagList tags = t.getTags();
            for(Tag tg: tags){
                cmd.append(" #").append(tg.tagName);
            }

            return cmd.toString();
        }

        /**
         * Generates an TaskManager with auto-generated tasks.
         */
        TaskManager generateTaskManager(int numGenerated) throws Exception{
            TaskManager taskManager = new TaskManager();
            addToTaskManager(taskManager, numGenerated);
            return taskManager;
        }

        /**
         * Generates an TaskManager based on the list of Tasks given.
         */
        TaskManager generateTaskManager(List<Task> tasks) throws Exception{
            TaskManager taskManager = new TaskManager();
            addToTaskManager(taskManager, tasks);
            return taskManager;
        }

        /**
         * Adds auto-generated Task objects to the given TaskManager
         * @param taskManager The TaskManager to which the Tasks will be added
         */
        void addToTaskManager(TaskManager taskManager, int numGenerated) throws Exception{
            addToTaskManager(taskManager, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given TaskManager
         */
        void addToTaskManager(TaskManager taskManager, List<Task> tasksToAdd) throws Exception{
            for(Task p: tasksToAdd){
                taskManager.addTask(p);
            }
        }

        /**
         * Adds auto-generated Task objects to the given model
         * @param model The model to which the Tasks will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given model
         */
        void addToModel(Model model, List<Task> tasksToAdd) throws Exception{
            for(Task p: tasksToAdd){
                model.addTask(p);
            }
        }

        /**
         * Generates a list of Tasks based on the flags.
         */
        List<Task> generateTaskList(int numGenerated) throws Exception{
            List<Task> tasks = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                tasks.add(generateTask(i));
            }
            return tasks;
        }

        List<Task> generateTaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }

        /**
         * Generates a Task object with given name. Other fields will have some dummy values.
         */
        Task generateTaskWithName(String name) throws Exception {
            return new Task(
                    new Content(name),
                    new TaskDate("13-02-2016", null),
                    new TaskTime("13:00", null), null,
                    new UniqueTagList(new Tag("tag"))
            );
        }
    }
}
```
###### /java/seedu/address/testutil/TaskBuilder.java
``` java
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
    }

    public TaskBuilder withContent(String content) throws IllegalValueException {
        this.task.setContent(new Content(content));
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            task.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TaskBuilder withDate(String taskdate, String enddate) throws IllegalValueException, ParseException {
        this.task.setDate(new TaskDate(taskdate, enddate));
        return this;
    }

    public TaskBuilder withTime(String startTime, String endTime) throws IllegalValueException, ParseException {
        this.task.setTime(new TaskTime(startTime, endTime));
        return this;
    }

    public TestTask build() {
        return this.task;
    }

}
```
###### /java/seedu/address/testutil/TaskManagerBuilder.java
``` java
public class TaskManagerBuilder {

    private TaskManager taskManager;

    public TaskManagerBuilder(TaskManager taskManager){
        this.taskManager = taskManager;
    }

    public TaskManagerBuilder withTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskManager.addTask(task);
        return this;
    }

    public TaskManagerBuilder withTag(String tagName) throws IllegalValueException {
        taskManager.addTag(new Tag(tagName));
        return this;
    }

    public TaskManager build(){
        return taskManager;
    }
}
```
###### /java/seedu/address/testutil/TestTask.java
``` java
	public class TestTask implements ReadOnlyTask {

	    private Content content;
	    private TaskDate taskdate;
	    private TaskTime tasktime;
	    private boolean done;
	    private Integer duration;

	    private UniqueTagList tags;

	    public TestTask() {
	        tags = new UniqueTagList();
	    }

	    public void setContent(Content content) {
	        this.content = content;
	    }

	    public void setDate(TaskDate taskdate) {
	        this.taskdate = taskdate;
	    }

	    public void setTime(TaskTime tasktime) {
	        this.tasktime = tasktime;
	    }

	    @Override
	    public Content getContent() {
	        return content;
	    }

	    @Override
	    public TaskDate getDate() {
	        return taskdate;
	    }

	    @Override
	    public TaskTime getTime() {
	        return tasktime;
	    }

	    @Override
	    public UniqueTagList getTags() {
	        return tags;
	    }

	    @Override
	    public String toString() {
	    if(this.getDate().enddateString == null) {
	          return getAsText();
	            }
	            else
	                return getAsText2();
	        }

	    public String getAddCommand() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("add " + this.getContent().value + " ");
	        sb.append("sd/" + this.getDate().dateString + " ");
	        sb.append("ed/" + this.getDate().enddateString + " ");
	        sb.append("st/" + this.getTime().timeString + " ");
	        sb.append("et/" + this.getTime().endtimeString + " ");
	        this.getTags().getInternalList().stream().forEach(s -> sb.append("#" + s.tagName + " "));
	        return sb.toString();
	    }

        @Override
        public boolean getDone() {
            return done;
        }

        @Override
        public boolean setDone() {
            if (!done) done = true;
            else return false;
            return true;
        }
        
        @Override
        public Integer getDuration() {
            return duration;
        }
        
        @Override
        public boolean addTags(ArrayList<String> tagsToAdd) throws DuplicateTagException, IllegalValueException {
            UniqueTagList newList = new UniqueTagList();
            for(String t : tagsToAdd){
                newList.add(new Tag(t));
            }
            newList.mergeFrom(tags);
            setTags(newList);
            return true;
        }
        
        /**
         * Replaces this task's tags with the tags in the argument tag list.
         */
        public void setTags(UniqueTagList replacement) {
            tags.setTags(replacement);
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
        
        public boolean setUndone(){
        	if (done) done = false;
        	else return false;
        	return true;
        }
        

		@Override
		public boolean deleteTags(ArrayList<String> tagsToDel) throws DuplicateTagException, IllegalValueException {
			
			return false;
		}

        @Override
        public boolean getImportant() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setImportant() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setUnimportant() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setNext() {
            // TODO Auto-generated method stub
            return false;
        }



	}

```
###### /java/guitests/AddCommandTest.java
``` java
public class AddCommandTest extends TaskManagerGuiTest {

    @Test
    public void add() {
        //add one task

    	TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.appointment;
        //assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
   
        //add another task
        taskToAdd = TypicalTestTasks.flight;
        //assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add duplicate task
        commandBox.runCommand(TypicalTestTasks.flight.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand("clear");
        
        //assertAddSuccess(TypicalTestTasks.homework);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        try{
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getContent().value);
        assertMatching(taskToAdd, addedCard);
        }
        catch(IllegalStateException e) {
        	e.printStackTrace();
        	assert false : "something wrong";
        }
        //confirm the list now contains all previous persons plus the new person
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
```
###### /java/guitests/guihandles/CommandBoxHandle.java
``` java
public class CommandBoxHandle extends GuiHandle{

    private static final String COMMAND_INPUT_FIELD_ID = "#commandTextField";

    public CommandBoxHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    public void enterCommand(String command) {
        setTextField(COMMAND_INPUT_FIELD_ID, command);
    }

    public String getCommandInput() {
        return getTextFieldText(COMMAND_INPUT_FIELD_ID);
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     */
    public void runCommand(String command) {
        enterCommand(command);
        pressEnter();
        guiRobot.sleep(200); //Give time for the command to take effect
    }

    public HelpWindowHandle runHelpCommand() {
        enterCommand("help");
        pressEnter();
        return new HelpWindowHandle(guiRobot, primaryStage);
    }
}
```
###### /java/guitests/guihandles/GuiHandle.java
``` java
public class GuiHandle {
    protected final GuiRobot guiRobot;
    protected final Stage primaryStage;
    protected final String stageTitle;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    public GuiHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        this.guiRobot = guiRobot;
        this.primaryStage = primaryStage;
        this.stageTitle = stageTitle;
        focusOnSelf();
    }

    public void focusOnWindow(String stageTitle) {
        logger.info("Focusing " + stageTitle);
        java.util.Optional<Window> window = guiRobot.listTargetWindows()
                .stream()
                .filter(w -> w instanceof Stage && ((Stage) w).getTitle().equals(stageTitle)).findAny();

        if (!window.isPresent()) {
            logger.warning("Can't find stage " + stageTitle + ", Therefore, aborting focusing");
            return;
        }

        guiRobot.targetWindow(window.get());
        guiRobot.interact(() -> window.get().requestFocus());
        logger.info("Finishing focus " + stageTitle);
    }

    protected Node getNode(String query) {
        return guiRobot.lookup(query).tryQuery().get();
    }

    protected String getTextFieldText(String filedName) {
        return ((TextField) getNode(filedName)).getText();
    }

    protected void setTextField(String textFieldId, String newText) {
        guiRobot.clickOn(textFieldId);
        ((TextField)guiRobot.lookup(textFieldId).tryQuery().get()).setText(newText);
        guiRobot.sleep(500); // so that the texts stays visible on the GUI for a short period
    }

    public void pressEnter() {
        guiRobot.type(KeyCode.ENTER).sleep(500);
    }

    protected String getTextFromLabel(String fieldId, Node parentNode) {
        return ((Label) guiRobot.from(parentNode).lookup(fieldId).tryQuery().get()).getText();
    }

    public void focusOnSelf() {
        if (stageTitle != null) {
            focusOnWindow(stageTitle);
        }
    }

    public void focusOnMainApp() {
        this.focusOnWindow(TestApp.APP_TITLE);
    }

    public void closeWindow() {
        java.util.Optional<Window> window = guiRobot.listTargetWindows()
                .stream()
                .filter(w -> w instanceof Stage && ((Stage) w).getTitle().equals(stageTitle)).findAny();

        if (!window.isPresent()) {
            return;
        }

        guiRobot.targetWindow(window.get());
        guiRobot.interact(() -> ((Stage)window.get()).close());
        focusOnMainApp();
    }
}
```
###### /java/guitests/guihandles/TaskCardHandle.java
``` java
public class TaskCardHandle extends GuiHandle {
    private static final String CONTENT_FIELD_ID = "#content";
    private static final String DATE_FIELD_ID = "#date";
    private static final String TAG_FIELD_ID = "#tags";


    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getContent() {
        return getTextFromLabel(CONTENT_FIELD_ID);
    }

    public String getDate() {
        return getTextFromLabel(DATE_FIELD_ID);
    }
    
    public String getTag() {
        return getTextFromLabel(TAG_FIELD_ID);
    }



    public boolean isSameTask(ReadOnlyTask task){
        return getContent().toString().equals(task.getContent().value) && getDate().toString().equals(task.getDate().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getContent().equals(handle.getContent())
                    && getDate().equals(handle.getDate());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getContent() + " " + getDate();
    }
}
```
###### /java/guitests/HelpWindowTest.java
``` java
public class HelpWindowTest extends TaskManagerGuiTest {

    @Test
    public void openHelpWindow() {

        taskListPanel.clickOnListView();

        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());

        assertHelpWindowOpen(mainMenu.openHelpWindowUsingMenu());

        assertHelpWindowOpen(commandBox.runHelpCommand());

    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    }
}
```
###### /java/guitests/FindCommandTest.java
``` java
public class FindCommandTest extends TaskManagerGuiTest {

    @Test
    public void find_nonEmptyList() {
        assertFindResult("find Mark"); //no results
        assertFindResult("find Study", td.study, td.activities); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find Study",td.activities);
    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        //assertFindResult("find Jean"); //no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, TestTask... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
```

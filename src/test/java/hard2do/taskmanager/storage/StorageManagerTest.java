package hard2do.taskmanager.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import hard2do.taskmanager.commons.events.model.TaskManagerChangedEvent;
import hard2do.taskmanager.commons.events.storage.DataSavingExceptionEvent;
import hard2do.taskmanager.model.ReadOnlyTaskManager;
import hard2do.taskmanager.model.TaskManager;
import hard2do.taskmanager.model.UserPrefs;
import hard2do.taskmanager.storage.JsonUserPrefsStorage;
import hard2do.taskmanager.storage.Storage;
import hard2do.taskmanager.storage.StorageManager;
import hard2do.taskmanager.storage.XmlTaskManagerStorage;
import hard2do.taskmanager.testutil.EventsCollector;
import hard2do.taskmanager.testutil.TypicalTestTasks;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//@@author A0139523E-reused
public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setup() {
        storageManager = new StorageManager(getTempFilePath("ab"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void taskManagerReadSave() throws Exception {
        TaskManager original = new TypicalTestTasks().getTypicalTaskManager();
        storageManager.saveTaskManager(original);
        ReadOnlyTaskManager retrieved = storageManager.readTaskManager().get();
        assertEquals(original, new TaskManager(retrieved));
        //More extensive testing of TaskManager saving/reading is done in XmlTaskManagerStorageTest
    }

    @Test
    public void getTaskManagerFilePath(){
        assertNotNull(storageManager.getTaskManagerFilePath());
    }

    @Test
    public void handleTaskManagerChangedEvent_exceptionThrown_eventRaised() throws IOException {
        //Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTaskManagerStorageExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleTaskManagerChangedEvent(new TaskManagerChangedEvent(new TaskManager()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskManagerStorageExceptionThrowingStub extends XmlTaskManagerStorage{

        public XmlTaskManagerStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}

# A0135787Nreused
###### /java/guitests/ClearCommandTest.java
``` java
public class ClearCommandTest extends TaskManagerGuiTest {

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(td.homework.getAddCommand());
        assertTrue(taskListPanel.isListMatching(td.homework));
        commandBox.runCommand("delete 1");
//        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertResultMessage("Task Manager has been cleared!");
    }
}
```

# A0135787Nreused
###### \java\guitests\ClearCommandTest.java
``` java
public class ClearCommandTest extends TaskManagerGuiTest {

    @Test
    public void testClear_nonEmptyList_listCleared() {
        //verify a non-empty list can be cleared
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
        assertClearCommandSuccess();
    }
    
    @Test
    public void testClear_beforeCommands_listCleared() {
    	//verify clear command can work after other commands
    	commandBox.runCommand(td.flight.getAddCommand());
    	commandBox.runCommand("clear");
    	assertListSize(0);
    }
    
    @Test
    public void testClear_afterCommands_listCleared() {
        //verify other commands can work after a clear command
        commandBox.runCommand("clear");
        assertListSize(0);
        commandBox.runCommand(td.flight.getAddCommand());
        TestTask taskToAdd = TypicalTestTasks.flight;
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getContent().value);
        assertMatching(taskToAdd, addedCard);
    }
   
    @Test
    public void testClear_emptyList_listCleared() {
        //verify clear command works when the list is empty
    	commandBox.runCommand("clear");
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertResultMessage("Task Manager has been cleared!");
    }
}
```

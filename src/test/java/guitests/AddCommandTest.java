package guitests;

import guitests.guihandles.TaskCardHandle;
import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.logic.commands.AddCommand;
import hard2do.taskmanager.logic.commands.EditCommand;
import hard2do.taskmanager.model.task.UniqueTaskList.DuplicateTaskException;
import hard2do.taskmanager.testutil.TestTask;
import hard2do.taskmanager.testutil.TestUtil;
import hard2do.taskmanager.testutil.TypicalTestTasks;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
//@@author A0139523E-reused
public class AddCommandTest extends TaskManagerGuiTest {

    @Test
    public void testAddTask_multipleTasks_addedTasksExpected() throws DuplicateTaskException, IllegalValueException {
        //add one task

    	TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.appointment;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
   
        //add another task
        taskToAdd = TypicalTestTasks.flight;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
    }
    
     @Test
     public void testAddTask_duplicateTasks_errorMessageExpected() throws DuplicateTaskException, IllegalValueException {
    	 TestTask[] currentList = td.getTypicalTasks();
    	 commandBox.runCommand(TypicalTestTasks.study.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));
     }
     
     @Test
     public void testAddTask_emptyTaskManager_addedTaskExpected() throws DuplicateTaskException, IllegalValueException {
        commandBox.runCommand("clear");
        assertAddSuccess(TypicalTestTasks.homework);
     }

     @Test
     public void testAddTask_invalidCommand_errorMessageExpected() throws DuplicateTaskException, IllegalValueException {
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
        
        commandBox.runCommand("add");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    //helper method for main test
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

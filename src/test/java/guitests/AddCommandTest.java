package guitests;

import guitests.guihandles.TaskCardHandle;
import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.logic.commands.AddCommand;
import hard2do.taskmanager.testutil.TestTask;
import hard2do.taskmanager.testutil.TestUtil;
import hard2do.taskmanager.testutil.TypicalTestTasks;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
//@@author A0139523E-reused
public class AddCommandTest extends TaskManagerGuiTest {

    @Test
    public void add() {
        //add one task

    	TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.appointment;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
   
        //add another task
        taskToAdd = TypicalTestTasks.flight;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add duplicate task
        commandBox.runCommand(TypicalTestTasks.flight.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand("clear");
        
        assertAddSuccess(TypicalTestTasks.homework);

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

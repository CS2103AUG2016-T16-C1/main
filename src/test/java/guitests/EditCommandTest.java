package guitests;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.logic.commands.EditCommand;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;
import hard2do.taskmanager.model.task.Content;
import hard2do.taskmanager.model.task.TaskDate;
import hard2do.taskmanager.model.task.UniqueTaskList.DuplicateTaskException;
import hard2do.taskmanager.testutil.TestTask;

public class EditCommandTest extends TaskManagerGuiTest {
	
	@Test
	public void testEdit_differentIndex_updatedTasksExpected() throws DuplicateTaskException, IllegalValueException, ParseException {
		TestTask[] currentList = td.getTypicalTasks();
		int targetIndex = 1;
		//edit task details to the first of the list
		assertEditTaskSuccess(targetIndex, currentList);
		
		//edit task details to the middle of the list
		targetIndex = 4;
		assertEditTaskSuccess(targetIndex, currentList);
	}
	
	@Test
	public void testEdit_floatingTask_updatedTasksExpected()  throws DuplicateTaskException, IllegalValueException, ParseException {
		TestTask[] currentList = td.getTypicalTasks();
		commandBox.runCommand("add Pay the bills");
		//edit task details of a floating task
		int targetIndex = 7;
		assertEditTaskSuccess(targetIndex, currentList);
	}

	@Test
	public void testEdit_invalidIndex_errorMessageExpected() {
        //edit an invalid index
        commandBox.runCommand("edit " + "100 " + "c/movie");    
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
	}
	
    private void assertEditTaskSuccess(int targetIndexOneIndexed, final TestTask[] currentList) throws DuplicateTagException, IllegalValueException, ParseException {
        TestTask taskToEdit = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing

        taskToEdit.setContent(new Content("do CS2103"));
        taskToEdit.setDate(new TaskDate("22-01-2016"));
        commandBox.runCommand("edit " + targetIndexOneIndexed + " c/do CS2103 sd/22-01-2016");

        //confirm added Task has the new tag
        TaskCardHandle editedTaskCard = taskListPanel.navigateToTask(taskToEdit.getContent().value);
        assertEquals(taskToEdit.getContent().value, editedTaskCard.getContent());
        assertEquals(taskToEdit.getDate().dateString, editedTaskCard.getDate());
        
        //confirm the result message is correct
        assertResultMessage(String.format(EditCommand.MESSAGE_SUCCESS, taskToEdit));
    }
}




package guitests;

import guitests.guihandles.TaskCardHandle;
import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.logic.commands.AddCommand;
import hard2do.taskmanager.logic.commands.AddTagCommand;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;
import hard2do.taskmanager.testutil.TestTask;
import hard2do.taskmanager.testutil.TestUtil;
import hard2do.taskmanager.testutil.TypicalTestTasks;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


//@@author A0147989B
public class RecurringTaskTest extends TaskManagerGuiTest {

    @Test
    public void add() {
        //add one recurring task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.appointment;
        commandBox.runCommand(taskToAdd.getAddCommand());
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
        assertNextSuccess(7,currentList);
    }
    
    @Test
    public void testNext_invalidIndex_errorMessageExpected() throws DuplicateTagException, IllegalValueException {
        commandBox.runCommand("next " + "100");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    private void assertNextSuccess(int index, final TestTask[] currentList) {
        TestTask taskToNext = currentList[index-1];
        commandBox.runCommand("next " + index);
        
        //confirm date already changed
        TaskCardHandle nextedCard = taskListPanel.navigateToTask(taskToNext.getContent().value);
        assertEquals(taskToNext.getDate().toString(), nextedCard.getDate());
    }

}

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
        commandBox.runCommand("addtag 7 testtag");
        commandBox.runCommand("deltag 7 testtag");
        commandBox.runCommand("edit 7 c/very important appointment sd/12-12-2015 st/11:00");
        commandBox.runCommand("important 7");
        commandBox.runCommand("unimportant 7");
        commandBox.runCommand("done 7");
        commandBox.runCommand("list all");
        commandBox.runCommand("notdone 7");
        
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
    }

}

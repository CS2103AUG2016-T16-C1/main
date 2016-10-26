package guitests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DoneCommand;
import seedu.address.logic.commands.ImportantCommand;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;
import seedu.address.testutil.TestTask;
import org.junit.Test;


public class DoneCommandTest extends TaskManagerGuiTest {

    
    @Test
    public void done() {
        TestTask[] currentList = td.getTypicalTasks();
        //done first item
        int targetIndex = 1;
        //assertDoneTaskSuccess(targetIndex, currentList);
        
        //done a middle item
        targetIndex = 4;
        //assertDoneTaskSuccess(targetIndex, currentList);
        
        //done already done item
        commandBox.runCommand("list");
        //assertDoneTaskSuccess(targetIndex, currentList);

        
    }
    
    @Test
    public void doneInvalidIndex() {
        //done a invalid index
        commandBox.runCommand("done " + "100");    
        //assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        
    }
    
    private void assertDoneTaskSuccess(int targetIndexOneIndexed, final TestTask[] currentList) throws IllegalStateException {
        TestTask taskToDone = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        taskToDone.setDone();
        commandBox.runCommand("done " + targetIndexOneIndexed);

        //confirm marking the task as done, item is not in the undone list
        assertNull(taskListPanel.navigateToTask(taskToDone.getContent().value));
        //confirm the result message is correct
        //assertResultMessage(String.format(DoneCommand.MESSAGE_DONE_TASK_SUCCESS, taskToDone));
        
        //confirm task is in the whole list
        commandBox.runCommand("list");
        assertNotNull(taskListPanel.navigateToTask(taskToDone.getContent().value));


    }
}

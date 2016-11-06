package guitests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.logic.commands.DoneCommand;
import hard2do.taskmanager.logic.commands.ImportantCommand;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;
import hard2do.taskmanager.testutil.TestTask;

//@@author A0147989B
public class ImportantCommandTest extends TaskManagerGuiTest {

    @Test
    public void testImportant_differentIndex_updatedImportantListExpected() {
        TestTask[] currentList = td.getTypicalTasks();
        
        //important first item
        int targetIndex = 1;
        assertImportantTaskSuccess(targetIndex, currentList);
        
        //important a middle item
        targetIndex = 4;
        assertImportantTaskSuccess(targetIndex, currentList);
        
        //important already important item
        assertImportantTaskSuccess(targetIndex, currentList);
     
    }
    
    @Test
    public void testImportant_invalidIndex_errorMessageExpected() {
        //important a invalid index
        commandBox.runCommand("important " + "100");    
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        
    }
    
    //helper method for main test
    private void assertImportantTaskSuccess(int index, final TestTask[] currentList) throws IllegalStateException {
        TestTask taskToImportant = currentList[index-1]; //-1 because array uses zero indexing
        taskToImportant.setImportant();
        commandBox.runCommand("important " + index);

        //confirm marking the task as important
        assertEquals(taskToImportant.getImportant(), false);
        
        //confirm the result message is correct
        assertResultMessage(String.format(ImportantCommand.MESSAGE_IMPORTANT_TASK_SUCCESS, taskToImportant));
        
    }
}

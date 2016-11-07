package guitests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.logic.commands.DoneCommand;
import hard2do.taskmanager.logic.commands.ImportantCommand;
import hard2do.taskmanager.logic.commands.UnimportantCommand;
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
        
        //unimportant the first item
        targetIndex = 1;
        assertUnimportantTaskSuccess(targetIndex, currentList);
        
        //unimportant already unimportant item
        targetIndex = 1;
        assertImportantTaskSuccess(targetIndex, currentList);
        
        //important already important item
        targetIndex = 4;
        assertImportantTaskSuccess(targetIndex, currentList);
     
    }
    
    @Test
    public void testImportant_invalidIndex_errorMessageExpected() {
        //important a invalid index
        commandBox.runCommand("important " + "100");    
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        
    }
    
    @Test
    public void testUnimportant_invalidIndex_errorMessageExpected() {
        //important a invalid index
        commandBox.runCommand("unimportant " + "100");    
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        
    }
    
    @Test
    public void testUImportant_noIndex_errorMessageExpected() {
    	commandBox.runCommand("unimportant");
    	assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, UnimportantCommand.MESSAGE_USAGE));
    }
    
    @Test
    public void testImportant_noIndex_errorMessageExpected() {
    	commandBox.runCommand("important");
    	assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ImportantCommand.MESSAGE_USAGE));
    }
    
    //helper method for main test
    private void assertImportantTaskSuccess(int index, final TestTask[] currentList) throws IllegalStateException {
        TestTask taskToImportant = currentList[index-1]; //-1 because array uses zero indexing
        taskToImportant.setImportant();
        commandBox.runCommand("important " + index);

        //confirm marking the task as important
        assertEquals(taskToImportant.getImportant(), true);
        
        //confirm the result message is correct
        assertResultMessage(String.format(ImportantCommand.MESSAGE_IMPORTANT_TASK_SUCCESS, taskToImportant));
        
    }
    
    private void assertUnimportantTaskSuccess(int index, final TestTask[] currentList) throws IllegalStateException {
        TestTask taskToUnimportant = currentList[index-1]; //-1 because array uses zero indexing
        taskToUnimportant.setImportant();
        commandBox.runCommand("unimportant " + index);

        //confirm marking the task as unimportant
        assertEquals(taskToUnimportant.getImportant(), true);
        
        //confirm the result message is correct
        assertResultMessage(String.format(UnimportantCommand.MESSAGE_UNIMPORTANT_TASK_SUCCESS, taskToUnimportant));
        
    }
}

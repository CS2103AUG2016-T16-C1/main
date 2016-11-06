package guitests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import guitests.guihandles.TaskCardHandle;
import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.logic.commands.AddTagCommand;
import hard2do.taskmanager.logic.commands.EditCommand;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;
import hard2do.taskmanager.testutil.TestTask;

//@@author A0141054W
public class AddTagCommandTest extends TaskManagerGuiTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void testAddTag_differentIndex_updatedTasksExpected() throws DuplicateTagException, IllegalValueException {
        TestTask[] currentList = td.getTypicalTasks();
        int targetIndex = 1;
        String newTag = "newtag";
        //add tag to the first of the list
        assertAddTagSuccess(targetIndex, currentList, newTag);
        
        //add tag to the middle of the list
        targetIndex = 4;
        assertAddTagSuccess(targetIndex, currentList, newTag);
        
        //add tag to item with no task
        targetIndex = 2;
        assertAddTagSuccess(targetIndex, currentList, newTag);
    }
    
    
    @Test
    public void testAddTag_invalidIndex_errorMessageExpected() throws DuplicateTagException, IllegalValueException {
        commandBox.runCommand("addtag " + "100 " + "newTag");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }
    
    @Test
    public void testAddTag_invalidTag_errorMessageExpected() throws DuplicateTagException, IllegalValueException {
        commandBox.runCommand("addtag " + "1 " + "newTag$^&");
        assertResultMessage(AddTagCommand.MESSAGE_INVALID_TAG);
    }
    
    //helper method for main test
    private void assertAddTagSuccess(int targetIndexOneIndexed, final TestTask[] currentList, String newTag) throws DuplicateTagException, IllegalValueException {
        TestTask taskToAddTag = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        ArrayList<String> tagList = new ArrayList<String>();
        tagList.add(newTag);
        taskToAddTag.addTags(tagList);
        commandBox.runCommand("addtag " + targetIndexOneIndexed + " " + newTag);

        //confirm added Task has the new tag
        TaskCardHandle addedTagCard = taskListPanel.navigateToTask(taskToAddTag.getContent().value);
        assertEquals(taskToAddTag.tagsString(), addedTagCard.getTag());
        
        //confirm the result message is correct
        assertResultMessage(String.format(EditCommand.MESSAGE_SUCCESS, taskToAddTag));
    }
}

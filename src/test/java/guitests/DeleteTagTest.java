package guitests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.logic.commands.AddTagCommand;
import hard2do.taskmanager.logic.commands.DeleteTagCommand;
import hard2do.taskmanager.logic.commands.EditCommand;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;
import hard2do.taskmanager.testutil.TestTask;

public class DeleteTagTest extends TaskManagerGuiTest {
    
    @Test
    public void testDeleteTag_differentIndex_updatedTasksExpected() throws DuplicateTagException, IllegalValueException {
        TestTask[] currentList = td.getTypicalTasks();
        int targetIndex = 1;
        String tagToDelete = "shag";
        //delete tag to the first of the list
        assertDeleteTagSuccess(targetIndex, currentList, tagToDelete);
        
        //delete tag to the middle of the list
        targetIndex = 3;
        tagToDelete = "tired";
        assertDeleteTagSuccess(targetIndex, currentList, tagToDelete);
        
        //delete tag at the end of list
        targetIndex = 7;
        tagToDelete = "food";
        assertDeleteTagSuccess(targetIndex, currentList, tagToDelete);
    }
    
    @Test
    public void testDeleteTag_invalidIndex_errorMessageExpected() throws DuplicateTagException, IllegalValueException {
        commandBox.runCommand("deltag " + "100 " + "newTag");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }
    
    @Test
    public void testDeleteTag_invalidTag_errorMessageExpected() throws DuplicateTagException, IllegalValueException {
        commandBox.runCommand("deltag " + "1 " + "newTag$^&");
        assertResultMessage(DeleteTagCommand.MESSAGE_INVALID_TAG);
    }
    
    @Test
    public void testDeleteTag_notExistingTag_errorMessageExpected() {
        commandBox.runCommand("deltag " + "1" + " notExisting");
        assertResultMessage("Task does not have Tag: notExisting");
    }
    //helper method for main test
    private void assertDeleteTagSuccess(int targetIndexOneIndexed, final TestTask[] currentList, String tagToDelete) throws DuplicateTagException, IllegalValueException {
        TestTask taskToDeleteTag = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        ArrayList<String> tagList = new ArrayList<String>();
        tagList.add(tagToDelete);
        taskToDeleteTag.deleteTags(tagList);
        commandBox.runCommand("deltag " + targetIndexOneIndexed + " " + tagToDelete);

        //confirm added Task des not have the deleted tag
        TaskCardHandle addedTagCard = taskListPanel.navigateToTask(taskToDeleteTag.getContent().value);
        assertEquals(taskToDeleteTag.tagsString(), addedTagCard.getTag());
        
        //confirm the result message is correct
        assertResultMessage(String.format(EditCommand.MESSAGE_SUCCESS, taskToDeleteTag));
    }
}

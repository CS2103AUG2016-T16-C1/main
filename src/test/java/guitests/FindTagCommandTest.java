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
import hard2do.taskmanager.logic.commands.FindTagCommand;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;
import hard2do.taskmanager.testutil.TestTask;

//@@author A0147989B
public class FindTagCommandTest extends TaskManagerGuiTest {
    
    @Test
    public void testFindTag_updatedTasksExpected() throws DuplicateTagException, IllegalValueException {
        TestTask[] currentList = td.getTypicalTasks();
        String tagToFind = "shag";
        //find tag to the first of the list
        assertFindTagSuccess(currentList, tagToFind);
        
        //find tag to the middle of the list
        tagToFind = "tired";
        assertFindTagSuccess(currentList, tagToFind);
        
        //find tag at the end of list
        tagToFind = "food";
        assertFindTagSuccess(currentList, tagToFind);
    }
    
    
    @Test
    public void testDeleteTag_notExistingTag_errorMessageExpected() {
        commandBox.runCommand("findtag notExisting");
        assertResultMessage("0 tasks listed!");
    }
    
    //helper method for main test
    private void assertFindTagSuccess(final TestTask[] currentList, String tagToFind) throws DuplicateTagException, IllegalValueException {
        commandBox.runCommand("findtag " + tagToFind);

        //confirm the result message is correct
        assertResultMessage("1 tasks listed!");
    }
}

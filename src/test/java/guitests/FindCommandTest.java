package guitests;

import org.junit.Test;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.testutil.TestTask;

import static org.junit.Assert.assertTrue;
//@@author A0139523E-reused
public class FindCommandTest extends TaskManagerGuiTest {

    @Test
    public void find_nonEmptyList() {
        assertFindResult("find Mark"); //no results
        assertFindResult("find Study", td.study, td.activities); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find Study",td.activities);
    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find Jean"); //no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, TestTask... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}

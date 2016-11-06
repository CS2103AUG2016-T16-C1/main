package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import hard2do.taskmanager.testutil.TestTask;

//@@author A0139523E
public class ListCommandTest extends TaskManagerGuiTest {
	
	@Test
	public void testList_listAll_correctListExpected() {
		TestTask[] currentList = td.getTypicalTasks();
		commandBox.runCommand("list all");
		assertTrue(taskListPanel.isListMatching(currentList));
	}
	
	@Test
	public void testList_listDone_correctListExpected() {
		commandBox.runCommand("done 1");
		commandBox.runCommand("done 2");
		commandBox.runCommand("list done");
		assertListSize(2);
	}
	
	@Test
	public void testList_listImportant_correctListExpected() {
		commandBox.runCommand("important 3");
		commandBox.runCommand("important 1");
		commandBox.runCommand("list important");
		assertListSize(2);
	}
	
	@Test
	public void testList_listUnimportant_correctListExpected() {
		commandBox.runCommand("important 1");
		commandBox.runCommand("important 2");
		commandBox.runCommand("list unimportant");
		assertListSize(5);
	}
}
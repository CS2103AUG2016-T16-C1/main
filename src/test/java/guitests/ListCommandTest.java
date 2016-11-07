package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.logic.commands.AddCommand;
import hard2do.taskmanager.logic.commands.ListCommand;
import hard2do.taskmanager.testutil.TestTask;

//@@author A0139523E
public class ListCommandTest extends TaskManagerGuiTest {
	
	@Test
	public void testList_listAll_correctListExpected() {
		TestTask[] currentList = td.getTypicalTasks();
		commandBox.runCommand("list all");
		assertListAllSuccess(currentList);
	}
	
	@Test
	public void testList_listAllShortcut_correctListExpected() {
		TestTask[] currentList = td.getTypicalTasks();
		commandBox.runCommand("list -a");
		assertListAllSuccess(currentList);
	}
	
	@Test
	public void testList_listUndone_correctListExpected() {
		TestTask[] currentList = td.getTypicalTasks();
		commandBox.runCommand("list");
		assertListAllSuccess(currentList);
	}
	
	@Test
	public void testList_listDone_correctListExpected() {
		commandBox.runCommand("done 1");
		commandBox.runCommand("done 2");
		commandBox.runCommand("list done");
		assertListChangeSuccess(2);
	}
	
	@Test
	public void testList_listDoneShortcut_correctListExpected() {
		commandBox.runCommand("done 1");
		commandBox.runCommand("done 2");
		commandBox.runCommand("list -d");
		assertListChangeSuccess(2);
	}
	
	
	@Test
	public void testList_listImportant_correctListExpected() {
		commandBox.runCommand("important 3");
		commandBox.runCommand("important 1");
		commandBox.runCommand("list important");
		assertListChangeSuccess(2);
	}
	
	@Test
	public void testList_listImportantShortcut_correctListExpected() {
		commandBox.runCommand("important 3");
		commandBox.runCommand("important 1");
		commandBox.runCommand("list -i");
		assertListChangeSuccess(2);
	}
	
	@Test
	public void testList_listUnimportant_correctListExpected() {
		commandBox.runCommand("important 1");
		commandBox.runCommand("important 2");
		commandBox.runCommand("list unimportant");
		assertListChangeSuccess(5);
	}
	
	@Test
	public void testList_listUnimportantShortcut_correctListExpected() {
		commandBox.runCommand("important 1");
		commandBox.runCommand("important 2");
		commandBox.runCommand("list -ui");
		assertListChangeSuccess(5);
	}
	
	@Test
	public void testList_invalidCommand_errorMessageExpected() {
		commandBox.runCommand("list me");
		assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_LIST_RESTRICTION));
	}
	
	public void assertListAllSuccess(final TestTask[] currentList) {
		assertTrue(taskListPanel.isListMatching(currentList));
	}

	public void assertListChangeSuccess(int taskChanged) {
		assertListSize(taskChanged);
	}
}
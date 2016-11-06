package guitests;

import org.junit.Test;

import hard2do.taskmanager.commons.core.Messages;

public class EditCommandTest extends TaskManagerGuiTest {
	
	@Test
	public void testEdit_invalidIndex_errorMessageExpected() {
        //edit an invalid index
        commandBox.runCommand("edit " + "100 " + "c/movie");    
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
	}
}



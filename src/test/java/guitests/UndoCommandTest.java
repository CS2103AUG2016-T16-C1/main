package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


//@@author A0141054W
public class UndoCommandTest extends TaskManagerGuiTest{
    
    @Test
    public void testUndo_oneCommand_previousStateExpected() {
        //undo 1 command
        commandBox.runCommand(td.appointment.getAddCommand());
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }
    
    @Test
    public void testUndo_multipleCommand_originalStateExpected() {
        //undo more than 1 command
        commandBox.runCommand(td.appointment.getAddCommand());
        commandBox.runCommand(td.flight.getAddCommand());
        commandBox.runCommand("delete 1");
        commandBox.runCommand("edit 2 c/nothing sd/10-10-2010");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }
    
    @Test
    public void testUndo_noCommand_errorMessageExpected() {
        commandBox.runCommand("undo");
        assertResultMessage("No available commands can be undone");
    }
    
    @Test
    public void testUndo_noChanges_errorMessageExpected() {
    	//undo doesn't work with commands that doesn't change information
    	commandBox.runCommand("find homework");
    	commandBox.runCommand("list all");
    	commandBox.runCommand("undo");
    	assertResultMessage("No available commands can be undone");
    }
}

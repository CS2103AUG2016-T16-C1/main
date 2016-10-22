package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;



public class UndoCommandTest extends TaskManagerGuiTest{
    
    @Test
    public void undoSingleCommand() {
        //undo 1 command
        commandBox.runCommand(td.appointment.getAddCommand());
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }
    
    @Test
    public void undoMultipleCommand() {
        //undo more than 1 command
        commandBox.runCommand(td.appointment.getAddCommand());
        commandBox.runCommand(td.flight.getAddCommand());
        commandBox.runCommand("delete 1");
        commandBox.runCommand("edit 2 c/nothing d/10-10-2010");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }
    
    @Test
    public void undoNoCommand() {
        commandBox.runCommand("undo");
        assertResultMessage("No available commands can be undone");

    }
    
}

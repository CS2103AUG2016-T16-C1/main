package guitests;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import hard2do.taskmanager.commons.core.Messages;
import hard2do.taskmanager.commons.util.FileUtil;
import hard2do.taskmanager.logic.commands.LoadCommand;
import hard2do.taskmanager.logic.commands.SaveCommand;
import hard2do.taskmanager.testutil.TestTask;


//@@author A0141054W
public class SaveLoadCommandTest extends TaskManagerGuiTest {
    
    @Test
    public void testSave_newFileExpected() {
        TestTask[] currentList = td.getTypicalTasks();
        String savePath = "./src/test/data/savedFile.xml";
        commandBox.runCommand("save " + savePath);
        
        File saveFile = new File(savePath);
        assertTrue(FileUtil.isFileExists(saveFile));
        
        assertResultMessage(SaveCommand.MESSAGE_SUCCESS);
    }
    
    @Test
    public void testSave_invalidPath_errorMessageExpected() {
        String savePath = "not a file path";
        commandBox.runCommand("save " + savePath);
        assertResultMessage(SaveCommand.MESSAGE_INVALID_FILENAME);        
    }
    
    @Test
    public void testLoad_justSavedFile_successMessageExpected() {
        String savePath = "./src/test/data/savedFile.xml";
        commandBox.runCommand("load " + savePath);
        assertResultMessage(LoadCommand.MESSAGE_SUCCESS);        
    }
    
    @Test
    public void testLoad_invalidPath_errorMessageExpected() {
        String savePath = "not_a_file_path";
        commandBox.runCommand("load " + savePath);
        assertResultMessage("Invalid command format! \n" + LoadCommand.MESSAGE_INVALID_FILEPATH);        
    }
}

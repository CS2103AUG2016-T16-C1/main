package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
//@@author A0141054W

/**
 * A handler for the TaskDetail of the UI
 */
public class TaskDetailHandle extends GuiHandle {
    private static final String CONTENT_FIELD_ID = "#content";
    private static final String DATE_FIELD_ID = "#datePicker";
    private static final String Time_FIELD_ID = "#timePicker";
    
    public TaskDetailHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }   
}

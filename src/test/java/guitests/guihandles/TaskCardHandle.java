package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.address.model.person.ReadOnlyTask;

/**
 * Provides a handle to a task card in the person list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String CONTENT_FIELD_ID = "#content";
    private static final String DATE_FIELD_ID = "#date";
    private static final String TAG_FIELD_ID = "#tags";


    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getContent() {
        return getTextFromLabel(CONTENT_FIELD_ID);
    }

    public String getDate() {
        return getTextFromLabel(DATE_FIELD_ID);
    }
    
    public String getTag() {
        return getTextFromLabel(TAG_FIELD_ID);
    }



    public boolean isSameTask(ReadOnlyTask task){
        return getContent().toString().equals(task.getContent().value) && getDate().toString().equals(task.getDate().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getContent().equals(handle.getContent())
                    && getDate().equals(handle.getDate());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getContent() + " " + getDate();
    }
}

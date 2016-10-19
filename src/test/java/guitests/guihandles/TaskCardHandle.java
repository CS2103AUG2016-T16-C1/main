package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.address.model.person.ReadOnlyTask;

/**
 * Provides a handle to a person card in the person list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String CONTENT_FIELD_ID = "#content";
    private static final String DATE_FIELD_ID = "#date";
    private static final String TIME_FIELD_ID = "#time";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getFullName() {
        return getTextFromLabel(CONTENT_FIELD_ID);
    }

    public String getDate() {
        return getTextFromLabel(DATE_FIELD_ID);
    }

    public String getTime() {
        return getTextFromLabel(TIME_FIELD_ID);
    }

    public boolean isSamePerson(ReadOnlyTask task){
        return getFullName().equals(task.getContent().value) && getDate().equals(task.getDate().value)
                && getTime().equals(task.getTime().value);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getFullName().equals(handle.getFullName())
                    && getDate().equals(handle.getDate())
                    && getTime().equals(handle.getTime());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getFullName() + " " + getDate() + " " + getTime();
    }
}

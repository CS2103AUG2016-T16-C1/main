package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.address.model.person.ReadOnlyTask;

public class TaskDetail extends UiPart {
    
    private static final String FXML = "TaskDetail.fxml";
    
    @FXML
    private Label content;
    @FXML
    private Label time;
    @FXML
    private Label tags;
    @FXML
    private Label date;
    
    private ReadOnlyTask task;

    public TaskDetail() {
    }
    

    public static TaskDetail load(Stage primaryStage, AnchorPane placeHolder){
        TaskDetail detail = UiPartLoader.loadUiPart(primaryStage, placeHolder, new TaskDetail());
        return detail;
    }
    
    public void loadTaskDetail(ReadOnlyTask task) {
        this.task = task;
        this.initialize();
    }

    @FXML
    public void initialize() {
        content.setText(task.getContent().toString());
        date.setText(task.getDate().toString());
        time.setText(task.getTime().toString());

        tags.setText(task.tagsString());
    }
    
    @Override
    public void setNode(Node node) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

}

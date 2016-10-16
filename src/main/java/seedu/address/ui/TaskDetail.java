package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.model.person.ReadOnlyTask;

public class TaskDetail extends UiPart {

    private static final String FXML = "TaskDetail.fxml";
    
    private AnchorPane placeHolderPane;
    private AnchorPane taskDetailPane;
    
    @FXML
    private VBox detailView;

    @FXML
    private Label content;
    @FXML
    private Label time;
    @FXML
    private Label tags;
    @FXML
    private Label date;

    public TaskDetail() {
    }

    public static TaskDetail load(Stage primaryStage, AnchorPane placeHolder) {
        System.out.println("TaskDetail is called");
        TaskDetail detail = UiPartLoader.loadUiPart(primaryStage, placeHolder, new TaskDetail());
        detail.addToPlaceHolder();
        return detail;
    }

    public void loadTaskDetail(ReadOnlyTask task) {
        content.setText(task.getContent().toString());
        date.setText(task.getDate().toString());
        time.setText(task.getTime().toString());

        tags.setText(task.tagsString());    
    }
    
    private void addToPlaceHolder() {
        placeHolderPane.getChildren().add(detailView);
    }


    @Override
    public void setNode(Node node) {
        taskDetailPane = (AnchorPane) node;
    }
    
    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

}

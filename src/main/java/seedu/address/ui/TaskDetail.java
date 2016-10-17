package seedu.address.ui;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.jfoenix.controls.JFXDatePicker;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.model.person.ReadOnlyTask;

public class TaskDetail extends UiPart {

    private static final String FXML = "TaskDetail.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane taskDetailPane;
    
    @FXML
    private DatePicker datePicker;
    @FXML
    private VBox detailView;
    @FXML
    private Label content;
    @FXML
    private Label tags;
    @FXML
    private JFXDatePicker timePicker;

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
        if (task.getDate().getValue() != null) {
            datePicker.setValue(DateTimeUtil.changeDateToLocalDate(task.getDate().getValue()));
        }
        else {
            datePicker.setValue(null);
        }
        if (task.getTime().getValue() != null) {

            timePicker.setTime(DateTimeUtil.changeDateToLocalTime(task.getTime().getValue()));
        }
        else {
            timePicker.setValue(null);
        }
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

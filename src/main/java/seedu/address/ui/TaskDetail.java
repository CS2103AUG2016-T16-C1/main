package seedu.address.ui;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.model.person.ReadOnlyTask;

public class TaskDetail extends UiPart {

    private static final String FXML = "TaskDetail.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane taskDetailPane;
    private DatePicker datePicker;
    @FXML
    private VBox detailView;

    @FXML
    private Label content;
    @FXML
    private AnchorPane timePane;
    @FXML
    private Label tags;
    @FXML
    private Label time;

    public TaskDetail() {
    }

    public static TaskDetail load(Stage primaryStage, AnchorPane placeHolder) {
        System.out.println("TaskDetail is called");
        TaskDetail detail = UiPartLoader.loadUiPart(primaryStage, placeHolder, new TaskDetail());
        detail.addToPlaceHolder();
        detail.initiateDatePicker();
        return detail;
    }

    public void loadTaskDetail(ReadOnlyTask task) {
        content.setText(task.getContent().toString());
        time.setText(task.getTime().toString());
        if (task.getDate().getValue() != null) {
            Instant instant = task.getDate().getValue().toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate date = zdt.toLocalDate();
            datePicker.setValue(date);
        }
        else {
            datePicker.setValue(null);
        }
        tags.setText(task.tagsString());
    }

    private void initiateDatePicker() {
        datePicker = new DatePicker();
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });
        timePane.getChildren().add(datePicker);
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

package seedu.address.ui;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.swing.text.AbstractDocument.Content;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.logic.Logic;
import seedu.address.model.person.ReadOnlyTask;

public class TaskDetail extends UiPart {

    private static final String FXML = "TaskDetail.fxml";
    private Logic logic;

    private AnchorPane placeHolderPane;
    private AnchorPane taskDetailPane;
    
    @FXML
    private DatePicker datePicker;
    @FXML
    private VBox detailView;
    @FXML
    private JFXTextField content;
    @FXML
    private Label tags;
    @FXML
    private JFXDatePicker timePicker;

    public TaskDetail() {
    }

    public static TaskDetail load(Stage primaryStage, AnchorPane placeHolder, Logic logic) {
        TaskDetail detail = UiPartLoader.loadUiPart(primaryStage, placeHolder, new TaskDetail());
        detail.configure(logic);
        detail.addToPlaceHolder();      
        detail.initializeTextField();
        return detail;
    }
    
    private void configure(Logic logic) {
        this.logic = logic;
    }
    
    private void initializeTextField() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        content.getValidators().add(validator);
        validator.setMessage("No Input Given");
        content.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    content.validate();
                }
            } 
        });
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

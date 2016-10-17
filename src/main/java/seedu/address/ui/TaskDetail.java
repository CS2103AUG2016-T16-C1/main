package seedu.address.ui;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

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
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.person.ReadOnlyTask;

public class TaskDetail extends UiPart {
    private final Logger logger = LogsCenter.getLogger(TaskDetail.class);
    private static final String FXML = "TaskDetail.fxml";
    private Logic logic;
    private String newContent;
    private int index;
    private ResultDisplay resultDisplay;
    private CommandResult mostRecentResult;


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

    public static TaskDetail load(Stage primaryStage, AnchorPane placeHolder, ResultDisplay resultDisplay, Logic logic) {
        TaskDetail detail = UiPartLoader.loadUiPart(primaryStage, placeHolder, new TaskDetail());
        detail.configure(resultDisplay, logic);
        detail.addToPlaceHolder();      
        detail.initializeTextField();
        return detail;
    }
    
    private void configure(ResultDisplay resultDisplay, Logic logic) {
        this.logic = logic;
        this.resultDisplay = resultDisplay;
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

    public void loadTaskDetail(ReadOnlyTask task, int index) {
        this.index = index + 1;
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
    
    @FXML
    private void handleContentChanged() throws ParseException {
        logger.info("changed is called");
        newContent = content.getText();
        mostRecentResult = logic.execute("edit " + index + " c/" + newContent);
        resultDisplay.postMessage(mostRecentResult.feedbackToUser);
        logger.info("Result: " + mostRecentResult.feedbackToUser);
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

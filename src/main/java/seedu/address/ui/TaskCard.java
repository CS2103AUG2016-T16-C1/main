package seedu.address.ui;

import java.text.ParseException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import seedu.address.logic.Logic;
import seedu.address.model.person.ReadOnlyTask;

public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";
    private Logic logic;


    @FXML
    private HBox cardPane;
    @FXML
    private Label content;
    @FXML
    private Label id;
    @FXML
    private Label tags;
    @FXML
    private Label date;
    @FXML
    private CheckBox doneCheckBox;
    @FXML
    private Circle importantCircle;

    private ReadOnlyTask task;
    private int displayedIndex;

    public TaskCard(){

    }

    public static TaskCard load(ReadOnlyTask task, int displayedIndex, Logic logic){
        TaskCard card = new TaskCard();
        card.logic = logic;
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        content.setText(task.getContent().toString());
        id.setText(displayedIndex + ". ");
        date.setText(task.getDate().toString());
        doneCheckBox.setSelected(task.getDone());
        if (task.getImportant()) {
        	importantCircle.setFill(Paint.valueOf("red"));
        }
        else {
        	importantCircle.setFill(Paint.valueOf("green"));

        }

        tags.setText(task.tagsString());
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
    
    @FXML
    public void handleDoneCheckBox() throws ParseException {
    	if (doneCheckBox.isSelected()) {
    		logic.execute("done " + displayedIndex);
    	}
    	else {
    		logic.execute("undone " + displayedIndex);
    	}
    }
}

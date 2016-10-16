package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

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

    
    @Override
    public void setNode(Node node) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

}

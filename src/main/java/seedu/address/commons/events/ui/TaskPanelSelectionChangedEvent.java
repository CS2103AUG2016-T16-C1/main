package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.ReadOnlyTask;

/**
 * Represents a selection change in the Person List Panel
 */
//@@author A0141054W-reused
public class TaskPanelSelectionChangedEvent extends BaseEvent {


    private final ReadOnlyTask newSelection;
    private final int newIndex;

    public TaskPanelSelectionChangedEvent(ReadOnlyTask newSelection, int newIndex){
        this.newSelection = newSelection;
        this.newIndex = newIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ReadOnlyTask getNewSelection() {
        return newSelection;
    }

    public int getNewIndex() {
        return newIndex;
    }
}

package hard2do.taskmanager.commons.events.model;

import hard2do.taskmanager.commons.events.BaseEvent;
import hard2do.taskmanager.model.ReadOnlyTaskManager;

//@@author A0135787N-reused
/** Indicates the TaskManager in the model has changed*/
public class TaskManagerChangedEvent extends BaseEvent {

    public final ReadOnlyTaskManager data;

    public TaskManagerChangedEvent(ReadOnlyTaskManager data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}

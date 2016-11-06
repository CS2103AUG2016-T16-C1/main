package hard2do.taskmanager.model;

import java.util.List;

import hard2do.taskmanager.model.tag.Tag;
import hard2do.taskmanager.model.tag.UniqueTagList;
import hard2do.taskmanager.model.task.ReadOnlyTask;
import hard2do.taskmanager.model.task.UniqueTaskList;

/**
 * Unmodifiable view of all Tasks
 */
//@@author A0139523E-reused
public interface ReadOnlyTaskManager {

    UniqueTagList getUniqueTagList();

    UniqueTaskList getUniqueTaskList();

    /**
     * Returns an unmodifiable view of tasks list
     */
    List<ReadOnlyTask> getTaskList();

    /**
     * Returns an unmodifiable view of tags list
     */
    List<Tag> getTagList();

}


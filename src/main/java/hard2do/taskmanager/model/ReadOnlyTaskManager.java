package hard2do.taskmanager.model;

import java.util.List;

import hard2do.taskmanager.model.person.ReadOnlyTask;
import hard2do.taskmanager.model.person.UniqueTaskList;
import hard2do.taskmanager.model.tag.Tag;
import hard2do.taskmanager.model.tag.UniqueTagList;

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


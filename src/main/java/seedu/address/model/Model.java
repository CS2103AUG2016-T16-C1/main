package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.History.StateNotFoundException;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.Task;
import seedu.address.model.person.UniqueTaskList;
import seedu.address.model.person.UniqueTaskList.TaskNotFoundException;
import seedu.address.model.tag.Tag;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. 
     * @throws ParseException 
     * @throws IllegalValueException */
    void resetData(ReadOnlyTaskManager newData) throws IllegalValueException, ParseException;

    /** Returns the TaskManager */
    ReadOnlyTaskManager getTaskManager();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);
    
    /** Updates the filter of the filtered task list to filter by the closest edit distance to given string input*/
    void updateFilteredTaskList(String toFind);
    
    /** Updates the filter of the filtered task list to filter by the given Tag*/
    void updateFilteredTaskList(Tag tagToFind);
    
    /** Edit the given task. 
     * @throws ParseException */
	void editTask(int targetIndex, String newDate, String newTime, String newContent) 
			throws TaskNotFoundException, ParseException;

    /** Mark the given task as done. */
    void doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Mark the given task as undone. */
    void undoneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

	void save(String commandType) throws IllegalValueException, ParseException;

	void undo() throws StateNotFoundException;

	History getHistory();

	void updateFilteredListToShowDone();
	
	void updateFilteredListToShowUndone();


	void addTags(ReadOnlyTask target, ArrayList<String> newTag) 
			throws TaskNotFoundException, ParseException, IllegalValueException;

	void deleteTags(ReadOnlyTask taskToDelTags, ArrayList<String> tagsToDel) 
			throws TaskNotFoundException, ParseException, IllegalValueException;

}
